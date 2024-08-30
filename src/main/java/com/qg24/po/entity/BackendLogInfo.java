package com.qg24.po.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackendLogInfo {
    private int backendLogInfoId;
    private int projectId;
    private String logContent;
    private String logTime;
    private String logLevel;
}
