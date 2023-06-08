package org.shaechi.jaadas2.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/403")
public class ExceptionView extends VerticalLayout {
    public ExceptionView() {
        add(new H1("HTTP Status 403 - Access is denied \n " +
                "You do not have permission to access this page"));
    }
}
