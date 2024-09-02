package com.qg24.po.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DSReMessage {
    private List<Choice> choices;
    private long created;
    private String id;
    private String model;
    private String object;
}
