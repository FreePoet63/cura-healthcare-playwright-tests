package com.cura.components;

import com.cura.pages.HomePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class SideMenu {
    private final Page page;
    private final Locator toggle;
    private final Locator openedSidebar;

    public SideMenu(Page page) {
        this.page = page;
        this.toggle = page.locator("#menu-toggle");
        this.openedSidebar = page.locator("#sidebar-wrapper.active");
    }

    @Step("Open side menu")
    public SideMenu open() {
        if (openedSidebar.count() == 0) {
            toggle.click();
        }
        openedSidebar.waitFor();
        return this;
    }

    @Step("Log out via side menu")
    public HomePage logout() {
        open();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Logout")).click();
        return new HomePage(page);
    }
}