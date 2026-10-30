package com.cura.tests.appointment;

import com.cura.base.BaseTest;
import com.cura.models.Appointment;
import com.cura.models.User;
import com.cura.pages.AppointmentPage;
import com.cura.pages.ConfirmationPage;
import com.cura.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("CURA Healthcare")
@Feature("Appointment booking")
class AppointmentCommentTest extends BaseTest {

    record CommentCase(String title, String comment) {
        @Override
        public String toString() {
            return title;
        }
    }

    static Stream<CommentCase> comments() {
        return Stream.of(
                new CommentCase("Long text (1000 chars)", "A".repeat(1000)),
                new CommentCase("Special characters", "!@#$%^&*()_+-=[]{};':\",.<>/?\\|"),
                new CommentCase("HTML and script tags", "<b>bold</b><script>alert('xss')</script>"),
                new CommentCase("Cyrillic text", "Повторный визит к врачу"),
                new CommentCase("Emoji", "Visit 😀 follow-up"));
    }

    private AppointmentPage form;

    @BeforeEach
    void loginAndOpenForm() {
        form = new LoginPage(page).open().loginAs(User.demo());
        assertThat(form.heading()).hasText("Make Appointment");
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("comments")
    @Story("Comment content")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Comment is shown on the confirmation page exactly as entered")
    @Description("Books an appointment with a boundary or special comment and verifies that the "
            + "confirmation page shows the same text. For HTML it also checks that the input is "
            + "escaped and shown as plain text.")
    void comment_isShownOnConfirmationAsEntered(CommentCase commentCase) {
        ConfirmationPage confirmation = form
                .enterVisitDate(Appointment.futureDate(5))
                .enterComment(commentCase.comment())
                .book();

        assertThat(confirmation.comment()).hasText(commentCase.comment());
    }
}