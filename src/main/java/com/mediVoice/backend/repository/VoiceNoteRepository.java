package com.mediVoice.backend.repository;

import com.mediVoice.backend.model.VoiceNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoiceNoteRepository extends JpaRepository<VoiceNote, Long> {

    List<VoiceNote> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    List<VoiceNote> findByAppointmentId(Long appointmentId);
}
