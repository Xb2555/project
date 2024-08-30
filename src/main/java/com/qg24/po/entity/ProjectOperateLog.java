package com.qg24.po.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectOperateLog {

    private int presentationId;

    private int projectId;

    private String description;

    private String date;

    private String operateType;
    
}
