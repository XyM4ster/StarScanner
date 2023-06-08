package org.shaechi.jaadas2.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.shaechi.jaadas2.components.AddScanJobPart;
import org.shaechi.jaadas2.components.ProjectInfoPart;
import org.shaechi.jaadas2.entity.ScanJob;
import org.shaechi.jaadas2.entity.ScanProject;
import org.shaechi.jaadas2.entity.User;
import org.shaechi.jaadas2.repo.ScanProjectRepository;
import org.shaechi.jaadas2.services.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
@Route("project")
public class ProjectDetailView extends VerticalLayout implements HasUrlParameter<Long> {
    //Service中的接口
    private ScanProjectService scanProjectService;
    private ScanJobService jobService;
    private AddNewScanService service;
    private SecurityService securityService;
    private UserDetailsServiceImpl userDetailsServiceImpl;
    //entity
    private ScanProject current;
    private Long projectId;
    //component
    private ProjectInfoPart info;
    //ScanJob 是 entity，绑定到ScanJob.class
    private Grid<ScanJob> scanJobGrid = new Grid<>(ScanJob.class);
    private User user;
    public ProjectDetailView(ScanProjectService scanProjectService, AddNewScanService service, ScanJobService scanJobService,
                             SecurityService securityService, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.scanProjectService = scanProjectService;
        this.service = service;
        this.jobService = scanJobService;
        this.securityService = securityService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;

        UserDetails authenticatedUser = securityService.getAuthenticatedUser();
        this.user = userDetailsServiceImpl.getByUserName(authenticatedUser.getUsername());

        info = new ProjectInfoPart(false, scanProjectService, user);
        //设置表头
        scanJobGrid.setColumns("apkInfo.pkgname", "apkInfo.version", "submitTime", "status");
        //设置表格中的可以点击,跳转到ScanJobDetailView中。
        scanJobGrid.addItemClickListener(e -> {
            scanJobGrid.getUI().ifPresent(ui-> {
                ui.navigate(ScanJobDetailView.class, e.getItem().getId());
            });
        });
        scanJobGrid.addColumn(job -> this.jobService.calcScore(job.getUuid(), false)).setHeader("RScore");
        scanJobGrid.addColumn(job -> this.jobService.calcScore(job.getUuid(), true)).setHeader("AScore");
        add(info);
        add(this.scanJobGrid);
    }

    public void addDetailView(ScanProject current){

        //这里新建了一个component,false为不添加button
        info = new ProjectInfoPart(false, scanProjectService, user);
        //设置表头
        scanJobGrid.setColumns("apkInfo.pkgname", "apkInfo.version", "submitTime", "status");
        //设置表格中的可以点击,跳转到ScanJobDetailView中。
        scanJobGrid.addItemClickListener(e -> {
            scanJobGrid.getUI().ifPresent(ui-> {
                ui.navigate(ScanJobDetailView.class, e.getItem().getId());
            });
        });
        scanJobGrid.addColumn(job -> this.jobService.calcScore(job.getUuid(), false)).setHeader("RScore");
        scanJobGrid.addColumn(job -> this.jobService.calcScore(job.getUuid(), true)).setHeader("AScore");
        add(info);
        add(this.scanJobGrid);
    }

    @PreAuthorize("@securityService.checkScanProject(#aLong)")
    @Override
    public void setParameter(BeforeEvent beforeEvent, Long aLong) {
        projectId = aLong;
        current = scanProjectService.getProjectById(aLong);
        refreshUI();
    }
//    @PreAuthorize("@securityService.check()")
    private void refreshUI()
    {
        info.updateValue(current.getProjectName(), current.getProjectDesc());
        scanJobGrid.setItems(current.getProjectJobs());

        add(new AddScanJobPart(service, current.getId(), user));
    }
}
