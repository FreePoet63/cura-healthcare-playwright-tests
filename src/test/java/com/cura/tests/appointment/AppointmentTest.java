package com.cura.tests.appointment;

import com.cura.base.BaseTest;
import com.cura.models.Appointment;
import com.cura.models.Facility;
import com.cura.models.HealthcareProgram;
import com.cura.models.User;
import com.cura.pages.AppointmentPage;
import com.cura.pages.ConfirmationPage;
import com.cura.pages.HomePage;
import com.cura.pages.LoginPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("CURA Healthcare")
@Feature("Appointment booking")
public class AppointmentTest extends BaseTest {
    private AppointmentPage appointmentPage;

    @BeforeEach
    void loginAndOpenAppointmentForm() {
        appointmentPage = new LoginPage(page).open().loginAs(User.demo());
        assertThat(appointmentPage.heading()).hasText("Make Appointment");
    }

    @Test
    @Story("Successful booking")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Appointment with all fields filled is shown on the confirmation page")
    @Description("Fills every field of the form with non-default values, books the appointment "
            + "and verifies that confirmation page show exactly the entered data.")
    void bookAppointment_withAllFields_showsEnteredData() {
        Appointment appointment = Appointment.full();

        ConfirmationPage confirmation = appointmentPage.bookAppointment(appointment);

        assertConfirmation(confirmation, appointment);
    }

    @Test
    @Story("Successful booking")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Only the visit date is required: other fields keep default values")
    @Description("Enters only the visit date and books. Verifies that the form defaults are used: "
            + "first facility, no readmission, Medicare and an empty comment.")
    void bookAppointment_withRequiredFieldOnly_usesDefaults() {
        String date = Appointment.futureDate(3);

        ConfirmationPage confirmation = appointmentPage.enterVisitDate(date).book();

        assertThat(confirmation.heading()).hasText("Appointment Confirmation");
        assertThat(confirmation.facility()).hasText(Facility.TOKYO.label());
        assertThat(confirmation.hospitalReadmission()).hasText("No");
        assertThat(confirmation.program()).hasText(HealthcareProgram.MEDICARE.label());
        assertThat(confirmation.visitDate()).hasText(date);
        assertThat(confirmation.comment()).isEmpty();
    }

    @ParameterizedTest(name = "[{index}] facility: {0}")
    @EnumSource(Facility.class)
    @Story("Facility selection")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Each facility can be booked")
    @Description("Books an appointment for every facility from the dropdown and verifies "
            + "that the chosen facility is shown on the confirmation page.")
    void bookAppointment_forEachFacility(Facility facility) {
        Appointment appointment = Appointment.full().withFacility(facility);

        ConfirmationPage confirmation = appointmentPage.bookAppointment(appointment);

        assertThat(confirmation.facility()).hasText(facility.label());
    }

    @ParameterizedTest(name = "[{index}] program: {0}")
    @EnumSource(HealthcareProgram.class)
    @Story("Program selection")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Each Healthcare program can be booked")
    @Description("Books an appointment for every healthcare program (Medicare, Medicaid, None) "
            + "and verifies that the chosen program is shown on the confirmation page.")
    void bookAppointment_forEachFacility(HealthcareProgram program) {
        Appointment appointment = Appointment.full().withProgram(program);

        ConfirmationPage confirmation = appointmentPage.bookAppointment(appointment);

        assertThat(confirmation.program()).hasText(program.label());
    }

    @Test
    @Story("Form validation")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Appointment without a visit date is not submitted")
    @Description("Clicks 'Book Appointment' with an empty visit date. Verifies that the user "
            + "stays on the form and the date field is marked as invalid.")
    void bookAppointment_withoutVisitDate_isNotSubmitted() {
        appointmentPage.bookExpectingValidationError();

        assertThat(page).hasURL(Pattern.compile(".*#appointment"));
        assertThat(appointmentPage.invalidVisitDate()).isVisible();
    }

    @Test
    @Story("Navigation after booking")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("'Go to Homepage' on the confirmation page leads to the home page")
    @Description("Books an appointment, clicks 'Go to Homepage' and verifies that the home page is shown.")
    void confirmationPage_goToHomepage_opensHome() {
        HomePage home = appointmentPage.bookAppointment(Appointment.full()).goToHomepage();

        assertThat(home.makeAppointmentButton()).isVisible();
    }

    private void assertConfirmation(ConfirmationPage confirmation, Appointment appointment) {
        assertThat(confirmation.heading()).hasText("Appointment Confirmation");
        assertThat(confirmation.facility()).hasText(appointment.facility().label());
        assertThat(confirmation.hospitalReadmission())
                .hasText(appointment.hospitalReadmission() ? "Yes" : "No");
        assertThat(confirmation.program()).hasText(appointment.program().label());
        assertThat(confirmation.visitDate()).hasText(appointment.visitDate());
        assertThat(confirmation.comment()).hasText(appointment.comment());
    }
}