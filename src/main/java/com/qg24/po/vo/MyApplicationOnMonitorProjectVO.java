package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyApplicationOnMonitorProjectVO {

    private String applicationTime;

    private String projectName;

    private long projectId;

    private String description;

    /**
     * 也就是用户名
     */
    private String creator;

    private String applicationStatus;

}
