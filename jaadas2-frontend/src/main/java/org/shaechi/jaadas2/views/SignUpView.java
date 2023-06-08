package org.shaechi.jaadas2.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.shaechi.jaadas2.repo.UserRepository;
import org.shaechi.jaadas2.services.UserDetailsServiceImpl;

@Route("sign-up")
public class SignUpView extends VerticalLayout {

    TextField userNameField = new TextField("Username");
    PasswordField passwordField = new PasswordField();
    Checkbox checkbox = new Checkbox("You agree to the Privacy Policy");
    Button button = new Button("Create account");
    Dialog dialog = new Dialog();
    private UserDetailsServiceImpl userDetailsServiceImpl;
    private final UserRepository userRepository;
    private String userName;
    private String passWord;

    Boolean userNameValid;
    Boolean passwordValid;


    public SignUpView(UserDetailsServiceImpl userDetailsServiceImpl, UserRepository userRepository) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.userRepository = userRepository;

        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        userNameField.setLabel("Username");
        passwordField.setLabel("Password");

        Button userNameFieldHelpButton = new Button();
        //设置Button不显示边框
        userNameFieldHelpButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        userNameField.addValueChangeListener(event -> {
            isValidUsername(event.getValue());
            if(userNameValid){
                userNameFieldHelpButton.setText("Username is valid");
                userNameFieldHelpButton.getElement().getThemeList().add("badge success");
            }
            else{
                userNameFieldHelpButton.setText("Not a valid Username");
                //设置Button的颜色
                userNameFieldHelpButton.getElement().getThemeList().add("badge error");

            }
        });
        userNameField.setHelperComponent(userNameFieldHelpButton);

        passwordField.setHelperText("A password must be at least 8 characters. It has to have at least one letter and one digit.");
        passwordField.setPattern("^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$");
        passwordField.setErrorMessage("Not a valid password");
        passwordField.addValueChangeListener(event -> {
            if(event.getValue().matches(passwordField.getPattern())){
                passwordValid = true;
                passWord = event.getValue();
            }
            else{
                passwordValid = false;
            }
        });

        /***
         * 1.只有当账号accountValid == true && 密码符合正则匹配 && checkbox勾选了
         * 2. 满足1的条件，点击create account按钮，此时在数据库中保存该账号 密码。
         */

        //设置提示用户输入密码的强弱
//        Icon checkIcon = VaadinIcon.CHECK.create();
//        checkIcon.setVisible(false);
//        checkIcon.getStyle().set("color", "var(--lumo-success-color)");
//        //设置后缀组件，一个图标
//        passwordField.setSuffixComponent(checkIcon);

        button.setEnabled(false);
        checkbox.addClickListener(event -> {
            if(userNameValid && passwordValid && !event.getSource().isEmpty()){
                button.setEnabled(true);
            }
            else{
                button.setEnabled(false);
            }
        });

        dialog.getElement().setAttribute("aria-label", "Create new employee");
        dialog.add(new Label("Registration complete!"));
        dialog.addDialogCloseActionListener(event -> dialog.getUI().ifPresent(ui -> {
            ui.navigate(LoginView.class);
        }));

        button.addClickListener(event -> {
            userDetailsServiceImpl.saveUsernameAndPassword(userName,passWord);
            dialog.open();
        });

        add(new H1("Sign up"), userNameField, passwordField, checkbox, button);
    }

    public void isValidUsername(String userName){
        if(userRepository.findByUsername(userName) != null || userName.length() == 0){
            userNameValid = false;
        }
        else{
            userNameValid = true;
            this.userName = userName;
        }
    }

}
