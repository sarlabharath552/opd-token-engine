package com.opd.token.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opd.token.domain.entity.Doctor;
import com.opd.token.domain.entity.Slot;
import com.opd.token.domain.enums.SlotStatus;
import com.opd.token.repository.DoctorRepository;
import com.opd.token.repository.SlotRepository;

@Service
public class SlotService {

    private final SlotRepository slotRepository;
    private final DoctorRepository doctorRepository;

    public SlotService(SlotRepository slotRepository,
                       DoctorRepository doctorRepository) {
        this.slotRepository = slotRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public Slot createSlot(Long doctorId,
                           LocalTime start,
                           LocalTime end,
                           int capacity) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Slot slot = new Slot();
        slot.setDoctor(doctor);
        slot.setStartTime(start);
        slot.setEndTime(end);
        slot.setMaxCapacity(capacity);
        slot.setStatus(SlotStatus.OPEN);

        return slotRepository.save(slot);
    }

    public List<Slot> getDoctorSlots(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return slotRepository.findByDoctor(doctor);
    }
}
