package org.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findByOperatorIdAndSlotAndCreatedDateAndStatus(
            Long operatorId, Integer slot, LocalDate createdDate, AppointmentStatus status
    );
    List<Appointment> findBySlotAndCreatedDateAndStatus(Integer slot, LocalDate createdDate, AppointmentStatus status);
    List<Appointment> findByOperatorIdAndCreatedDateAndStatus(Long operatorId, LocalDate createdDate, AppointmentStatus status);
    List<Appointment> findByCreatedDateAndStatus(LocalDate createdDate, AppointmentStatus status);
}
