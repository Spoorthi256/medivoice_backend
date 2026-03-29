# Medivoice Backend – REST API Reference

Base URL: `http://localhost:8080/api`

## Authentication

- **POST /api/register** (public)  
  Body: `{ "username", "email", "password", "role": "admin"|"doctor"|"receptionist" }`  
  Returns: `UserResponse`

- **POST /api/login** (public)  
  Body: `{ "email", "password" }`  
  Returns: `{ "token": "<JWT>", "user": UserResponse }`

- **POST /api/auth/register** (public) – same as `/api/register`  
- **POST /api/auth/login** (public) – same as `/api/login`

Protected endpoints require header: `Authorization: Bearer <token>`.

---

## Users (admin only for list)

- **GET /api/users**  
  Returns: `List<UserResponse>`

---

## Patients

- **POST /api/patients** – create patient  
  Body: `PatientRequest` (firstName, lastName, dateOfBirth, phone?, email?, address?)  
  Roles: ADMIN, DOCTOR, RECEPTIONIST

- **PUT /api/patients/{id}** – update patient  
  Body: `PatientRequest`

- **GET /api/patients/{id}** – get by id  
- **GET /api/patients** – list all  
- **GET /api/patients/search?q=** – search by name, email, phone  

---

## Appointments

- **POST /api/appointments** – book appointment  
  Body: `{ "patientId", "doctorId", "appointmentDate", "notes?" }`  
  Status set to SCHEDULED

- **PATCH /api/appointments/{id}/status?status=** – set status  
  Status: SCHEDULED | CANCELLED | COMPLETED | NO_SHOW

- **POST /api/appointments/{id}/cancel** – cancel (sets status to CANCELLED)

- **GET /api/appointments/{id}**  
- **GET /api/appointments** – all  
- **GET /api/appointments/patient/{patientId}**  
- **GET /api/appointments/doctor/{doctorId}**  
- **GET /api/appointments/doctor/{doctorId}/schedule?start=&end=** – ISO date-time  

---

## Voice notes (speech-to-text storage)

- **POST /api/voice-notes** – create  
  Body: `{ "patientId", "doctorId", "appointmentId?", "transcript", "audioUrl?" }`  
  Roles: ADMIN, DOCTOR

- **GET /api/voice-notes/{id}**  
- **GET /api/voice-notes/patient/{patientId}**  

---

## Prescriptions

- **POST /api/prescriptions** – create  
  Body: `{ "patientId", "doctorId", "prescriptionDate", "notes?", "items": [ { "medicineName", "dosage", "instructions?" } ] }`  
  Roles: ADMIN, DOCTOR

- **GET /api/prescriptions/{id}**  
- **GET /api/prescriptions/patient/{patientId}**  

---

## Medical history

- **POST /api/medical-history** – add record  
  Body: `{ "patientId", "diagnosis", "diagnosisDate", "notes?", "prescriptionId?" }`  
  Roles: ADMIN, DOCTOR

- **GET /api/medical-history/{id}**  
- **GET /api/medical-history/patient/{patientId}**  

---

## Symptom Analysis

- **POST /api/symptom-analysis/analyze** – analyze symptoms  
  Body: `{ "patientId?", "queryType": "symptom_analysis", "queryText", "responseText?", "severityLevel?" }`  
  Returns: `QueryHistoryResponse`  
  Roles: ADMIN, DOCTOR

- **GET /api/symptom-analysis/history** – get user's query history  
  Returns: `List<QueryHistoryResponse>`  
  Roles: ADMIN, DOCTOR

- **GET /api/symptom-analysis/patient/{patientId}/history** – get patient's query history  
  Returns: `List<QueryHistoryResponse>`  
  Roles: ADMIN, DOCTOR

---

## Health Tips

- **GET /api/health-tips** – get general health tips  
  Returns: `List<String>` (static tips for now)  
  - **doctor**: myAppointmentsToday, totalMyAppointments  
  - **receptionist**: appointmentsToday, totalPatients  

---

## Security

- JWT in `Authorization: Bearer <token>`.
- Roles: ROLE_ADMIN, ROLE_DOCTOR, ROLE_RECEPTIONIST.
- CORS allowed origin: `http://localhost:3000`.
- Passwords hashed with BCrypt.
