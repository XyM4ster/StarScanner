package org.shaechi.jaadas2.services;

import lombok.extern.slf4j.Slf4j;
import org.shaechi.jaadas2.entity.*;
import org.shaechi.jaadas2.entity.apk.ApkInfo;
import org.shaechi.jaadas2.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ScanProjectServiceImpl implements ScanProjectService {
    final
    ApkInfoRepository apkInfoRepository;
    final
    ScanJobRepository scanJobRepository;
    final
    ScanProjectRepository scanProjectRepository;
    final UserRepository userRepository;
    final
    TaskExecutor taskExecutor;

    @Autowired
    private SecurityService securityService;
    public ScanProjectServiceImpl(ApkInfoRepository apkInfoRepository, ScanJobRepository scanJobRepository, ScanProjectRepository scanProjectRepository, TaskExecutor taskExecutor, SecurityService securityService, UserRepository userRepository) {
        this.apkInfoRepository = apkInfoRepository;
        this.scanJobRepository = scanJobRepository;
        this.scanProjectRepository = scanProjectRepository;
        this.taskExecutor = taskExecutor;
        this.userRepository = userRepository;
    }

    @Override
    public String addJob(Long projectId, String hash, String path, User user)
    {

        ApkInfo info = new ApkInfo();
        info.setMd5hash(hash);
        info.setApkPath(path);

        ScanJob job = new ScanJob();
        job.setSubmitTime(LocalDateTime.now());
        job.setStatus(JobStatus.Pending);
        job.setApkInfo(info);

        scanJobRepository.save(job);

        ScanProject project = scanProjectRepository.getById(projectId);
        project.addScanJob(scanJobRepository.getById(job.getId()));
        scanProjectRepository.save(project);

        //TODO 这里创建了一个Job，把它添加到许可的url中
        user.addScanJob(job);
        userRepository.save(user);

        return job.getUuid().toString();
    }

    @Override
    public ScanProject getProjectById(Long projectId) {
        return scanProjectRepository.getById(projectId);
    }

    @Override
    public Long createProject(String projectName, String projectDesc,  User user) {

        ScanProject project = new ScanProject();
        project.setProjectName(projectName);
        project.setProjectDesc(projectDesc);
        scanProjectRepository.save(project);
        user.addScanProject(project);
        List<ScanProject> scanProjects = user.getScanProjects();
        for(ScanProject scanProject : scanProjects){
            log.info("User ScanProjects",scanProject);
        }
        userRepository.save(user);
        return project.getId();
    }

    @Override
    public List<ScanProject> getAllProjects() {
        return scanProjectRepository.findAll();
    }

    @Override
    public Long createProjectIfNotExist(String projectName, String projectDesc, User user) {
        ScanProject project = scanProjectRepository.getByProjectName(projectName);
        if (project == null) {
            return createProject(projectName, projectDesc, user);
        }
        return project.getId();
    }

}
