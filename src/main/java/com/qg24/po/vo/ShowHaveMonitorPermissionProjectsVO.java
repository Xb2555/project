package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowHaveMonitorPermissionProjectsVO {

    private String projectName;

    private int projectId;

    private String description;

    private String createTime;

    /**
     * 也就是用户名
     */
    private String creator;
}
