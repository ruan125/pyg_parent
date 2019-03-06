package cn.itcast.core.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/*
* 实现springSecurity的UserDetailsService接口,目的是进行给用户赋予权限操作
* cas框架中登录过后的用户才会进入这里,在这里我们给用户赋予对应的权限,在springSecurity配置的拦截器中
* 就会放行,因为有权限访问
* */
public class UserDetailServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //创建权限集合
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        //向权限集合中加入对应的访问权限
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(username,"",authList);
    }
}
