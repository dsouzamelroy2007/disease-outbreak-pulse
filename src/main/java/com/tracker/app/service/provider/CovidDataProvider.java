package com.tracker.app.service.provider;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.app.model.Disease;
import com.tracker.app.model.ReportingLocationStatistics;

@Component
public class CovidDataProvider implements DiseaseDataProvider {

    private static final String COUNTRIES_URL = "https://disease.sh/v3/covid-19/countries";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Disease supports() {
        return Disease.COVID_19;
    }

    @Override
    public List<ReportingLocationStatistics> fetch() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(COUNTRIES_URL)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode countries = objectMapper.readTree(response.body());
        List<ReportingLocationStatistics> stats = new ArrayList<>();
        for (JsonNode country : countries) {
            ReportingLocationStatistics stat = new ReportingLocationStatistics();
            stat.setDisease(Disease.COVID_19);
            stat.setCountry(country.path("country").asText());
            stat.setLatestTotalCases(country.path("cases").asLong());
            stat.setDiffFromPrevPeriod(country.path("todayCases").asInt());
            long updatedMillis = country.path("updated").asLong();
            if (updatedMillis > 0) {
                stat.setAsOfDate(DateTimeFormatter.ISO_LOCAL_DATE
                        .withZone(ZoneOffset.UTC)
                        .format(Instant.ofEpochMilli(updatedMillis)));
            }
            stats.add(stat);
        }
        return stats;
    }
}
