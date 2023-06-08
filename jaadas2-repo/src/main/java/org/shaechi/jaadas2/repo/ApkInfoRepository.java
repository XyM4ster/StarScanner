package org.shaechi.jaadas2.repo;

import org.shaechi.jaadas2.entity.apk.ApkComponent;
import org.shaechi.jaadas2.entity.apk.ApkInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApkInfoRepository extends JpaRepository<ApkInfo, Long> {
    ApkInfo findByComponentsId(Long id);
}
