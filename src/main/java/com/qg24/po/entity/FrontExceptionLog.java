package com.qg24.po.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrontExceptionLog {

    private String data;

    private int frontExceptionLogId;

    private String type;

    private String url;

    private int projectId;

    private String logTime;
}
