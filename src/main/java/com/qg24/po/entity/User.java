package com.qg24.po.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userId;
    private String username;
    private String password;
    private int enabled;//是否被冻结
    private String deadline;//冻结时间
    private String role;
    private String createTime; //注册时间
    public GrantedAuthority getAuthority(){
        return new SimpleGrantedAuthority(role);
    }
}
