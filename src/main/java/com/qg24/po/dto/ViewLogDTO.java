package com.qg24.po.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewLogDTO {
    private int groupType;
    private int pageSize;
    private int page;
    private int projectId;
    private int logType;
}
