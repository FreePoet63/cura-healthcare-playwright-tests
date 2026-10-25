package com.cura.pages;

import com.cura.config.TestConfig;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class HomePage extends BasePage {
    private final Locator title = page.locator("h1");
    private final Locator makeAppointmentButton = page.locator("#btn-make-appointment");

    public HomePage(Page page) {
        super(page);
    }

    @Step("Open home page")
    public HomePage open() {
        page.navigate(TestConfig.baseUrl());
        return this;
    }

    @Step("Click 'Make Appointment")
    public LoginPage clickMakeAppointment() {
        makeAppointmentButton.click();
        return new LoginPage(page);
    }

    public Locator title() {
        return title;
    }

    public Locator makeAppointmentButton() {
        return makeAppointmentButton;
    }
}