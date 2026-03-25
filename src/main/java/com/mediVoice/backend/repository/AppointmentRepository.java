package com.mediVoice.backend.repository;

import com.mediVoice.backend.model.Appointment;
import com.mediVoice.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(Long patientId);

    List<Appointment> findByDoctorIdOrderByAppointmentDateDesc(Long doctorId);

    List<Appointment> findByDoctorIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(Long doctorId, LocalDateTime start, LocalDateTime end);
}
