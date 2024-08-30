package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyApplicationProjectVO {

    private String applicationTime;

    private String projectName;

    private int projectId;

    private String description;

    private String applicationStatus;

    /**
     * 被拒理由
     */
    private String rejectReason;

    /**
     * 发布或者更新项目
     */
    private String applicationType;

}
