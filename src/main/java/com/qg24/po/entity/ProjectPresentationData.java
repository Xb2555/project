package com.qg24.po.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPresentationData {

    private int presentationId;
    
    private int projectId;
    
    private int visits;
    
    private int totalVisits;
    
    private int errorNumber;
    
    private float errorRate;
    
    private String date;

    private int frontErrorNumber;

    private int backendErrorNumber;

    private int mobileErrorNumber;

    private double warningRate;
  
}
