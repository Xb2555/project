package com.qg24.po.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    private int projectId;

    private String projectName;

    private String description;

    private String username;

    private String createTime;

    private String freezeTime;

    private String projectUrl;

    private String projectPassword; // 项目口令
}
