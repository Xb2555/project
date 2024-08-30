package com.qg24.po.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOperateLog {

    private int userOperateLogId;

    private int userId;

    private String date;

    private String operateType;
}
