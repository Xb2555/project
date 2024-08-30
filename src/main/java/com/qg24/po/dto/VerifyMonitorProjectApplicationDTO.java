package com.qg24.po.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyMonitorProjectApplicationDTO {
    /**
     * 申请id
     */
    private long applyMonitorProjectId;
    /**
     * 拒绝的理由(通过不需要理由)
     */
    private String rejectReason;
    /**
     * 审核状态(1通过/2拒绝)
     */
    private Long status;
}
