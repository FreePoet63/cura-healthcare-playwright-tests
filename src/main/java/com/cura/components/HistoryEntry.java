package com.cura.components;

import com.microsoft.playwright.Locator;

public class HistoryEntry {
    private final Locator root;

    public HistoryEntry(Locator root) {
        this.root = root;
    }

    public Locator visitDate() {
        return root.locator(".panel-heading");
    }

    public Locator facility() {
        return root.locator("#facility");
    }

    public Locator hospitalReadmission() {
        return root.locator("#hospital_readmission");
    }

    public Locator program() {
        return root.locator("#program");
    }

    public Locator comment() {
        return root.locator("#comment");
    }
}