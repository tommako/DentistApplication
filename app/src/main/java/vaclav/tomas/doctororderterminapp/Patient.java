package vaclav.tomas.doctororderterminapp;

public class Patient {

    private String id;
    private String patientName;
    private String patientSurname;
    private String emailForConfirm;
    private String terminDate;
    private String terminTime;
    private String stavTerminu;

    public Patient (String id, String patientName, String patientSurname, String emailForConfirm, String terminDate, String terminTime, String stavTerminu){
        this.id = id;
        this.patientName = patientName;
        this.patientSurname = patientSurname;
        this.emailForConfirm = emailForConfirm;
        this.terminDate = terminDate;
        this.terminTime = terminTime;
        this.stavTerminu = stavTerminu;
    }

    public Patient(String patientName, String patientSurname, String terminDate, String terminTime, String stavTerminu){
        this.patientName = patientName;
        this.patientSurname = patientSurname;
        this.terminDate = terminDate;
        this.terminTime = terminTime;
        this.stavTerminu = stavTerminu;
    }

    public Patient (){

    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }

    public String getTerminDate() {
        return terminDate;
    }

    public void setTerminDate(String terminDate) {
        this.terminDate = terminDate;
    }

    public String getTerminTime() {
        return terminTime;
    }

    public void setTerminTime(String terminTime) {
        this.terminTime = terminTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStavTerminu() {
        return stavTerminu;
    }

    public void setStavTerminu(String stavTerminu) {
        this.stavTerminu = stavTerminu;
    }

    public String getEmailForConfirm() { return emailForConfirm; }

    public void setEmailForConfirm(String emailForConfirm) { this.emailForConfirm = emailForConfirm; }
}
