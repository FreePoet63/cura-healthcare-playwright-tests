package com.cura.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class ConfirmationPage extends BasePage {
    private final Locator heading = page.locator("section#summary h2");
    private final Locator facility = page.locator("#facility");
    private final Locator hospitalReadmission = page.locator("#hospital_readmission");
    private final Locator program = page.locator("#program");
    private final Locator visitDate = page.locator("#visit_date");
    private final Locator comment = page.locator("#comment");
    private final Locator goToHomepageButton = page.locator(".btn.btn-default");

    public ConfirmationPage(Page page) {
        super(page);
    }

    @Step("Click 'Go to Homepage'")
    public HomePage goToHomepage() {
        goToHomepageButton.click();
        return new HomePage(page);
    }

    public Locator heading() {
        return heading;
    }

    public Locator facility() {
        return facility;
    }

    public Locator hospitalReadmission() {
        return hospitalReadmission;
    }

    public Locator program() {
        return program;
    }

    public Locator visitDate() {
        return visitDate;
    }

    public Locator comment() {
        return comment;
    }
}