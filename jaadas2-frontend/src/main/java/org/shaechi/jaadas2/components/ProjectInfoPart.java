package org.shaechi.jaadas2.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.shaechi.jaadas2.entity.User;
import org.shaechi.jaadas2.services.ScanProjectService;

public class ProjectInfoPart extends VerticalLayout {

    private TextField projectNameField = new TextField("Project Name");
    private TextField projectDescField = new TextField("Project Description");
    private Button addButton = new Button("Submit");


    public ProjectInfoPart(boolean isAdd, ScanProjectService service, User user)
    {
        projectNameField.setReadOnly(!isAdd);
        projectDescField.setWidthFull();
        projectNameField.setWidthFull();
        projectDescField.setReadOnly(!isAdd);

        add(projectNameField, projectDescField);

        addButton.addClickListener(buttonClickEvent -> {
            service.createProject(projectNameField.getValue(), projectDescField.getValue(), user);
            Notification notification = new Notification("Project create successfully.", 3000);
            notification.open();
        });
        if (isAdd) {
            add(addButton);
        }
    }

    public void updateValue(String name, String desc) {
        projectNameField.setValue(name);
        projectDescField.setValue(desc);
    }
}
