package com.cura.pages;

import com.cura.config.TestConfig;
import com.cura.models.User;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.qameta.allure.model.Parameter;

public class LoginPage extends BasePage {
    private final Locator usernameInput = page.locator("#txt-username");
    private final Locator passwordInput = page.locator("#txt-password");
    private final Locator loginButton = page.locator("#btn-login");
    private final Locator errorMessage = page.locator("p.text-danger");

    public LoginPage(Page page) {
        super(page);
    }

    @Step("Open login page")
    public LoginPage open() {
        page.navigate(TestConfig.baseUrl() + "/profile.php#login");
        usernameInput.waitFor();
        return this;
    }

    @Step("Log in with the given user credentials")
    public AppointmentPage loginAs(User user) {
        submit(user.username(), user.password());
        return new AppointmentPage(page);
    }

    @Step("Submit credentials for username '{username}'")
    public LoginPage submit(String username, @Param(mode = Parameter.Mode.MASKED) String password) {
        usernameInput.fill(username);
        passwordInput.fill(password);
        loginButton.click();
        return this;
    }

    public Locator errorMessage() {
        return errorMessage;
    }

    public Locator loginButton() {
        return loginButton;
    }
}