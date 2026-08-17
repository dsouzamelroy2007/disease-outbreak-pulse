package com.tracker.app.service.provider;

import java.util.List;

import com.tracker.app.model.Disease;
import com.tracker.app.model.ReportingLocationStatistics;

public interface DiseaseDataProvider {

    Disease supports();

    List<ReportingLocationStatistics> fetch() throws Exception;
}
