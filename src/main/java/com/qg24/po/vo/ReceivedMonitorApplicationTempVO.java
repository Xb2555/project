package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedMonitorApplicationTempVO {

   private String applicationTime;

   private String projectName;

   private int projectId;

   private String description;

   private int applicantId;

   private String applicationStatus;

   private int applyMonitorProjectId;
}
