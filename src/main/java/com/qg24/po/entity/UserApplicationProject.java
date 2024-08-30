package com.qg24.po.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserApplicationProject {

    private int userApplicationProjectId;
    
    private int userId;
    
    private int projectId;

    private String projectName;

    private String projectUrl;
    
    private String applicationTime;

    private int applicationType;

    private int status;

    private String rejectReason;

    private String projectPassword;

    private String description;
}
