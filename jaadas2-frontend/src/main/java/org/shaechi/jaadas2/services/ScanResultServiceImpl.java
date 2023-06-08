package org.shaechi.jaadas2.services;

import org.shaechi.jaadas2.entity.apk.ApkInfo;
import org.shaechi.jaadas2.entity.result.ScanResult;
import org.shaechi.jaadas2.repo.ApkInfoRepository;
import org.shaechi.jaadas2.repo.ScanResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ScanResultServiceImpl implements ScanResultService{
    @Autowired
    private ScanResultRepository resultRepository;
    @Autowired
    private ApkInfoRepository apkInfoRepository;

    @Override
    public void updateResultComment(Long id, String comment) {
        ScanResult result = resultRepository.getById(id);
        result.setComment(comment);
        resultRepository.save(result);
    }

    @Override
    public void updateResultStatus(Long id, boolean status) {
        ScanResult result = resultRepository.getById(id);
        result.setIgnored(status);
        resultRepository.save(result);
    }

    @Override
    public ScanResult getById(Long id) {
        return resultRepository.getById(id);
    }

    @Override
    public ApkInfo getApkByCompId(Long id) {
        return apkInfoRepository.findByComponentsId(id);
    }
}
