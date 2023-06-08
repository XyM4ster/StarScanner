package org.shaechi.jaadas2.security;

import org.shaechi.jaadas2.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";

    private final MenuFilterInvocationSecurityMetadataSource menuFilterInvocationSecurityMetadataSource;
    private final MenuAccessDecisionManager menuAccessDecisionManager;
    private final MyAuthenticationProvider myAuthenticationProvider;
    private final MyAccessDeniedHandler myAccessDeniedHandler;

    public SecurityConfig(MenuFilterInvocationSecurityMetadataSource menuFilterInvocationSecurityMetadataSource, MenuAccessDecisionManager menuAccessDecisionManager, MyAuthenticationProvider myAuthenticationProvider, MyAccessDeniedHandler myAccessDeniedHandler) {
        this.menuFilterInvocationSecurityMetadataSource = menuFilterInvocationSecurityMetadataSource;
        this.menuAccessDecisionManager = menuAccessDecisionManager;
        this.myAuthenticationProvider = myAuthenticationProvider;
        this.myAccessDeniedHandler = myAccessDeniedHandler;
    }

    /**
     * Require login to access internal pages and configure login form.
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/sign-up, /login").permitAll()
//                    .antMatchers("/login").anonymous()
                .antMatchers("/user").hasRole("admin")
//                .antMatchers("/project/{projectid}")
//                    .access()
                .antMatchers("/admin").hasRole("superadmin")
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .anyRequest().authenticated();
        http.exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler)
                .accessDeniedPage("/403");

        http.csrf().disable()

                // Register our CustomRequestCache, which saves unauthorized access attempts, so the user is redirected after login.
                .requestCache().requestCache(new CustomRequestCache())

                // Restrict access to our application.
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(menuFilterInvocationSecurityMetadataSource); //动态获取url权限配置
                        object.setAccessDecisionManager(menuAccessDecisionManager); //权限判断
                        return object;
                    }
                })
                .and().formLogin().defaultSuccessUrl("/projects")
                .loginPage(LOGIN_URL).permitAll()
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .failureUrl(LOGIN_FAILURE_URL)

                // Configure logout
                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);


    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.authenticationProvider(myAuthenticationProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Allows access to static resources, bypassing Spring Security.
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                // Client-side JS
                "/VAADIN/**",

                // the standard favicon URI
                "/favicon.ico",

                // the robots exclusion standard
                "/robots.txt",

                // web application manifest
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",

                // icons and images
                "/icons/**",
                "/images/**",
                "/styles/**",

                // (development mode) H2 debugging console
                "/h2-console/**");
    }
}