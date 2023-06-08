package org.shaechi.jaadas2.repo;

import org.shaechi.jaadas2.entity.JobStatus;
import org.shaechi.jaadas2.entity.ScanJob;
import org.shaechi.jaadas2.entity.apk.ApkInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScanJobRepository extends JpaRepository<ScanJob, Long> {
    ScanJob findByUuid(UUID uuid);
}
