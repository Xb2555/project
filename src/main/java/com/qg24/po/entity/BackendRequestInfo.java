package com.qg24.po.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackendRequestInfo {
   
    private int backendRequestInfoId;
   
    private int projectId;

    private String requestIp;
    
    private String requestUrl;
    
    private String requestMethod;

    private String requestController;

    private String requestApi;

    private String requestTime;
    
}
