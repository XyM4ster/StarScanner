package org.shaechi.jaadas2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;



//    public void setBCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }


    public void setUserDetailsServiceImpl(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

//      boolean flag = bCryptPasswordEncoder.matches(password, userDetails.getPassword());
//        boolean flag = bCryptPasswordEncoder.matches(password, regisInfoRepository.findByAccount(username).getPassword());
        // 校验通过
        boolean flag = password.equals(userDetails.getPassword());

        if (flag) {
            // 将权限信息也封装进去

            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        }

        throw new AuthenticationException("用户密码错误") {
        };

    }


    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }




}
