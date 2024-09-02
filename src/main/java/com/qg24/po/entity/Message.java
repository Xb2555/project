package com.qg24.po.entity;

import lombok.Data;

@Data
public class Message {
    private String content;
    private String role = "system";
}
