package org.shaechi.jaadas2.services;


import lombok.extern.slf4j.Slf4j;
import org.shaechi.jaadas2.entity.Menu;
import org.shaechi.jaadas2.entity.Role;
import org.shaechi.jaadas2.repo.MenuRepository;
import org.shaechi.jaadas2.repo.RoleRepository;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MenuFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final MenuRepository menuRepository;
    private final RoleRepository roleRepository;

    public MenuFilterInvocationSecurityMetadataSource(MenuRepository menuRepository, RoleRepository roleRepository) {
        this.menuRepository = menuRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    /**
     * 1.用户存储有权限的project
     *2.在当前类获取登录用户的所有project，利用正则看是否匹配，匹配就分配权限
     * */
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        Set<ConfigAttribute> set = new HashSet<>();
        // 获取请求地址
        //这里需要查询所有的数据库，如果数据量很大，需要缓存
        /*TODO 如何和RequestURl匹配，如果requesturl变化了呢？//project??
        *  TODO  即使存了Project，那怎么判断这个路径他能不能访问呢？
        *   因为需要hasRole，这样的话是每个登陆角色都是一个新的role么？
        *   这样Security config里面要写很多个hasRole
        * */

        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        log.info("requestUrl >> {}", requestUrl);

        List<Menu> allMenus = menuRepository.findAll();
        //获取所有的菜单
        if (!CollectionUtils.isEmpty(allMenus)) {
            //过滤得到符合requestUrl的urlList
            List<String> urlList = allMenus.stream().filter(f->f.getUrl().endsWith("**")?
                    requestUrl.startsWith(f.getUrl().substring(0,f.getUrl().lastIndexOf("/"))) :
                    requestUrl.equals(f.getUrl())).map(menu -> menu.getUrl()).collect(Collectors.toList());
            for (String url:urlList){
                //menu和Role是manytomany的关系
                List<Role> roles = roleRepository.findByMenu(menuRepository.findByUrl(url));
                if(!CollectionUtils.isEmpty(roles)){
                    roles.forEach(role -> {
                        SecurityConfig securityConfig = new SecurityConfig(role.getAuthority());
                        set.add(securityConfig);
                    });
                }
            }
        }
        if (ObjectUtils.isEmpty(set)) {
            return SecurityConfig.createList("ROLE_LOGIN");
        }
        return set;
    }
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
