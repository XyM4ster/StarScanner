//package org.shaechi.jaadas2.services;
//
//
//import org.shaechi.jaadas2.entity.RegisInfo;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//
//public class MyUserPrincipal implements UserDetails {
//    private RegisInfo regisInfo;
//    private Collection<? extends GrantedAuthority> authorities;
//
//    public MyUserPrincipal(RegisInfo regisInfo, Collection<? extends GrantedAuthority> authorities) {
//        this.regisInfo = regisInfo;
//    }
//
//    public RegisInfo getRegisInfo(){
//        return regisInfo;
//    }
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public String getPassword() {
//        return regisInfo.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return regisInfo.getAccount();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return false;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return false;
//    }
//}
