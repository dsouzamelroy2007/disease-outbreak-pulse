package com.tracker.app.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tracker.app.model.Disease;
import com.tracker.app.model.ReportingLocationStatistics;
import com.tracker.app.service.provider.DiseaseDataProvider;

@Service
public class DiseaseDataService {

    private static final Logger log = LoggerFactory.getLogger(DiseaseDataService.class);

    private final Map<Disease, DiseaseDataProvider> providersByDisease = new EnumMap<>(Disease.class);
    private final Map<Disease, List<ReportingLocationStatistics>> statsCache = new ConcurrentHashMap<>();

    public DiseaseDataService(List<DiseaseDataProvider> providers) {
        for (DiseaseDataProvider provider : providers) {
            providersByDisease.put(provider.supports(), provider);
        }
    }

    @PostConstruct
    @Scheduled(fixedRate = 4, timeUnit = java.util.concurrent.TimeUnit.HOURS)
    public void refreshAll() {
        providersByDisease.values().parallelStream().forEach(provider -> {
            try {
                List<ReportingLocationStatistics> stats = provider.fetch();
                Collections.sort(stats);
                statsCache.put(provider.supports(), stats);
            } catch (Exception e) {
                log.warn("Failed to refresh data for {}: {}", provider.supports(), e.getMessage());
            }
        });
    }

    public List<ReportingLocationStatistics> getStats(Disease disease) {
        return statsCache.getOrDefault(disease, List.of());
    }

    public List<Disease> getSupportedDiseases() {
        return new ArrayList<>(providersByDisease.keySet());
    }
}
