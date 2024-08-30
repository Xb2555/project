package com.qg24.po.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelUserMonitorPermissionDTO {

    private int userId;

    private int projectId;
}
