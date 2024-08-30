package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewLogVO {

    private String logType;

    private String logTime;

    private String logInfo;

    private int logId;
}
