package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPresentationDateOneWeek {
  
    private String date;

    private int totalVisit;

    private int visits;

    private int errorNumber;

    private float errorRate;

    private int frontErrorNumber;

    private int backendErrorNumber;

    private int mobileErrorNumber;
}
