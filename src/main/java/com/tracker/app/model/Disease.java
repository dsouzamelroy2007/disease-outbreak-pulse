package com.tracker.app.model;

public enum Disease {
    COVID_19("COVID-19", 
        "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv",
        "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Recovered.csv"),
    MPOX("MPOX", 
        "https://raw.githubusercontent.com/owid/datasets/master/datasets/Monkeypox%20cases%20by%20country%20(Johns%20Hopkins)/Monkeypox%20cases%20by%20country%20(Johns%20Hopkins).csv",
        "https://raw.githubusercontent.com/owid/datasets/master/datasets/Monkeypox%20cases%20by%20country%20(Johns%20Hopkins)/Monkeypox%20cases%20by%20country%20(Johns%20Hopkins).csv"),
    INFLUENZA("Influenza", 
        "https://raw.githubusercontent.com/reichlab/covid19-forecast-hub/master/data-truth/truth-Incident%20Cases.csv",
        "https://raw.githubusercontent.com/reichlab/covid19-forecast-hub/master/data-truth/truth-Incident%20Cases.csv"),
    DENGUE("Dengue", 
        "https://raw.githubusercontent.com/ecohealthalliance/PREDICT_Dashboard_data/main/dengue_data.csv",
        "https://raw.githubusercontent.com/ecohealthalliance/PREDICT_Dashboard_data/main/dengue_data.csv");

    private final String displayName;
    private final String confirmedCasesUrl;
    private final String recoveredCasesUrl;

    Disease(String displayName, String confirmedCasesUrl, String recoveredCasesUrl) {
        this.displayName = displayName;
        this.confirmedCasesUrl = confirmedCasesUrl;
        this.recoveredCasesUrl = recoveredCasesUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getConfirmedCasesUrl() {
        return confirmedCasesUrl;
    }

    public String getRecoveredCasesUrl() {
        return recoveredCasesUrl;
    }

    public static Disease fromString(String disease) {
        try {
            return Disease.valueOf(disease.toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException e) {
            return COVID_19;
        }
    }
}
