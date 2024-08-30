package com.qg24.po.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackendExceptionInfoDTO {
    private String projectId;
    private String exception;
    private String errorTime;
}