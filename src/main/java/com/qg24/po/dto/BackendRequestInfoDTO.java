package com.qg24.po.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackendRequestInfoDTO {
    private String projectId;
    private String requestIp;
    private String requestUrl;
    private String requestMethod;
    private String requestController;
    private String requestApi;
    private String requestTime;
}
