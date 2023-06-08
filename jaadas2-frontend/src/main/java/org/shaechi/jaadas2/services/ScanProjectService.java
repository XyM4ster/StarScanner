package org.shaechi.jaadas2.services;


import org.shaechi.jaadas2.entity.ScanProject;
import org.shaechi.jaadas2.entity.User;

import java.util.List;

public interface ScanProjectService {
    String addJob(Long projectId, String hash, String path, User user);
    ScanProject getProjectById(Long projectId);
    Long createProject(String projectName, String projectDesc, User user);
    List<ScanProject> getAllProjects();
    Long createProjectIfNotExist(String projectName, String projectDesc, User user);
}
