package com.qg24.po.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMonitorProject {

    private int userMonitorProjectId;

    private int userId;

    private int projectId;
}
