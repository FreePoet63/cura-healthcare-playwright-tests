package com.cura.models;

import com.cura.config.TestConfig;

public record User(String username, String password) {
    public static User demo() {
        return new User(TestConfig.username(), TestConfig.password());
    }

    @Override
    public String toString() {
        return "User[username=" + username + ", password=*****]";
    }
}