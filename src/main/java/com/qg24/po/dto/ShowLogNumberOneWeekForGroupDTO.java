package com.qg24.po.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowLogNumberOneWeekForGroupDTO {
    /**
     * 日志组别，（后台0、前端1、移动2）
     */
    private int groupType;
    /**
     * 日志类型，(0异常/1其他包括性能，正常日志/2后台自定义日志)
     */
    private int logType;
    /**
     * 项目id
     */
    private int projectId;
}
