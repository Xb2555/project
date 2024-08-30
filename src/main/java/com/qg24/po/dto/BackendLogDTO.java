package com.qg24.po.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackendLogDTO {
    private String projectId;
    private String logLevel;
    private String logTime;
    private String logContent;
}
