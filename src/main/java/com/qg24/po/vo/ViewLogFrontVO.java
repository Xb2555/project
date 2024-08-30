package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewLogFrontVO<T> {

    private int logId;

    private T logInfo;

    private String logTime;

    private String logType;
}
