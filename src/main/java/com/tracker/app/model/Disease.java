package com.tracker.app.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Disease {
    COVID_19("covid-19", "COVID-19", "Live", "disease.sh (worldometers-derived)"),
    MPOX("mpox", "Mpox", "As of Dec 2024", "Our World in Data"),
    INFLUENZA("influenza", "Influenza", "Live", "WHO FluNet"),
    DENGUE("dengue", "Dengue (Brazil)", "Live", "InfoDengue (Fiocruz)");

    private final String code;
    private final String displayName;
    private final String dataFreshnessLabel;
    private final String sourceLabel;

    Disease(String code, String displayName, String dataFreshnessLabel, String sourceLabel) {
        this.code = code;
        this.displayName = displayName;
        this.dataFreshnessLabel = dataFreshnessLabel;
        this.sourceLabel = sourceLabel;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDataFreshnessLabel() {
        return dataFreshnessLabel;
    }

    public String getSourceLabel() {
        return sourceLabel;
    }

    public static Disease fromCode(String code) {
        for (Disease disease : values()) {
            if (disease.code.equalsIgnoreCase(code)) {
                return disease;
            }
        }
        throw new IllegalArgumentException("Unknown disease code: " + code);
    }
}
