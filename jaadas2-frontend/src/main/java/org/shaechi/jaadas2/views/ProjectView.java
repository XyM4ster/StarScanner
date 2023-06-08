package org.shaechi.jaadas2.views;

import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.shaechi.jaadas2.components.ProjectInfoPart;
import org.shaechi.jaadas2.entity.ScanProject;
import org.shaechi.jaadas2.entity.User;
import org.shaechi.jaadas2.services.ScanProjectService;
import org.shaechi.jaadas2.services.SecurityService;
import org.shaechi.jaadas2.services.UserDetailsServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;

@Route("projects")
public class ProjectView extends VerticalLayout{

    /**
     * 1.初始时，projectsGrid为空。用户创建一个project，创建之后，点进去，再提交apk。
     * */
    private ScanProjectService service;
    private Grid<ScanProject> projectsGrid = new Grid<>(ScanProject.class);;
    private ProjectInfoPart info;
    private final SecurityService securityService;
    private UserDetailsServiceImpl userDetailsServiceImpl;
    public ProjectView(ScanProjectService service, SecurityService securityService,  UserDetailsServiceImpl userDetailsServiceImpl)
    {
        this.service = service;
        this.securityService = securityService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        createHeader();

        UserDetails authenticatedUser = securityService.getAuthenticatedUser();
        User user = userDetailsServiceImpl.getByUserName(authenticatedUser.getUsername());

        info = new ProjectInfoPart(true, service, user);

        projectsGrid.addItemClickListener(e -> {
            projectsGrid.getUI().ifPresent(ui -> {
                ui.navigate(ProjectDetailView.class, e.getItem().getId());
            });
        });

        projectsGrid.setItems(this.service.getAllProjects());
        add(projectsGrid);
        add(info);
    }


    private void createHeader() {
        H1 logo = new H1("StarScanner");
        logo.addClassNames("text-l", "m-m");

        Button logout = new Button("Log out", e -> securityService.logout());

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        add(header);

    }

}
