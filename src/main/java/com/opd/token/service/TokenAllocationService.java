package com.opd.token.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opd.token.domain.entity.Patient;
import com.opd.token.domain.entity.Slot;
import com.opd.token.domain.entity.Token;
import com.opd.token.domain.enums.Priority;
import com.opd.token.domain.enums.TokenStatus;
import com.opd.token.repository.PatientRepository;
import com.opd.token.repository.SlotRepository;
import com.opd.token.repository.TokenRepository;

@Service
public class TokenAllocationService {

    private final SlotRepository slotRepository;
    private final PatientRepository patientRepository;
    private final TokenRepository tokenRepository;

    public TokenAllocationService(SlotRepository slotRepository,
                                  PatientRepository patientRepository,
                                  TokenRepository tokenRepository) {
        this.slotRepository = slotRepository;
        this.patientRepository = patientRepository;
        this.tokenRepository = tokenRepository;
    }

    // =========================
    // NORMAL BOOKING
    // =========================
    @Transactional
    public Token bookToken(Long slotId, Patient patient, Priority priority) {

        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (slot.getCurrentCapacity() >= slot.getMaxCapacity()) {
            throw new RuntimeException("Slot is full");
        }

        Patient savedPatient = patientRepository.save(patient);

        Token token = new Token();
        token.setPatient(savedPatient);
        token.setDoctor(slot.getDoctor());
        token.setSlot(slot);
        token.setPriority(priority);
        token.setStatus(TokenStatus.CREATED);
        token.setTokenNumber(slot.getCurrentCapacity() + 1);

        slot.setCurrentCapacity(slot.getCurrentCapacity() + 1);
        slotRepository.save(slot);

        return tokenRepository.save(token);
    }

    // =========================
    // EMERGENCY BOOKING (FIXED)
    // =========================
    @Transactional
    public Token emergencyBook(Long slotId, Patient patient) {

        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        List<Token> activeTokens =
                tokenRepository.findBySlotAndStatus(slot, TokenStatus.CREATED);

        // If slot is FULL → remove LOWEST priority token
        if (activeTokens.size() >= slot.getMaxCapacity()) {

            Token lowestPriorityToken = activeTokens.stream()
                    .max(Comparator.comparing(t -> t.getPriority().ordinal()))
                    .orElseThrow(() -> new RuntimeException("No token to replace"));

            lowestPriorityToken.setStatus(TokenStatus.CANCELLED);
            tokenRepository.save(lowestPriorityToken);

            slot.setCurrentCapacity(slot.getCurrentCapacity() - 1);
        }

        Patient savedPatient = patientRepository.save(patient);

        Token emergencyToken = new Token();
        emergencyToken.setPatient(savedPatient);
        emergencyToken.setDoctor(slot.getDoctor());
        emergencyToken.setSlot(slot);
        emergencyToken.setPriority(Priority.EMERGENCY);
        emergencyToken.setStatus(TokenStatus.CREATED);

        slot.setCurrentCapacity(slot.getCurrentCapacity() + 1);
        slotRepository.save(slot);

        tokenRepository.save(emergencyToken);

        // =========================
        // REASSIGN TOKEN NUMBERS
        // =========================
        List<Token> reordered =
                tokenRepository.findBySlotAndStatus(slot, TokenStatus.CREATED);

        reordered.sort(
                Comparator
                        .comparing((Token t) -> t.getPriority().ordinal())
                        .thenComparing(Token::getCreatedAt)
        );

        int tokenNo = 1;
        for (Token t : reordered) {
            t.setTokenNumber(tokenNo++);
            tokenRepository.save(t);
        }

        return emergencyToken;
    }

    // =========================
    // CANCEL TOKEN
    // =========================
    @Transactional
    public void cancelToken(Long tokenId) {

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if (token.getStatus() != TokenStatus.CREATED) {
            throw new RuntimeException("Token cannot be cancelled");
        }

        Slot slot = token.getSlot();
        token.setStatus(TokenStatus.CANCELLED);
        slot.setCurrentCapacity(slot.getCurrentCapacity() - 1);

        tokenRepository.save(token);
        slotRepository.save(slot);
    }

    // =========================
    // NO SHOW
    // =========================
    @Transactional
    public void markNoShow(Long tokenId) {

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if (token.getStatus() != TokenStatus.CREATED) {
            throw new RuntimeException("Invalid token state");
        }

        Slot slot = token.getSlot();
        token.setStatus(TokenStatus.NO_SHOW);
        slot.setCurrentCapacity(slot.getCurrentCapacity() - 1);

        tokenRepository.save(token);
        slotRepository.save(slot);
    }
}
