package com.qg24.po.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobileExceptionLog {

    private String data;

    private int mobileExceptionLogId;

    private int projectId;

    private String logTime;
}

