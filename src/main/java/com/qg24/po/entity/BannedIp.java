package com.qg24.po.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 被封禁的ip
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannedIp{
    private Integer bannedIpId;
    private String ip;
    private String bannedReason;//封禁原因
    private String bannedTime;
}
