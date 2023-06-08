package org.shaechi.jaadas2.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.shaechi.jaadas2.views.LoginView;
import org.shaechi.jaadas2.views.SignUpView;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {
    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }
    //reroute all requests to the login, if the user is not logged in
    private void authenticateNavigation(BeforeEnterEvent event) {
        if ((!LoginView.class.equals(event.getNavigationTarget())
                && !SecurityUtils.isUserLoggedIn())) {
//            if(!SignUpView.class.equals(event.getNavigationTarget())){
//                event.rerouteTo(LoginView.class);
//            }

        }

    }
}