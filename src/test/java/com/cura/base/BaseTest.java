package com.cura.base;

import com.cura.config.TestConfig;
import com.cura.core.PlaywrightManager;
import com.cura.extensions.TestFailureExtension;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.fields.AllIndexesSelector;

import java.lang.reflect.Method;

@ExtendWith(TestFailureExtension.class)
public abstract class BaseTest {
    protected Page page;

    @BeforeEach
    void setUp() {
        page = PlaywrightManager.start();
        Allure.parameter("browser", TestConfig.browser());
    }

    @AfterEach
    void tearDown(TestInfo info) {
        PlaywrightManager.stop(info.getTestMethod().map(Method::getName).orElse("test"));
    }
}