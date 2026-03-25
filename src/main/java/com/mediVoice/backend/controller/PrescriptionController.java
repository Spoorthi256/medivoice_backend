package com.mediVoice.backend.controller;

import com.mediVoice.backend.dto.PrescriptionRequest;
import com.mediVoice.backend.dto.PrescriptionResponse;
import com.mediVoice.backend.dto.VoicePrescriptionRequest;
import com.mediVoice.backend.service.PrescriptionService;
import com.mediVoice.backend.service.VoicePrescriptionService;
import jakarta.validation.Valid;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final VoicePrescriptionService voicePrescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService, VoicePrescriptionService voicePrescriptionService) {
        this.prescriptionService = prescriptionService;
        this.voicePrescriptionService = voicePrescriptionService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<PrescriptionResponse> create(@Valid @RequestBody PrescriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prescriptionService.create(request));
    }

    @PostMapping("/from-voice")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<PrescriptionResponse> createFromVoice(@Valid @RequestBody VoicePrescriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(voicePrescriptionService.createFromVoice(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<PrescriptionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getById(id));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<List<PrescriptionResponse>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionService.getByPatientId(patientId));
    }

    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) throws IOException {
        PrescriptionResponse p = prescriptionService.getById(id);
        byte[] pdfBytes = generatePdf(p);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "prescription-" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    private byte[] generatePdf(PrescriptionResponse p) throws IOException {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.newLineAtOffset(50, 700);
                content.showText("Medivoice Prescription");
                content.endText();

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 11);
                content.newLineAtOffset(50, 670);
                content.showText("Prescription ID: " + p.getId());
                content.newLineAtOffset(0, -14);
                content.showText("Patient ID: " + p.getPatientId());
                content.newLineAtOffset(0, -14);
                content.showText("Doctor ID: " + p.getDoctorId());
                content.newLineAtOffset(0, -14);
                content.showText("Date: " + p.getPrescriptionDate().format(DateTimeFormatter.ISO_DATE));
                content.endText();

                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 12);
                content.newLineAtOffset(50, 600);
                content.showText("Medications:");
                content.endText();

                float y = 580;
                for (int i = 0; i < p.getItems().size(); i++) {
                    var item = p.getItems().get(i);
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA, 11);
                    content.newLineAtOffset(60, y);
                    content.showText((i + 1) + ". " + item.getMedicineName() + " - " + item.getDosage());
                    content.newLineAtOffset(0, -14);
                    content.showText("   " + (item.getInstructions() != null ? item.getInstructions() : ""));
                    content.endText();
                    y -= 34;
                }

                content.beginText();
                content.setFont(PDType1Font.HELVETICA_OBLIQUE, 11);
                content.newLineAtOffset(50, y - 20);
                content.showText("Notes: " + (p.getNotes() != null ? p.getNotes() : ""));
                content.endText();
            }

            document.save(out);
            return out.toByteArray();
        }
    }
}
