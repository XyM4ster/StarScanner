package org.shaechi.jaadas2.services;

import org.shaechi.jaadas2.entity.engine.EngineReport;

import java.util.UUID;

public interface ProcessEngineReportService {
    void processEngineReport(UUID jobUUID, EngineReport report);
}
