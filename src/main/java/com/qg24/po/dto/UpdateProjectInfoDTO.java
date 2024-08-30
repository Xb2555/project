package com.qg24.po.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectInfoDTO {

    private int projectId;

    private String projectUrl;

    private String description;

    private String projectPassword;

    private String projectName;

    private int userId;
}
