package org.shaechi.jaadas2.services;


import org.shaechi.jaadas2.entity.Menu;
import org.shaechi.jaadas2.entity.Role;
import org.shaechi.jaadas2.entity.User;
import org.shaechi.jaadas2.repo.MenuRepository;
import org.shaechi.jaadas2.repo.RoleRepository;
import org.shaechi.jaadas2.repo.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final MenuRepository menuRepository;

    private final RoleRepository roleRepository;


    public UserDetailsServiceImpl(UserRepository userRepository, MenuRepository menuRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.menuRepository = menuRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        //List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("user");
//        return new MyUserPrincipal(regisInfo,auths);
        return user;
        //return new User("aaa",new BCryptPasswordEncoder().encode("bbb"), auths);
    }

    public void saveUsernameAndPassword(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        Role role = new Role();
        role.setCode("user");
        role.setName("普通用户");
        roleRepository.save(role);
        user.setRole(role);
        userRepository.save(user);
    }
    public User getByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

}
