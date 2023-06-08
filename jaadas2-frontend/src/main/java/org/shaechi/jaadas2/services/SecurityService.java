package org.shaechi.jaadas2.services;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.shaechi.jaadas2.entity.ScanJob;
import org.shaechi.jaadas2.entity.ScanProject;
import org.shaechi.jaadas2.entity.User;
import org.shaechi.jaadas2.repo.ScanJobRepository;
import org.shaechi.jaadas2.repo.ScanProjectRepository;
import org.shaechi.jaadas2.repo.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SecurityService {

    private static final String LOGOUT_SUCCESS_URL = "/";
    private final UserRepository userRepository;
    private final ScanProjectRepository scanProjectRepository;
    private final ScanJobRepository scanJobRepository;
    private User user;
    public SecurityService(UserRepository userRepository, ScanProjectRepository scanProjectRepository, ScanJobRepository scanJobRepository) {
        this.userRepository = userRepository;
        this.scanProjectRepository = scanProjectRepository;
        this.scanJobRepository = scanJobRepository;
    }
    //获取认证的用户信息
    public UserDetails getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) context.getAuthentication().getPrincipal();
        }
        // Anonymous or no authentication.
        return null;
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }

    public boolean checkScanProject(Long scanProjectId){
        this.user = this.userRepository.findByUsername(getAuthenticatedUser().getUsername());
        List<ScanProject> scanProjects = user.getScanProjects();
        for(ScanProject scanProject : scanProjects){
            if(scanProject.getId().equals(scanProjectId)){
                return true;
            }
        }
        throw new AccessDeniedException("当前用户无权访问此页面");
    }
    public boolean checkScanJob(Long scanJobId){
        this.user = this.userRepository.findByUsername(getAuthenticatedUser().getUsername());
        List<ScanJob> scanJobs = user.getScanJobs();
        for(ScanJob scanJob : scanJobs){
            if(scanJob.getId().equals(scanJobId)){
                return true;
            }
        }
        throw new AccessDeniedException("当前用户无权访问此页面");
    }
}