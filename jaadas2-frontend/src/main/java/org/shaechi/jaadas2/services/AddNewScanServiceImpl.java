package org.shaechi.jaadas2.services;

import org.apache.logging.log4j.util.Strings;
import org.shaechi.jaadas2.entity.JobStatus;
import org.shaechi.jaadas2.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AddNewScanServiceImpl implements AddNewScanService{
    final
    TaskExecutor taskExecutor;
    ScanProjectService service;
    ScanJobService jobService;

    public AddNewScanServiceImpl(ScanProjectService service, ScanJobService jobService, TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
        this.service = service;
        this.jobService = jobService;
    }

    @Override
    public void addNewApkJobToProject(Long projectId, String hash, String path, User user) {
        String uuid = service.addJob(projectId, hash, path, user);
        String projectName = service.getProjectById(projectId).getProjectName();

        taskExecutor.execute(() -> {
            logger.info("new task {} scheduled", uuid);
            callEngine(path, uuid, projectName != null && projectName.contains("AOSP"));
        });
    }

    @Override
    public void addNewApkJobToProjectWithProjectDesc(String projectName, String projectDesc, String hash, String path, User user) {
        Long projectId = service.createProjectIfNotExist(projectName, projectDesc, user);
        String uuid = service.addJob(projectId, hash, path, user);
        taskExecutor.execute(() -> {
            logger.info("new task {} scheduled", uuid);
            callEngine(path, uuid, projectName != null && projectName.contains("AOSP"));
        });
    }

    static Logger logger = LoggerFactory.getLogger(AddNewScanService.class.getName());

    private void callEngine(String apkPath, String uuid, boolean isAOSP) {
        //...
    }
}