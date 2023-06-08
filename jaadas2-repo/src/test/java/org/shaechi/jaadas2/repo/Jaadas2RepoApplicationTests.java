package org.shaechi.jaadas2.repo;

import org.junit.jupiter.api.Test;
import org.shaechi.jaadas2.entity.result.ScanResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.shaechi"})
@EntityScan(basePackages = {"org.shaechi"})
@SpringBootTest
class Jaadas2RepoApplicationTests {

    @Autowired
    ScanResultRepository scanResultRepository;
    @Test
    void contextLoads() {

        ScanResult result = new ScanResult();
        scanResultRepository.save(result);

    }

}
