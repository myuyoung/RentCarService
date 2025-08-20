package me.changwook.repository;

import me.changwook.domain.CarRegistrationSubmission;
import me.changwook.domain.SubmissionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarRegistrationSubmissionRepository extends JpaRepository<CarRegistrationSubmission, UUID> {


    Page<CarRegistrationSubmission> findByStatus(SubmissionStatus status, Pageable pageable);
}


