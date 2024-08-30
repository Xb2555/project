package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailedInfoVO {

    private int projectId;

    private String projectName;

    private String description;

    private String creator;

    private String projectUrl;

    private String createTime;

}

