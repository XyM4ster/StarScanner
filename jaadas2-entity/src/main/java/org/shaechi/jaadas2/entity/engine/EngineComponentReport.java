package org.shaechi.jaadas2.entity.engine;

import lombok.Data;

@Data
public class EngineComponentReport {
    String reportPath;
    String componentName;
    String reportStatus;
    int counter;
    int total;
}
