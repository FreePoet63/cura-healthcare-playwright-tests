package com.cura.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record Appointment (
        Facility facility,
        boolean hospitalReadmission,
        HealthcareProgram program,
        String visitDate,
        String comment) {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String futureDate(int daysAhead) {
        return LocalDate.now().plusDays(daysAhead).format(DATE_FORMAT);
    }

    public static Appointment full() {
        return new Appointment(
                Facility.SEOUL,
                true,
                HealthcareProgram.MEDICAID,
                futureDate(7),
                "Follow-up visit after surgery");
    }

    public Appointment withFacility(Facility newFacility) {
        return new Appointment(newFacility, hospitalReadmission, program, visitDate, comment);
    }

    public Appointment withProgram(HealthcareProgram newProgram) {
        return new Appointment(facility, hospitalReadmission, newProgram, visitDate, comment);
    }

    public Appointment withComment(String newComment) {
        return new Appointment(facility, hospitalReadmission, program, visitDate, newComment);
    }
}