package com.cura.tests.appointment;

import com.cura.base.BaseTest;
import com.cura.models.Facility;
import com.cura.models.HealthcareProgram;
import com.cura.models.User;
import com.cura.pages.AppointmentPage;
import com.cura.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("CURA Healthcare")
@Feature("Appointment booking")
class AppointmentFormTest extends BaseTest {

    private AppointmentPage form;

    @BeforeEach
    void loginAndOpenForm() {
        form = new LoginPage(page).open().loginAs(User.demo());
        assertThat(form.heading()).hasText("Make Appointment");
    }

    @Test
    @Story("Form defaults")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Form is opened with the default values")
    @Description("Opens the form right after login and verifies the initial state of every field.")
    void form_hasDefaultValues() {
        assertThat(form.facilitySelect()).hasValue(Facility.TOKYO.label());
        assertThat(form.readmissionCheckbox()).not().isChecked();
        assertThat(form.programRadio(HealthcareProgram.MEDICARE)).isChecked();
        assertThat(form.visitDateInput()).isEmpty();
        assertThat(form.commentInput()).isEmpty();
    }

    @Test
    @Story("Facility selection")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Facility dropdown contains exactly the three facilities")
    @Description("Verifies the list of options in the facility dropdown.")
    void facilityDropdown_containsExactlyThreeFacilities() {
        String[] expected = Arrays.stream(Facility.values())
                .map(Facility::label)
                .toArray(String[]::new);

        assertThat(form.facilityOptions()).hasText(expected);
    }

    @ParameterizedTest(name = "[{index}] program: {0}")
    @EnumSource(HealthcareProgram.class)
    @Story("Program selection")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Healthcare program radio buttons are mutually exclusive")
    @Description("Selects a program and verifies that only this radio button is checked.")
    void programRadios_areMutuallyExclusive(HealthcareProgram selected) {
        form.selectProgram(selected);

        for (HealthcareProgram program : HealthcareProgram.values()) {
            if (program == selected) {
                assertThat(form.programRadio(program)).isChecked();
            } else {
                assertThat(form.programRadio(program)).not().isChecked();
            }
        }
    }
}