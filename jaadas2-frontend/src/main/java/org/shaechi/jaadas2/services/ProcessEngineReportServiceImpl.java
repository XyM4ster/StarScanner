package org.shaechi.jaadas2.services;

import com.google.gson.Gson;
import org.shaechi.jaadas2.entity.apk.ApkComponent;
import org.shaechi.jaadas2.entity.engine.EngineComponentReport;
import org.shaechi.jaadas2.entity.engine.EngineReport;
import org.shaechi.jaadas2.entity.JobStatus;
import org.shaechi.jaadas2.entity.ScanJob;
import org.shaechi.jaadas2.entity.apk.ApkInfo;
import org.shaechi.jaadas2.entity.result.ScanResult;
import org.shaechi.jaadas2.repo.*;
import org.shaechi.jaadas2.util.XMLToResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProcessEngineReportServiceImpl implements ProcessEngineReportService {
    Logger logger = LoggerFactory.getLogger(ProcessEngineReportService.class.getName());

    List<Long> idList = new ArrayList<>();

    @Autowired
    ApkInfoRepository apkInfoRepository;
    @Autowired
    ScanJobRepository scanJobRepository;
    @Autowired
    ScanProjectRepository scanProjectRepository;
    @Autowired
    ApkComponentRepository apkComponentRepository;
    @Autowired
    SourceCodeRepository sourceCodeRepository;
    @Autowired
    ScanResultRepository scanResultRepository;
    @Autowired
    SourceCodeDetailsRepository sourceCodeDetailsRepository;


    Gson gson = new Gson();
    @Override
    public void processEngineReport(UUID jobUUID, EngineReport report) {
        //synchronized (this)
        {
            logger.info("Got incoming for job {}, request type {}", jobUUID, report.getType());
            ScanJob job = scanJobRepository.findByUuid(jobUUID);
            if (job == null) {
                logger.warn("Report got with unknown job uuid {}", jobUUID);
            }
            else {
                switch (report.getType()) {
                    case ENGINE_APK_META_REPORT:
                        ApkInfo info = gson.fromJson(report.getContent(), ApkInfo.class);
                        ApkInfo infoInDB = job.getApkInfo();
                        // the component in original db should be null
                        if (infoInDB.getComponents() != null && infoInDB.getComponents().size() != 0) {
                            logger.warn("The job {} has non-empty components. Overwriting.", jobUUID);
                        }
                        infoInDB.setComponents(info.getComponents());
                        logger.info("Got pkgname {} for uuid {}", info.getPkgname(), jobUUID);
                        infoInDB.setPkgname(info.getPkgname());
                        infoInDB.setVersion(info.getVersion());
                        infoInDB.setUid(info.getUid());
                        job.setTotal(info.getComponents().size());
                        job.setCounter(0);
                        break;
                    case ENGINE_APK_COMP_REPORT:
                        //current just parse path. FIXME
                        EngineComponentReport ecr = gson.fromJson(report.getContent(), EngineComponentReport.class);
                        try {
                            List<ScanResult> results = XMLToResult.parseXMLwithReader(ecr.getReportPath(), 1);

                            Long id = job.getApkInfo().getId();
                            if(results.size() != 0 && !idList.contains(id)){
                                idList.add(job.getApkInfo().getId());
                                ScanFileDirectoryServiceImpl scanFileDirectoryServiceImpl  = new ScanFileDirectoryServiceImpl(sourceCodeRepository, scanResultRepository, sourceCodeDetailsRepository, scanJobRepository);
                                //这里的apkname还需要修改
                                scanFileDirectoryServiceImpl.apkDeCompile("PowerKeeper.apk",job);
                            }

                            boolean found = false;
                            for (ApkComponent component : job.getApkInfo().getComponents()) {
                                if (component.getComponentName().equals(ecr.getComponentName())) {
                                    for (ScanResult result : results) {
                                        result.setComponent(component);
                                    }
                                    job.setCounter(ecr.getCounter());
                                    job.setTotal(ecr.getTotal());
                                    job.getResults().addAll(results);
                                    found = true;
                                    break;
                                }
                            }
                            if (!found)
                                logger.warn("Component report does not have component in db: {}", ecr.getComponentName());
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("Parse component result failed.", e);
                        }
                        break;
                    case ENGINE_SCAN_FINISH:
                        job.setScanFinishTime(LocalDateTime.now());
                        job.setStatus(JobStatus.SuccessFinish);
                }
                scanJobRepository.save(job);
            }
        }
    }
}
