package com.qg24.service.impl;


import com.qg24.po.entity.User;
import com.qg24.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

/**
 * 用于从数据库中加载用户信息
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        if(user==null){
            //账户不存在
            throw new UsernameNotFoundException("User not found");
        }
        //获取用户权限
        GrantedAuthority authority = user.getAuthority();

        boolean isEnable = true;

        if(user.getEnabled()==0 && compareDate(user.getDeadline())>=0){
            //如果为冻结状态并且还未到结冻时间
            isEnable = false;
        }


        //返回已认证用户核心数据
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                isEnable,//必须为isEnable字段
                true,
                true,
                true,
                Collections.singleton(authority)
        );
    }

    private int compareDate(String Time){
        //比较时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(Time,formatter);
        LocalDateTime now = LocalDateTime.now().withNano(0);
        return  dateTime.compareTo(now);
    }
}
