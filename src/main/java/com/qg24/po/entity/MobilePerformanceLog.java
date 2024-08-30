package com.qg24.po.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobilePerformanceLog {

    private int fps;

    private int memory;

    private int mobilePerformanceLogId;

    private int projectId;

    private String view;

    private String logTime;

}
