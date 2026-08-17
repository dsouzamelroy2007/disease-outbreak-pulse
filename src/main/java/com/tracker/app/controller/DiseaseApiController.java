package com.tracker.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.app.dto.DiseaseInfo;
import com.tracker.app.dto.DiseaseSummary;
import com.tracker.app.model.Disease;
import com.tracker.app.model.ReportingLocationStatistics;
import com.tracker.app.service.DiseaseDataService;

@RestController
public class DiseaseApiController {

    @Autowired
    DiseaseDataService diseaseDataService;

    @GetMapping("/api/diseases")
    public List<DiseaseInfo> diseases() {
        return diseaseDataService.getSupportedDiseases().stream()
                .map(DiseaseInfo::from)
                .toList();
    }

    @GetMapping("/api/diseases/{code}/stats")
    public List<ReportingLocationStatistics> stats(@PathVariable String code) {
        return diseaseDataService.getStats(Disease.fromCode(code));
    }

    @GetMapping("/api/diseases/{code}/summary")
    public DiseaseSummary summary(@PathVariable String code) {
        Disease disease = Disease.fromCode(code);
        List<ReportingLocationStatistics> stats = diseaseDataService.getStats(disease);

        long totalCases = stats.stream().mapToLong(ReportingLocationStatistics::getLatestTotalCases).sum();
        long totalNew = stats.stream()
                .mapToInt(stat -> stat.getDiffFromPrevPeriod() == null ? 0 : stat.getDiffFromPrevPeriod())
                .sum();

        return new DiseaseSummary(disease.getCode(), disease.getDisplayName(), totalCases, totalNew,
                disease.getDataFreshnessLabel(), disease.getSourceLabel(), stats.size());
    }
}
