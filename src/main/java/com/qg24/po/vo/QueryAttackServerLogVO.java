package com.qg24.po.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryAttackServerLogVO {

    private String illegalRequest;

    private String illegalRequestTIme;

    private String ipAddress;
}
