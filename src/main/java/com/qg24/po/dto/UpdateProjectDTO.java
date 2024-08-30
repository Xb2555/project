package com.qg24.po.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectDTO {

    private int projectId;

    private String projectUrl;

    private String description;

    private String projectPassword;

    private int userId;
}
