package com.opd.token.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opd.token.domain.entity.Slot;
import com.opd.token.domain.entity.Token;
import com.opd.token.domain.enums.TokenStatus;

public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findBySlotAndStatus(Slot slot, TokenStatus status);
}
