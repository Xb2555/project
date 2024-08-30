package com.qg24.interceptor;

import com.qg24.utils.JwtUtils;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;

public class CustomConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        // 获取请求头中的 token
        Map<String, List<String>> headers = request.getHeaders();
        List<String> tokenList = headers.get("token");
        if(tokenList==null){
            //拿Sec-Websocket-Protocol中的请求头
            tokenList = headers.get("Sec-Websocket-Protocol");
            if (tokenList != null && !tokenList.isEmpty()) {
                String token = tokenList.get(0);
                if (isValidToken(token)) {
                    // 如果 token 有效，将 token 存储在 config 的 userProperties 中
                    config.getUserProperties().put("protocol", token);
                } else {
                    // 如果 token 无效，抛出异常或进行其他处理
                    System.out.println("Invalid token");
                }
            } else {
                // 如果没有 token，抛出异常或进行其他处理
                System.out.println("Token not found");
            }
        }else {
            if (tokenList != null && !tokenList.isEmpty()) {
                String token = tokenList.get(0);
                if (isValidToken(token)) {
                    // 如果 token 有效，将 token 存储在 config 的 userProperties 中
                    config.getUserProperties().put("token", token);
                } else {
                    // 如果 token 无效，抛出异常或进行其他处理
                    System.out.println("Invalid token");
                }
            } else {
                // 如果没有 token，抛出异常或进行其他处理
                System.out.println("Token not found");
            }
        }
    }

    private boolean isValidToken(String token) {
        // 验证 token 的逻辑
        return !JwtUtils.isTokenExpired(token);
    }
}
