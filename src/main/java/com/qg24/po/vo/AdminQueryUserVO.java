package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminQueryUserVO {

    private int userId;

    private String username;

    private String createTime;

    private String enabled;

    private String isOnline;

}
