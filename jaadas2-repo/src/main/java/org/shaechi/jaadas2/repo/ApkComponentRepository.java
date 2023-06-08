package org.shaechi.jaadas2.repo;

import org.shaechi.jaadas2.entity.apk.ApkComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApkComponentRepository extends JpaRepository<ApkComponent, Long> {
}
