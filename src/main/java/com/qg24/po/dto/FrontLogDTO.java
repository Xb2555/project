package com.qg24.po.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrontLogDTO {
    private String data;

    private String type;

    private String url;

    private int projectId;
}
