package com.cura.base;

import com.cura.config.TestConfig;
import com.cura.core.PlaywrightManager;
import com.cura.extensions.TestFailureExtension;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Label;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Method;

@ExtendWith(TestFailureExtension.class)
public abstract class BaseTest {
    protected Page page;

    @BeforeEach
    void setUp() {
        page = PlaywrightManager.start();
        tagWithBrowser(TestConfig.browser());
    }

    @AfterEach
    void tearDown(TestInfo info) {
        PlaywrightManager.stop(info.getTestMethod().map(Method::getName).orElse("test"));
    }

    private static void tagWithBrowser(String browser) {
        Allure.parameter("browser", browser);
        Allure.getLifecycle().updateTestCase(result -> {
            String base = result.getHistoryId() != null
                    ? result.getHistoryId() :
                    String.valueOf(result.getFullName());
            result.setHistoryId(base + "-" + browser);
            result.setName(result.getName() + " [" + browser + "]");
            result.getLabels().add(new Label().setName("parentSuite").setValue(browser));
        });
    }
}