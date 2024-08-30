package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryAllUserOperationLogVO {

    private String userName;

    private String operate;

    private String operateTime;

}

