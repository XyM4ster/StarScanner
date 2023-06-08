package org.shaechi.jaadas2.services;


import lombok.extern.slf4j.Slf4j;
import org.shaechi.jaadas2.entity.User;
import org.shaechi.jaadas2.repo.RoleRepository;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Slf4j
public class MenuAccessDecisionManager implements AccessDecisionManager {


    private final RoleRepository roleRepository;

    public MenuAccessDecisionManager(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
// 当前请求需要的权限
        log.info("collection:{}", collection);
        log.info("principal:{} authorities:{}", authentication.getPrincipal().toString());

        Collection<? extends GrantedAuthority> authorities = null;
        for (ConfigAttribute configAttribute : collection) {
            // 当前请求需要的权限
            String needRole = configAttribute.getAttribute();
            if ("ROLE_LOGIN".equals(needRole)) {
                return;
            }
            // 当前用户所具有的权限
            if(authorities == null){
                User loginUser = (User) authentication.getPrincipal();
                authorities = loginUser.getAuthorities();
            }
            for (GrantedAuthority grantedAuthority : authorities) {
                // 包含其中一个角色即可访问
                if (grantedAuthority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("SimpleGrantedAuthority!!");

    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
