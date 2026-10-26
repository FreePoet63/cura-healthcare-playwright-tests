package com.cura.pages;

import com.cura.components.SideMenu;
import com.cura.models.Appointment;
import com.cura.models.Facility;
import com.cura.models.HealthcareProgram;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class AppointmentPage extends BasePage {
    private final Locator heading = page.locator("section#appointment h2");
    private final Locator facilitySelect = page.locator("#combo_facility");
    private final Locator readmissionCheckbox = page.locator("#chk_hospotal_readmission");
    private final Locator visitDateInput = page.locator("#txt_visit_date");
    private final Locator commentInput = page.locator("#txt_comment");
    private final Locator bookButton = page.locator("#btn-book-appointment");
    private final SideMenu sideMenu;

    public AppointmentPage(Page page) {
        super(page);
        this.sideMenu = new SideMenu(page);
    }

    @Step("Select facility: {facility}")
    public AppointmentPage selectFacility(Facility facility) {
        facilitySelect.selectOption(facility.label());
        return this;
    }

    @Step("Set hospital readmission: {checked}")
    public AppointmentPage setHospitalReadmission(boolean checked) {
        readmissionCheckbox.setChecked(checked);
        return this;
    }

    @Step("Select healthcare program: {program}")
    public AppointmentPage selectProgram(HealthcareProgram program) {
        page.locator(program.selector()).check();
        return this;
    }

    @Step("Enter visit date: {date}")
    public AppointmentPage enterVisitDate(String date) {
        visitDateInput.click();
        visitDateInput.pressSequentially(date);
        visitDateInput.press("Escape");
        return this;
    }

    @Step("Enter comment: {comment}")
    public AppointmentPage enterCommit(String comment) {
        commentInput.fill(comment);
        return this;
    }

    @Step("Click 'Book Appointment'")
    public ConfirmationPage book() {
        bookButton.click();
        return new ConfirmationPage(page);
    }

    @Step("Click 'Book Appointment' without a visit date")
    public AppointmentPage bookExpectingValidationError() {
        bookButton.click();
        return this;
    }

    @Step("Fill the whole form and book the appointment")
    public ConfirmationPage bookAppointment(Appointment appointment) {
        selectFacility(appointment.facility());
        setHospitalReadmission(appointment.hospitalReadmission());
        selectProgram(appointment.program());
        enterVisitDate(appointment.visitDate());
        enterCommit(appointment.comment());
        return book();
    }

    public Locator heading() {
        return heading;
    }

    public Locator invalidVisitDate() {
        return page.locator("#txt_visit_date:invalid");
    }

    public SideMenu sideMenu() {
        return sideMenu;
    }
}