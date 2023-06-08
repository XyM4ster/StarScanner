package org.shaechi.jaadas2.services;

import org.shaechi.jaadas2.entity.JobStatus;
import org.shaechi.jaadas2.entity.ScanJob;
import org.shaechi.jaadas2.entity.result.ScanResult;

import java.util.List;
import java.util.UUID;

public interface ScanJobService {
    void updateJobStateIfNotSuccess(JobStatus status, UUID uuid);
    int calcScore(UUID uuid, boolean allReachable);
    ScanJob getByJobId(Long id);
    void updateJobScanStartTime(UUID uuid);
}
