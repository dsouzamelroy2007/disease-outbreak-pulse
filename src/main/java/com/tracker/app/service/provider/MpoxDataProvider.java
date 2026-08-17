package com.tracker.app.service.provider;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import com.tracker.app.model.Disease;
import com.tracker.app.model.ReportingLocationStatistics;

@Component
public class MpoxDataProvider implements DiseaseDataProvider {

    private static final String DATA_URL = "https://raw.githubusercontent.com/owid/monkeypox/main/owid-monkeypox-data.csv";

    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public Disease supports() {
        return Disease.MPOX;
    }

    @Override
    public List<ReportingLocationStatistics> fetch() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(DATA_URL)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new StringReader(response.body()));

        Map<String, ReportingLocationStatistics> latestByLocation = new LinkedHashMap<>();
        Map<String, LocalDate> latestDateByLocation = new LinkedHashMap<>();

        for (CSVRecord record : records) {
            String isoCode = record.get("iso_code");
            if (isoCode == null || isoCode.isBlank() || isoCode.startsWith("OWID_")) {
                continue;
            }
            String location = record.get("location");
            LocalDate date = LocalDate.parse(record.get("date"));
            LocalDate previousLatest = latestDateByLocation.get(location);
            if (previousLatest != null && !date.isAfter(previousLatest)) {
                continue;
            }

            ReportingLocationStatistics stat = new ReportingLocationStatistics();
            stat.setDisease(Disease.MPOX);
            stat.setCountry(location);
            stat.setLatestTotalCases((long) parseDoubleOrZero(record.get("total_cases")));
            stat.setDiffFromPrevPeriod((int) parseDoubleOrZero(record.get("new_cases")));
            stat.setAsOfDate(date.toString());

            latestByLocation.put(location, stat);
            latestDateByLocation.put(location, date);
        }

        return new ArrayList<>(latestByLocation.values());
    }

    private double parseDoubleOrZero(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }
        return Double.parseDouble(value);
    }
}
