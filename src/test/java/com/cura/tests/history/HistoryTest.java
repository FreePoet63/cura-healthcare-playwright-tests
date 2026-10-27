package com.cura.tests.history;

import com.cura.base.BaseTest;
import com.cura.components.HistoryEntry;
import com.cura.models.Appointment;
import com.cura.models.Facility;
import com.cura.models.HealthcareProgram;
import com.cura.models.User;
import com.cura.pages.AppointmentPage;
import com.cura.pages.HistoryPage;
import com.cura.pages.HomePage;
import com.cura.pages.LoginPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("CURA Healthcare")
@Feature("Appointment history")
public class HistoryTest extends BaseTest {
    private AppointmentPage loginAndOpenForm() {
        AppointmentPage form = new LoginPage(page).open().loginAs(User.demo());
        assertThat(form.heading()).hasText("Make Appointment");
        return form;
    }

    private static String uniqueComment() {
        return "History check " + UUID.randomUUID().toString().substring(0, 8);
    }

    @Test
    @Story("Empty history")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("History is empty for a new session without booking")
    @Description("Logs in without booking anything and opens the history. "
            + "Verifies that the 'No appointment.' message is shown.")
    void history_withoutBooking_showsNoAppointments() {
        HistoryPage history = loginAndOpenForm().sideMenu().openHistory();

        assertThat(history.heading()).hasText("History");
        assertThat(history.emptyMessage()).isVisible();
    }

    @Test
    @Story("Booked appointment in history")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Booked appointment appears in the history with the entered data")
    @Description("Books an appointment with all fields filled and opens the history via the side menu. "
            + "Verifies that the record shows the visit date, facility, readmission, program and comment.")
    void bookedAppointment_appearsInHistory_withEnteredData() {
        Appointment appointment = Appointment.full().withComment(uniqueComment());

        HistoryPage history = loginAndOpenForm()
                .bookAppointment(appointment)
                .sideMenu()
                .openHistory();

        assertThat(history.heading()).hasText("History");
        HistoryEntry entry = history.entryWithComment(appointment.comment());
        assertThat(entry.visitDate()).containsText(appointment.visitDate());
        assertThat(entry.facility()).hasText(appointment.facility().label());
        assertThat(entry.hospitalReadmission()).hasText("Yes");
        assertThat(entry.program()).hasText(appointment.program().label());
        assertThat(entry.comment()).hasText(appointment.comment());
    }

    @Test
    @Story("Several appointments in history")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Two booked appointments are both shown in the history")
    @Description("Books two appointments with different facilities and program. "
            + "Verifies that both records are present in the history with their own data.")
    void severalAppointments_appearInHistory() {
        Appointment first = Appointment.full()
                .withFacility(Facility.TOKYO)
                .withComment(uniqueComment());
        Appointment second = Appointment.full()
                .withFacility(Facility.HONGKONG)
                .withProgram(HealthcareProgram.NONE)
                .withComment(uniqueComment());

        AppointmentPage form = loginAndOpenForm();
        form.bookAppointment(first);
        HistoryPage history = form.open()
                .bookAppointment(second)
                .sideMenu()
                .openHistory();

        HistoryEntry firstEntry = history.entryWithComment(first.comment());
        assertThat(firstEntry.facility()).hasText(Facility.TOKYO.label());
        assertThat(firstEntry.program()).hasText(first.program().label());
        HistoryEntry secondEntry = history.entryWithComment(second.comment());
        assertThat(secondEntry.facility()).hasText(Facility.HONGKONG.label());
        assertThat(secondEntry.program()).hasText(HealthcareProgram.NONE.label());
    }

    @Test
    @Story("Access control")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("History is not available without login")
    @Description("Opens the history page by direct URL as an unauthenticated user. "
            + "Verifies that the user is redirected to the home page and no history is shown.")
    void history_withoutLogin_redirectsToLogin() {
        HistoryPage history = new HistoryPage(page).open();

        assertThat(page).not().hasURL(Pattern.compile(".*history\\.php.*"));
        assertThat(new HomePage(page).makeAppointmentButton()).isVisible();
        assertThat(history.heading()).not().isVisible();
    }
}