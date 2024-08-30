package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowSelfProjectsVO {

    private String projectName;

    private long projectId;

    private String description;

    private String createTime;
}
