package com.qg24.po.result;

import com.qg24.po.vo.AdminQueryUserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsocketMessage {
    private Object data;
    private String msg;
    private String type;
    private List<AdminQueryUserVO> userList;
}
