package com.qg24.AIExplain;

import com.alibaba.fastjson.JSON;
import com.qg24.po.entity.Message;
import com.qg24.po.entity.DSReMessage;
import com.qg24.po.entity.DeepSeekMessage;
import okhttp3.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**\
 * 向deepseek发送数据，让其分析该数据
 */

public class ExplainLogs {
    private static String API_KEY = "sk-bffb6d57ca884ed19628e19d57dc6260";
    private static final String API_URL = "https://api.deepseek.com/chat/completions";

    public DSReMessage LogsExplain(Object logs){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // 连接超时时间设置为 60 秒
                .readTimeout(60, TimeUnit.SECONDS)    // 读取超时时间设置为 60 秒
                .writeTimeout(60, TimeUnit.SECONDS)   // 写入超时时间设置为 60 秒
                .build();

        //构建请求体
        DeepSeekMessage message = new DeepSeekMessage();
        Message dsMessage = new Message();
        dsMessage.setContent(logs.toString()+"帮我分析一下这些项目日志，给一些对该项目改进的建议,100字");
        List<Message> messages = new ArrayList<>();
        messages.add(dsMessage);
        message.setMessages(messages);
        message.setModel("deepseek-chat");
        String json = JSON.toJSONString(message);
        RequestBody body = RequestBody.create(JSON.toJSONString(message), MediaType.get("application/json; charset=utf-8"));

        //构建请求
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        //发送请求并处理响应
        try (Response response = client.newCall(request).execute()){
            assert response.body() != null;
            String responseBody = new String(response.body().bytes(), StandardCharsets.UTF_8);
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            DSReMessage reMessage = JSON.parseObject(responseBody,DSReMessage.class);
            System.out.println(reMessage.getChoices().get(0).getMessage().getContent());
            return reMessage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
