package com.opd.token.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.opd.token.domain.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
