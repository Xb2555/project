package com.qg24.po.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyMonitorProject {

    private int applyMonitorProjectId;

    private int applicantId;
    
    private int projectId;

    private int masterId;

    private String applicationTime;

    private String applicationStatus;

    private String rejectReason;

}
