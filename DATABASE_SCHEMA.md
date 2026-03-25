# Medivoice – Database Schema (MySQL)

## Tables

### users (existing)
| Column     | Type         | Constraints        |
|------------|--------------|--------------------|
| id         | BIGINT       | PK, AUTO_INCREMENT |
| username   | VARCHAR(100) | NOT NULL           |
| email      | VARCHAR(255) | NOT NULL, UNIQUE   |
| password   | VARCHAR(255) | NOT NULL (BCrypt)  |
| role       | VARCHAR(20)  | NOT NULL (admin, doctor, receptionist) |

### patients
| Column       | Type         | Constraints        |
|--------------|--------------|--------------------|
| id           | BIGINT       | PK, AUTO_INCREMENT |
| first_name   | VARCHAR(100) | NOT NULL           |
| last_name    | VARCHAR(100) | NOT NULL           |
| date_of_birth| DATE         | NOT NULL           |
| phone        | VARCHAR(20)  |                    |
| email        | VARCHAR(255) |                    |
| address      | VARCHAR(500) |                    |
| created_by_id| BIGINT       | FK -> users(id)    |
| created_at   | TIMESTAMP    | NOT NULL           |
| updated_at   | TIMESTAMP    |                    |

### appointments
| Column        | Type         | Constraints        |
|---------------|--------------|--------------------|
| id            | BIGINT       | PK, AUTO_INCREMENT |
| patient_id    | BIGINT       | FK -> patients(id), NOT NULL |
| doctor_id     | BIGINT       | FK -> users(id), NOT NULL    |
| appointment_date | DATETIME  | NOT NULL           |
| status        | VARCHAR(20)  | NOT NULL (SCHEDULED, CANCELLED, COMPLETED, NO_SHOW) |
| notes         | TEXT         |                    |
| created_at    | TIMESTAMP    | NOT NULL           |
| updated_at    | TIMESTAMP    |                    |

### voice_notes
| Column          | Type         | Constraints        |
|-----------------|--------------|--------------------|
| id              | BIGINT       | PK, AUTO_INCREMENT |
| patient_id      | BIGINT       | FK -> patients(id), NOT NULL |
| doctor_id       | BIGINT       | FK -> users(id), NOT NULL    |
| appointment_id  | BIGINT       | FK -> appointments(id)       |
| transcript      | TEXT         | NOT NULL (speech-to-text)    |
| audio_url       | VARCHAR(500) | optional stored file URL      |
| created_at      | TIMESTAMP    | NOT NULL           |

### prescriptions
| Column        | Type         | Constraints        |
|---------------|--------------|--------------------|
| id            | BIGINT       | PK, AUTO_INCREMENT |
| patient_id    | BIGINT       | FK -> patients(id), NOT NULL |
| doctor_id     | BIGINT       | FK -> users(id), NOT NULL    |
| prescription_date | DATE     | NOT NULL           |
| notes         | TEXT         |                    |
| created_at    | TIMESTAMP    | NOT NULL           |

### prescription_items
| Column         | Type         | Constraints        |
|----------------|--------------|--------------------|
| id             | BIGINT       | PK, AUTO_INCREMENT |
| prescription_id| BIGINT       | FK -> prescriptions(id), NOT NULL |
| medicine_name  | VARCHAR(200) | NOT NULL          |
| dosage         | VARCHAR(100) | NOT NULL          |
| instructions   | VARCHAR(500) |                   |

### medical_history
| Column          | Type         | Constraints        |
|-----------------|--------------|--------------------|
| id              | BIGINT       | PK, AUTO_INCREMENT |
| patient_id      | BIGINT       | FK -> patients(id), NOT NULL |
| diagnosis       | VARCHAR(300) | NOT NULL          |
| diagnosis_date  | DATE         | NOT NULL           |
| notes           | TEXT         |                    |
| prescription_id | BIGINT       | FK -> prescriptions(id)      |
| created_at      | TIMESTAMP    | NOT NULL           |
