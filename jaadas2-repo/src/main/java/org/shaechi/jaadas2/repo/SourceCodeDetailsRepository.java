package org.shaechi.jaadas2.repo;

import org.shaechi.jaadas2.entity.SourceCode;
import org.shaechi.jaadas2.entity.SourceCodeDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SourceCodeDetailsRepository extends JpaRepository<SourceCodeDetails, Long> {
    List<SourceCodeDetails> findBySourceCodeAndMethodName(SourceCode sourceCode, String methodName);
}
