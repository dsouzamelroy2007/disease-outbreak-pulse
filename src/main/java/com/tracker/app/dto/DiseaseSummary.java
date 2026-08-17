package com.tracker.app.dto;

public record DiseaseSummary(String code, String displayName, long totalCases, long totalNew,
                              String dataFreshnessLabel, String sourceLabel, int locationsReporting) {
}
