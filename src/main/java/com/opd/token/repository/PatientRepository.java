package com.opd.token.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.opd.token.domain.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
