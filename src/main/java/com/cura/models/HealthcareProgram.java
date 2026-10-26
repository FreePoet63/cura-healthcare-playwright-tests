package com.cura.models;

public enum HealthcareProgram {
    MEDICARE("Medicare", "#radio_program_medicare"),
    MEDICAID("Medicaid", "#radio_program_medicaid"),
    NONE("None", "#radio_program_none");

    private final String label;
    private final String selector;

    HealthcareProgram(String label, String selector) {
        this.label = label;
        this.selector = selector;
    }

    public String label() {
        return label;
    }

    public String selector() {
        return selector;
    }

    @Override
    public String toString() {
        return label;
    }
}