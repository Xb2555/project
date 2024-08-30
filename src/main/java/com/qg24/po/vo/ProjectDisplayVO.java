package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDisplayVO {

    private int projectId;

    private String projectName;

    private String description;

    private String creator;

    private String createTime;

    private String projectUrl;
}
