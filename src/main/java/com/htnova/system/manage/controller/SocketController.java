package com.htnova.system.manage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.htnova.common.util.SocketUtil;
import com.htnova.common.util.SpringContextUtil;
import com.htnova.mt.order.service.OrderLogListService;
import com.htnova.mt.order.service.OrderService;
import com.htnova.security.entity.AuthUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Controller
public class SocketController {
    @Resource
    private SocketIOServer socketIOServer;


    // 监听前端的事件



    @OnEvent(value = "welcome")
    public void onEvent(SocketIOClient client, AckRequest ackRequest, String msg) {

    }
    @OnEvent(value = "Connect")
    public void onEvent2(SocketIOClient client, AckRequest ackRequest, String msg) {

    }
    /** 客户端建立连接 */
    @ApiOperation("Socket客户端建立连接")
    @OnConnect
    public void onConnect(SocketIOClient client) {

        log.info("-------------------客户端连接成功------------{}---------",JSON.toJSONString(client.getHandshakeData()) );
        String SESSION =client.getHandshakeData().getSingleUrlParam("SESSION");
        String httpSessionId2 = SocketUtil.getHttpSessionId2(SESSION);
        String httpSessionId = SocketUtil.getHttpSessionId(client);
        if (StringUtils.isBlank(httpSessionId)){
            httpSessionId=httpSessionId2;
        }
        // 如果 httpSession 过期 client 再断开连接，那缓存的 client 对象将无法释放，所以将 authuser 放在SocketIOClient里
        AuthUser authUser = SpringContextUtil.getAuthUser(httpSessionId);
        client.set(SocketUtil.SOCKET_USER_KEY, authUser);
        if (authUser != null) {
            SocketUtil.saveClient(authUser.getId(), client);
            client.sendEvent("Connect", "Socket连接成功");
            log.info("-----------------{}--客户端连接成功---------------------", authUser.getId());
        }
    }

    /** 客户端断开连接 */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        SocketUtil.deleteClient(client);
        log.info("-------------------客户断开连接---------------------");
    }

    // 房间广播消息

    public void broadcast() {
        socketIOServer.getRoomOperations("default_room").sendEvent("/broadcast", "广播消息");
    }
}
