package com.cura.tests.login;

import com.cura.base.BaseTest;
import com.cura.models.User;
import com.cura.pages.AppointmentPage;
import com.cura.pages.HomePage;
import com.cura.pages.LoginPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("CURA Healthcare")
@Feature("Authentication")
public class LoginTest extends BaseTest {
    private static final String ERROR_MESSAGE =
            "Login failed! Please ensure the username and password are valid.";

    @Test
    @Story("Successful login")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Valid user is redirected to the appointment form")
    @Description("Logs in with the demo account and verifies that the user lands on "
            + "the 'Make Appointment' form. Without a working login no other feature is reachable.")
    void validCredentials_openAppointmentForm() {
        AppointmentPage appointment = new LoginPage(page).open().loginAs(User.demo());

        assertThat(page).hasURL(Pattern.compile(".*#appointment"));
        assertThat(appointment.heading()).hasText("Make Appointment");
    }

    @Test
    @Story("Login from home page")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("'Make Appointment' button leads to the login form")
    @Description("Clicks 'Make Appointment' on the home page as an unauthenticated user "
            + "and verifies that the login form is shown.")
    void makeAppointmentButton_leadsToLogin() {
        LoginPage loginPage = new HomePage(page).open().clickMakeAppointment();

        assertThat(page).hasURL(Pattern.compile(".*profile\\.php#login"));
        assertThat(loginPage.loginButton()).isVisible();
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("invalidAttempts")
    @Story("Invalid login")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Invalid credentials show an error and keep the user on login")
    @Description("Submits wrong username, wrong password, both wrong and both empty. "
            + "Verifies that an error message is shown and access is not granted.")
    void invalidCredentials_showError(LoginAttempt attempt) {
        LoginPage loginPage = new LoginPage(page).open()
                .submit(attempt.username(), attempt.password());

        assertThat(loginPage.errorMessage()).hasText(ERROR_MESSAGE);
        assertThat(page).hasURL(Pattern.compile(".*profile\\.php#login"));
    }

    @Test
    @Story("Logout")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("User returns to the home pageafter logout")
    @Description("Logs in, opens the side menu, clicks 'Logout' and verifies "
            + "that the home page with 'Make Appointment' is shown.")
    void logout_returnsToHomePage() {
        HomePage home = new LoginPage(page).open()
                .loginAs(User.demo())
                .sideMenu()
                .logout();

        assertThat(home.makeAppointmentButton()).isVisible();
    }

    record LoginAttempt(String title, String username, String password) {
        @Override
        public String toString() {
            return title;
        }
    }

    static Stream<LoginAttempt> invalidAttempts() {
        return Stream.of(
                new LoginAttempt("Valid username, wrong password", "John Doe", "wrongPassword"),
                new LoginAttempt("Unknown username, valid password", "unknownUser", User.demo().password()),
                new LoginAttempt("Unknown username, wrong password", "unknownUser", "wrongPassword"),
                new LoginAttempt("Empty username and password", "", "")
        );
    }
}