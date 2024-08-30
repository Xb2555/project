package com.qg24.po.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobilePerformanceLogDTO {

    private long fps;

    private long memory;

    private long projectId;

    private String view;

}
