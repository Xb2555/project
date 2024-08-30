package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminQueryProjectApplicationVO {

    private String projectName;

    private int applicationId;

    private String description;

    private String creator;

    private String projectUrl;

    private String createTime;

    private String applicationType;

    private String status;
}
