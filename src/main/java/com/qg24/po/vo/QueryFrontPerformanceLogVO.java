package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryFrontPerformanceLogVO {


    private String data;

    private int frontPerformanceLogId;

    private String type;

    private String url;

    private String logTime;

}
