package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyApplicationOnMonitorProjectTempVO {

    private String applicationTime;

    private String projectName;

    private long projectId;

    private String description;

    private int masterId;

    private String applicationStatus;
}
