package com.qg24.po.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackendExceptionInfo {
    
    private int backendExceptionInfoId;

    private int projectId;

    private String exception;

    private String errorTime;
}
