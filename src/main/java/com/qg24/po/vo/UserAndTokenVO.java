package com.qg24.po.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAndTokenVO {

    /**
     * 登陆后返回的token
     */
    private String token;

    /**
     * 用户id
     */
    private int userId;

}
