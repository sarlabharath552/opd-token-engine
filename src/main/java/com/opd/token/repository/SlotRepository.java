package com.opd.token.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opd.token.domain.entity.Slot;
import com.opd.token.domain.entity.Doctor;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    List<Slot> findByDoctor(Doctor doctor);
}
