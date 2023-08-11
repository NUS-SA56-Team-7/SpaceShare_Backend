package com.spaceshare.backend.services;

import java.util.List;

import com.spaceshare.backend.models.ScamReport;

public interface ScamReportService {
    List<ScamReport> getAllScamReports();

	Boolean createScamReport(Long propertyId);

    Boolean approveScamReport(Long id);

    Boolean declineScamReport(Long id);

	Boolean deleteScamReport(Long id);

    ScamReport getScamReportsStatus();
}
