package com.cura.models;

public enum Facility {
    TOKYO("Tokyo CURA Healthcare Center"),
    HONGKONG("Hongkong CURA Healthcare Center"),
    SEOUL("Seoul CURA Healthcare Center");

    private final String label;

    Facility(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }


    @Override
    public String toString() {
        return label;
    }
}
