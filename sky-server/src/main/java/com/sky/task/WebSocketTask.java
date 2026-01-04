package com.sky.task;

import com.alibaba.fastjson.JSON;
import com.sky.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketTask {
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 通过WebSocket每隔5秒向客户端发送消息
     */
//    @Scheduled(cron = "0/5 * * * * ?")
    public void sendMessageToClient() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String currentTime = formatter.format(LocalDateTime.now());

        // 1. 构建Map存放消息内容
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("content", "这是来自服务端的消息");
        msgMap.put("time", currentTime);

        // 2. 转为合法JSON字符串
        String jsonMessage = JSON.toJSONString(msgMap);

        // 3. 推送JSON消息
        webSocketServer.sendToAllClient(jsonMessage);
    }
}
