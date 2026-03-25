package com.mediVoice.backend.controller;

import com.mediVoice.backend.dto.DashboardResponse;
import com.mediVoice.backend.model.User;
import com.mediVoice.backend.repository.AppointmentRepository;
import com.mediVoice.backend.repository.PatientRepository;
import com.mediVoice.backend.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public DashboardController(UserRepository userRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DashboardResponse> getDashboard(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new IllegalStateException("User not found"));
        DashboardResponse response = new DashboardResponse();
        response.setRole(user.getRole());
        Map<String, Object> data = new HashMap<>();
        switch (user.getRole().toLowerCase()) {
            case "admin":
                data.put("totalPatients", patientRepository.count());
                data.put("totalAppointments", appointmentRepository.count());
                data.put("totalUsers", userRepository.count());
                data.put("totalDoctors", userRepository.countByRole("doctor"));
                data.put("totalReceptionists", userRepository.countByRole("receptionist"));
                break;
            case "doctor":
                LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
                LocalDateTime endOfDay = startOfDay.plusDays(1);
                data.put("myAppointmentsToday", appointmentRepository.findByDoctorIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(user.getId(), startOfDay, endOfDay).size());
                data.put("totalMyAppointments", appointmentRepository.findByDoctorIdOrderByAppointmentDateDesc(user.getId()).size());
                break;
            case "receptionist":
                LocalDateTime start = LocalDateTime.now().toLocalDate().atStartOfDay();
                LocalDateTime end = start.plusDays(1);
                data.put("appointmentsToday", appointmentRepository.findAll().stream().filter(a -> a.getAppointmentDate().isAfter(start) && a.getAppointmentDate().isBefore(end)).count());
                data.put("totalPatients", patientRepository.count());
                break;
            default:
                break;
        }
        response.setData(data);
        return ResponseEntity.ok(response);
    }
}
