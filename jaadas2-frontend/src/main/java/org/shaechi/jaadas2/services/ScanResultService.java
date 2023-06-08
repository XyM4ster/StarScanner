package org.shaechi.jaadas2.services;

import org.shaechi.jaadas2.entity.apk.ApkInfo;
import org.shaechi.jaadas2.entity.result.ScanResult;

public interface ScanResultService {
    void updateResultComment(Long id, String comment);
    void updateResultStatus(Long id, boolean status);
    ScanResult getById(Long id);
    ApkInfo getApkByCompId(Long id);
}
