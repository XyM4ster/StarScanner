package org.shaechi.jaadas2.repo;

import org.shaechi.jaadas2.entity.ComponentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentResultRepository extends JpaRepository<ComponentResult, Long> {
}
