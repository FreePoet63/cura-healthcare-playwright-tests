package com.cura.pages;

import com.cura.components.SideMenu;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AppointmentPage extends BasePage {
    private final Locator heading = page.locator("section#appointment h2");
    private final SideMenu sideMenu;

    public AppointmentPage(Page page) {
        super(page);
        this.sideMenu = new SideMenu(page);
    }

    public Locator heading() {
        return heading;
    }

    public SideMenu sideMenu() {
        return sideMenu;
    }
}