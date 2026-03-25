package com.mediVoice.backend.service;

import com.mediVoice.backend.dto.AppointmentRequest;
import com.mediVoice.backend.dto.AppointmentResponse;
import com.mediVoice.backend.exception.ResourceNotFoundException;
import com.mediVoice.backend.model.Appointment;
import com.mediVoice.backend.model.Patient;
import com.mediVoice.backend.model.User;
import com.mediVoice.backend.repository.AppointmentRepository;
import com.mediVoice.backend.repository.PatientRepository;
import com.mediVoice.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AppointmentResponse create(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId()).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        User doctor = userRepository.findById(request.getDoctorId()).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        Appointment a = new Appointment();
        a.setPatient(patient);
        a.setDoctor(doctor);
        a.setAppointmentDate(request.getAppointmentDate());
        a.setNotes(request.getNotes());
        a.setStatus(Appointment.STATUS_SCHEDULED);
        return toResponse(appointmentRepository.save(a));
    }

    @Transactional
    public AppointmentResponse updateStatus(Long id, String status) {
        Appointment a = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
        a.setStatus(status);
        return toResponse(appointmentRepository.save(a));
    }

    @Transactional
    public void cancel(Long id) {
        updateStatus(id, Appointment.STATUS_CANCELLED);
    }

    public AppointmentResponse getById(Long id) {
        Appointment a = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
        return toResponse(a);
    }

    public List<AppointmentResponse> getByPatientId(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentDateDesc(patientId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<AppointmentResponse> getByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorIdOrderByAppointmentDateDesc(doctorId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<AppointmentResponse> getDoctorSchedule(Long doctorId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByDoctorIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(doctorId, start, end).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAll() {
        return appointmentRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    private AppointmentResponse toResponse(Appointment a) {
        AppointmentResponse r = new AppointmentResponse();
        r.setId(a.getId());
        r.setPatientId(a.getPatient().getId());
        r.setPatientName(a.getPatient().getFirstName() + " " + a.getPatient().getLastName());
        r.setDoctorId(a.getDoctor().getId());
        r.setDoctorName(a.getDoctor().getUsername());
        r.setAppointmentDate(a.getAppointmentDate());
        r.setStatus(a.getStatus());
        r.setNotes(a.getNotes());
        r.setCreatedAt(a.getCreatedAt());
        return r;
    }
}
