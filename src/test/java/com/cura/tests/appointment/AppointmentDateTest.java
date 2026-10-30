package com.cura.tests.appointment;

import com.cura.base.BaseTest;
import com.cura.models.Appointment;
import com.cura.models.User;
import com.cura.pages.AppointmentPage;
import com.cura.pages.ConfirmationPage;
import com.cura.pages.LoginPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("CURA Healthcare")
@Feature("Appointment booking")
public class AppointmentDateTest extends BaseTest {
    record DateCase(String title, String value) {
        @Override
        public String toString() {
            return title;
        }
    }

    static Stream<DateCase> invalidDates() {
        return Stream.of(
                new DateCase("Letters instead of a date", "abcdefgh"),
                new DateCase("Non-existent day (31/02)", "31/02/2026"),
                new DateCase("Zeros", "00/00/0000"),
                new DateCase("Wrong format (yyyy-mm-dd)", "2026-12-31"));
    }

    private AppointmentPage form;

    @BeforeEach
    void loginAndOpenForm() {
        form = new LoginPage(page).open().loginAs(User.demo());
        assertThat(form.heading()).hasText("Make Appointment");
    }

    @Test
    @Story("Visit date boundaries")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Today's date is accepted")
    @Description("Books an appointment for today and verifies the date on the confirmation page.")
    void visitDate_today_isAccepted() {
        String today = Appointment.futureDate(0);

        ConfirmationPage confirmation = form.enterVisitDate(today).book();

        assertThat(confirmation.heading()).hasText("Appointment Confirmation");
        assertThat(confirmation.visitDate()).hasText(today);
    }

    @Test
    @Story("Visit date boundaries")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("A date far in the future (10 years) is accepted")
    @Description("Books an appointment 10 years ahead and verifies the date on the confirmation page.")
    void visitDate_farFuture_isAccepted() {
        String date = Appointment.futureDate(3650);

        ConfirmationPage confirmation = form.enterVisitDate(date).book();

        assertThat(confirmation.heading()).hasText("Appointment Confirmation");
        assertThat(confirmation.visitDate()).hasText(date);
    }

    @Disabled("BUG #17: invalid date is converted to another date and the appointment is created")
    @Issue("17")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("invalidDates")
    @Story("Visit date boundaries")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Invalid visit date is not submitted")
    @Description("Enters an invalid value into the visit date field and clicks 'Book Appointment'. "
            + "Verifies that the form is not submitted and the date field is marked as invalid.")
    void visitDate_invalidValue_isNotSubmitted(DateCase dateCase) {
        form.enterVisitDate(dateCase.value()).bookExpectingValidationError();

        assertThat(form.invalidVisitDate()).isVisible();
    }

    @Disabled("BUG #18: appointment can be booked for a date in the past")
    @Issue("18")
    @Test
    @Story("Visit date boundaries")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Visit date in the past is not accepted")
    @Description("Books an appointment with a date 30 days in the past. a healthcare appointment "
            + "cannot be booked retroactively, so the confirmation page must not be shown.")
    void visitDate_inThePast_isRejected() {
        ConfirmationPage confirmation = form.enterVisitDate(Appointment.postDate(30)).book();

        assertThat(confirmation.heading()).not().isVisible();
    }
}