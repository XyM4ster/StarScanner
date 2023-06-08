package org.shaechi.jaadas2.services;

import org.shaechi.jaadas2.entity.JobStatus;
import org.shaechi.jaadas2.entity.ScanJob;
import org.shaechi.jaadas2.entity.result.ScanResult;
import org.shaechi.jaadas2.entity.vuln.VulnLevel;
import org.shaechi.jaadas2.repo.ScanJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ScanJobServiceImpl implements ScanJobService{
    final
    ScanJobRepository repo;

    public ScanJobServiceImpl(ScanJobRepository repo) {
        this.repo = repo;
    }

    @Override
    public void updateJobStateIfNotSuccess(JobStatus status, UUID uuid) {
        ScanJob job = repo.findByUuid(uuid);
        if (job.getStatus() != JobStatus.SuccessFinish)
            job.setStatus(status);
        this.repo.save(job);
    }


    /**
     * @param uuid
     * @param allReachable true if we want all score, false if we only want reachable (e.g. exported)
     *                     results
     * @return
     */
    @Override
    public int calcScore(UUID uuid, boolean allReachable) {
        ScanJob job = this.repo.findByUuid(uuid);
        List<ScanResult> resultList = job.getResults();

        int score = 0;
        for(ScanResult result: resultList) {
            if (result.getComponent().isExported() || allReachable)
                score += translateScore(result.getVulnLevel());
        }
        return score;
    }

    @Override
    public ScanJob getByJobId(Long id) {
        return this.repo.getById(id);
    }

    @Override
    public void updateJobScanStartTime(UUID uuid) {
        ScanJob job = this.repo.findByUuid(uuid);
        job.setScanStartTime(LocalDateTime.now());
        this.repo.save(job);
    }

    private int translateScore(VulnLevel level) {
        switch(level) {
            case HIGH:
                return 20;
            case MEDIUM:
                return 5;
            case LOW:
                return 1;
            case INFO:
                return 0;
        }
        return 0;
    }
}
