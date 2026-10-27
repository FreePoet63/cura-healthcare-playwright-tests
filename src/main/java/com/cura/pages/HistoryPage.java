package com.cura.pages;

import com.cura.components.HistoryEntry;
import com.cura.config.TestConfig;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class HistoryPage extends BasePage {
    private final Locator heading = page.locator("section#history h2");
    private final Locator entries = page.locator("section#history .panel");
    private final Locator emptyMessage = page.getByText("No appointment.");

    public HistoryPage(Page page) {
        super(page);
    }

    @Step("Open history page by direct URL")
    public HistoryPage open() {
        page.navigate(TestConfig.baseUrl() + "/history.php#history");
        return this;
    }

    public HistoryEntry entryWithComment(String comment) {
        return new HistoryEntry(entries.filter(new Locator.FilterOptions().setHasText(comment)));
    }

    public Locator heading() {
        return heading;
    }

    public Locator entries() {
        return entries;
    }

    public Locator emptyMessage() {
        return emptyMessage;
    }
}