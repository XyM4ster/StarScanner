package org.shaechi.jaadas2.apis;

import org.shaechi.jaadas2.apitool.APIScanJob;
import org.shaechi.jaadas2.entity.User;
import org.shaechi.jaadas2.entity.engine.EngineReport;
import org.shaechi.jaadas2.services.*;
import org.shaechi.jaadas2.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/")
public class ApiController {

    @Autowired private ProcessEngineReportService service;
    @Autowired private AddNewScanServiceImpl addNewScanService;
    @Autowired private SecurityService securityService;
    @Autowired private UserDetailsServiceImpl userDetailsServiceImpl;
    @PostMapping(value = "/enginereport/{uuid}")
    public String recvEngineReport(@RequestBody EngineReport report, @PathVariable UUID uuid) {
        service.processEngineReport(uuid, report);
        return "success";
    }

    @PostMapping (value = "/apiscan/")
    public String recvAPISubmitReport(@RequestBody APIScanJob job) {
        if (SecurityUtil.validateFilePath(job.getApkPath()))
            return "invalid file path";
        String hash = "";
        if (job.getMd5hash() != null)
            hash = job.getMd5hash().toUpperCase();
        else {
            try {
                hash = SecurityUtil.calcFileHash(job.getApkPath());
            } catch (Exception e) {
                return "error when getting file hash";
            }
        }
        UserDetails authenticatedUser = securityService.getAuthenticatedUser();
        User user = userDetailsServiceImpl.getByUserName(authenticatedUser.getUsername());
        addNewScanService.addNewApkJobToProjectWithProjectDesc(job.getProjectName(), job.getProjectDesc(), hash, job.getApkPath(), user);
        return "success";
    }

}
