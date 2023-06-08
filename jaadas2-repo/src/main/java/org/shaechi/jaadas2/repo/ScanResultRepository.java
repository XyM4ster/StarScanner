package org.shaechi.jaadas2.repo;

import org.shaechi.jaadas2.entity.result.ScanResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScanResultRepository extends JpaRepository<ScanResult, Long> {
}
