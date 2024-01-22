package com.htnova.common.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ExceptionListener;
import com.htnova.common.constant.GlobalConst;
import com.htnova.common.dto.Result;
import com.htnova.common.exception.ExceptionTranslate;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketExceptionListener implements ExceptionListener {

    /**
     * 前端事件响应中的异常
     *
     * @param e 统一为 ServiceException
     * @param args 前端传递过来的参数
     * @param client SocketIOClient
     */
    @Override
    public void onEventException(Exception e, List<Object> args, SocketIOClient client) {
        Result<?> result = ExceptionTranslate.translate(e);
        client.sendEvent(GlobalConst.SOCKET_ERROR_PATH, result);
        log.error("socket 错误", e);
    }

    @Override
    public void onDisconnectException(Exception e, SocketIOClient client) {}

    @Override
    public void onConnectException(Exception e, SocketIOClient client) {}

    @Override
    public void onPingException(Exception e, SocketIOClient client) {}

    /**
     * @param e
     * @param socketIOClient
     */
    @Override
    public void onPongException(Exception e, SocketIOClient socketIOClient) {

    }

    @Override
    public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        return false;
    }
}
