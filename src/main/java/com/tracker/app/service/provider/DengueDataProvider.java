package com.tracker.app.service.provider;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.app.model.Disease;
import com.tracker.app.model.ReportingLocationStatistics;

/**
 * There is no free global dengue API, so we track a curated list of Brazilian state
 * capitals via Fiocruz's InfoDengue surveillance API (weekly reported/estimated cases
 * per city geocode).
 */
@Component
public class DengueDataProvider implements DiseaseDataProvider {

    private static final String API_TEMPLATE =
            "https://info.dengue.mat.br/api/alertcity?geocode=%d&disease=dengue&format=json&ew_start=%d&ew_end=%d&ey_start=%d&ey_end=%d";

    private static final Map<String, Integer> STATE_CAPITAL_GEOCODES = new LinkedHashMap<>();

    static {
        STATE_CAPITAL_GEOCODES.put("Rio de Janeiro", 3304557);
        STATE_CAPITAL_GEOCODES.put("Sao Paulo", 3550308);
        STATE_CAPITAL_GEOCODES.put("Belo Horizonte", 3106200);
        STATE_CAPITAL_GEOCODES.put("Salvador", 2927408);
        STATE_CAPITAL_GEOCODES.put("Fortaleza", 2304400);
        STATE_CAPITAL_GEOCODES.put("Recife", 2611606);
        STATE_CAPITAL_GEOCODES.put("Porto Alegre", 4314902);
        STATE_CAPITAL_GEOCODES.put("Curitiba", 4106902);
        STATE_CAPITAL_GEOCODES.put("Brasilia", 5300108);
        STATE_CAPITAL_GEOCODES.put("Manaus", 1302603);
        STATE_CAPITAL_GEOCODES.put("Belem", 1501402);
        STATE_CAPITAL_GEOCODES.put("Goiania", 5208707);
        STATE_CAPITAL_GEOCODES.put("Campo Grande", 5002704);
        STATE_CAPITAL_GEOCODES.put("Cuiaba", 5103403);
        STATE_CAPITAL_GEOCODES.put("Natal", 2408102);
        STATE_CAPITAL_GEOCODES.put("Joao Pessoa", 2507507);
        STATE_CAPITAL_GEOCODES.put("Maceio", 2704302);
        STATE_CAPITAL_GEOCODES.put("Aracaju", 2800308);
        STATE_CAPITAL_GEOCODES.put("Teresina", 2211001);
        STATE_CAPITAL_GEOCODES.put("Sao Luis", 2111300);
        STATE_CAPITAL_GEOCODES.put("Palmas", 1721000);
        STATE_CAPITAL_GEOCODES.put("Porto Velho", 1100205);
        STATE_CAPITAL_GEOCODES.put("Rio Branco", 1200401);
        STATE_CAPITAL_GEOCODES.put("Boa Vista", 1400100);
        STATE_CAPITAL_GEOCODES.put("Macapa", 1600303);
        STATE_CAPITAL_GEOCODES.put("Florianopolis", 4205407);
        STATE_CAPITAL_GEOCODES.put("Vitoria", 3205309);
    }

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Disease supports() {
        return Disease.DENGUE;
    }

    @Override
    public List<ReportingLocationStatistics> fetch() throws Exception {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int week = now.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());
        int startWeek = Math.max(1, week - 8);

        List<ReportingLocationStatistics> stats = new ArrayList<>();
        for (Map.Entry<String, Integer> capital : STATE_CAPITAL_GEOCODES.entrySet()) {
            String url = String.format(API_TEMPLATE, capital.getValue(), startWeek, week, year, year);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode weeks = objectMapper.readTree(response.body());
            if (!weeks.isArray() || weeks.isEmpty()) {
                continue;
            }
            JsonNode latest = weeks.get(0);
            JsonNode previous = weeks.size() > 1 ? weeks.get(1) : null;

            ReportingLocationStatistics stat = new ReportingLocationStatistics();
            stat.setDisease(Disease.DENGUE);
            stat.setCountry("Brazil");
            stat.setState(capital.getKey());
            stat.setLatestTotalCases(latest.path("casos").asLong());
            if (previous != null) {
                stat.setDiffFromPrevPeriod((int) (latest.path("casos").asLong() - previous.path("casos").asLong()));
            }
            stat.setAsOfDate(java.time.Instant.ofEpochMilli(latest.path("data_iniSE").asLong()).toString().substring(0, 10));
            stats.add(stat);
        }
        return stats;
    }
}
