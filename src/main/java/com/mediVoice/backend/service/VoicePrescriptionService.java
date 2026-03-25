package com.mediVoice.backend.service;

import com.mediVoice.backend.dto.PrescriptionItemRequest;
import com.mediVoice.backend.dto.PrescriptionRequest;
import com.mediVoice.backend.dto.VoicePrescriptionRequest;
import com.mediVoice.backend.dto.PrescriptionResponse;
import com.mediVoice.backend.dto.VoiceNoteRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VoicePrescriptionService {

    private final VoiceNoteService voiceNoteService;
    private final PrescriptionService prescriptionService;

    public VoicePrescriptionService(VoiceNoteService voiceNoteService, PrescriptionService prescriptionService) {
        this.voiceNoteService = voiceNoteService;
        this.prescriptionService = prescriptionService;
    }

    public PrescriptionResponse createFromVoice(VoicePrescriptionRequest request) {
        // store raw transcript as a voice note
        VoiceNoteRequest noteRequest = new VoiceNoteRequest();
        noteRequest.setPatientId(request.getPatientId());
        noteRequest.setDoctorId(request.getDoctorId());
        noteRequest.setAppointmentId(request.getAppointmentId());
        noteRequest.setTranscript(request.getTranscript());
        voiceNoteService.create(noteRequest);

        // Build a prescription request by extracting structured data
        PrescriptionRequest prescriptionRequest = new PrescriptionRequest();
        prescriptionRequest.setPatientId(request.getPatientId());
        prescriptionRequest.setDoctorId(request.getDoctorId());
        prescriptionRequest.setPrescriptionDate(LocalDate.now());
        prescriptionRequest.setNotes(prettifyText(request.getTranscript()));
        prescriptionRequest.setItems(extractPrescriptionItems(request.getTranscript()));

        return prescriptionService.create(prescriptionRequest);
    }

    private String prettifyText(String transcript) {
        // Basic grammar fixes: add a period to the end if missing and capitalize first letter of each sentence
        String trimmed = transcript.trim();
        if (trimmed.isEmpty()) {
            return trimmed;
        }
        String[] sentences = trimmed.split("[\\.\\?\\!]+\\s*");
        StringBuilder sb = new StringBuilder();
        for (String sentence : sentences) {
            sentence = sentence.trim();
            if (sentence.isEmpty()) continue;
            String fixed = sentence.substring(0, 1).toUpperCase() + (sentence.length() > 1 ? sentence.substring(1) : "");
            if (!fixed.endsWith(".")) {
                fixed += ".";
            }
            sb.append(fixed).append(" ");
        }
        return sb.toString().trim();
    }

    private List<PrescriptionItemRequest> extractPrescriptionItems(String transcript) {
        List<PrescriptionItemRequest> items = new ArrayList<>();
        if (transcript == null || transcript.isBlank()) {
            return items;
        }

        // Very simple extraction: look for patterns like "<medicine> <dosage>" (e.g., paracetamol 500mg, ibuprofen 200 mg)
        Pattern pattern = Pattern.compile("([A-Za-z0-9 ]+?)\\s+((?:\\d+\\s?mg)|(?:\\d+mg)|(?:\\d+\\s?mcg)|(?:\\d+\\s?ml)|(?:\\d+\\s?units))", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(transcript);
        while (matcher.find()) {
            String medicine = matcher.group(1).trim();
            String dosage = matcher.group(2).trim();
            // Build a simple item
            PrescriptionItemRequest item = new PrescriptionItemRequest();
            item.setMedicineName(medicine);
            item.setDosage(dosage);
            item.setInstructions("Take as directed.");
            items.add(item);
        }

        // If no items detected, add a fallback item with the whole transcript as notes
        if (items.isEmpty()) {
            PrescriptionItemRequest fallback = new PrescriptionItemRequest();
            fallback.setMedicineName("General advice");
            fallback.setDosage("As needed");
            fallback.setInstructions(transcript.trim());
            items.add(fallback);
        }

        return items;
    }
}
