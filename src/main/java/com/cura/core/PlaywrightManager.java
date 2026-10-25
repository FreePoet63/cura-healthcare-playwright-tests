package com.cura.core;

import com.cura.config.TestConfig;
import com.microsoft.playwright.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class PlaywrightManager {
    public static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    public static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    public static final ThreadLocal<BrowserContext> CONTEXT = new ThreadLocal<>();
    public static final ThreadLocal<Page> PAGE = new ThreadLocal<>();
    public static final ThreadLocal<Boolean> FAILED = ThreadLocal.withInitial(() -> false);

    private PlaywrightManager() {}

    public static Page start() {
        Playwright playwright = Playwright.create();
        BrowserType type = switch (TestConfig.browser().toLowerCase()) {
            case "firefox" -> playwright.firefox();
            case "webkit" -> playwright.webkit();
            default -> playwright.chromium();
        };
        Browser browser = type.launch(new BrowserType.LaunchOptions()
                .setHeadless(TestConfig.headless())
                .setSlowMo(TestConfig.slowMo()));

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1280, 720));
        context.setDefaultTimeout(TestConfig.timeoutMs());
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        Page page = context.newPage();

        PLAYWRIGHT.set(playwright);
        BROWSER.set(browser);
        CONTEXT.set(context);
        PAGE.set(page);
        FAILED.set(false);
        return page;
    }

    public static Page page() {
        return PAGE.get();
    }

    public static void markFailed() {
        FAILED.set(true);
    }

    public static void stop(String testName) {
        try {
            BrowserContext context = CONTEXT.get();
            if (context != null) {
                if (FAILED.get()) {
                    Path trace = Paths.get("target", "traces", testName + "-" + System.currentTimeMillis() + ".zip");
                    context.tracing().stop(new Tracing.StopOptions().setPath(trace));
                } else {
                    context.tracing().stop();
                }
                context.close();
            }
            if (BROWSER.get() != null) {
                BROWSER.get().close();
            }
            if (PLAYWRIGHT.get() != null) {
                PLAYWRIGHT.get().close();
            }
        } finally {
            PAGE.remove();
            CONTEXT.remove();
            BROWSER.remove();
            PLAYWRIGHT.remove();
            FAILED.remove();
        }
    }
}