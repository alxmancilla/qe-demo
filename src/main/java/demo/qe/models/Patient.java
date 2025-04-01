package demo.qe.models;

import java.util.Date;

import org.bson.types.ObjectId;

// start-patient-model
public class Patient {
    public ObjectId id;
    public String patientName;
    public Date dateOfBirth;

    public PatientRecord patientRecord;

    public Patient() {
    }

    public Patient(String name, Date dateOfBirth, PatientRecord patientRecord) {
        this.patientName = name;
        this.dateOfBirth = dateOfBirth;
        this.patientRecord = patientRecord;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String name) {
        this.patientName = name;
    }

    public PatientRecord getPatientRecord() {
        return patientRecord;
    }

    public void setPatientRecord(PatientRecord patientRecord) {
        this.patientRecord = patientRecord;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + patientName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", patientRecord=" + patientRecord +
                '}';
    }
}
// end-patient-model