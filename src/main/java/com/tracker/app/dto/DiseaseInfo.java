package com.tracker.app.dto;

import com.tracker.app.model.Disease;

public record DiseaseInfo(String code, String displayName, String dataFreshnessLabel, String sourceLabel) {

    public static DiseaseInfo from(Disease disease) {
        return new DiseaseInfo(disease.getCode(), disease.getDisplayName(),
                disease.getDataFreshnessLabel(), disease.getSourceLabel());
    }
}
