package org.shaechi.jaadas2.services;

import org.shaechi.jaadas2.entity.ScanProject;
import org.shaechi.jaadas2.entity.User;
import org.shaechi.jaadas2.entity.apk.ApkInfo;

public interface AddNewScanService {
    public void addNewApkJobToProject(Long project, String hash, String path, User user);
    public void addNewApkJobToProjectWithProjectDesc(String projectName, String projectDesc, String hash, String path, User user);
}
