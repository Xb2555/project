package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedMonitorApplicationVO {

   private String applicationTime;

   private String projectName;

   private int projectId;

   private String description;

   private String applicant;

   private String applicationStatus;

   private int applyMonitorProjectId;
}
