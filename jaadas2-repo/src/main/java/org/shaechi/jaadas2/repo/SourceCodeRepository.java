package org.shaechi.jaadas2.repo;

import org.shaechi.jaadas2.entity.ScanJob;
import org.shaechi.jaadas2.entity.SourceCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceCodeRepository extends JpaRepository<SourceCode, Long> {
    List<SourceCode> findByScanJobAndLocation(ScanJob scanJob, String location);
}
