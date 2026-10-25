package com.cura.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public final class TestConfig {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = TestConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read config.properties", e);
        }
    }

    private TestConfig() {}

    private static String get(String key) {
        return System.getProperty(key, PROPS.getProperty(key));
    }

    public static String baseUrl() {
        return get("baseUrl");
    }

    public static String browser() {
        return get("browser");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static double slowMo() {
        return Double.parseDouble(get("slowMo"));
    }

    public static double timeoutMs() {
        return Double.parseDouble(get("timeoutMs"));
    }

    public static String username() {
        return get("username");
    }

    public static String password() {
        return get("password");
    }
}