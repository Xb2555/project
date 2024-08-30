package com.qg24.po.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishProjectDTO {

    private String projectName;

    private long userId;

    private String description;

    private String projectUrl;

    private String projectPassword;

}
