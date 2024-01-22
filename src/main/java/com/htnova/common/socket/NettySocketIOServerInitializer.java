package com.htnova.common.socket;

import io.netty.channel.*;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import org.springframework.stereotype.Service;


@Service
public class NettySocketIOServerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // Add the necessary HTTP handlers
        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpResponseEncoder());
        pipeline.addLast(new HttpServerCodec());

        // Configure CORS headers
        CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin()
                .allowedRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
                .allowedRequestHeaders(HttpHeaders.Names.ORIGIN, HttpHeaders.Names.CONTENT_TYPE,
                        HttpHeaders.Names.ACCEPT, HttpHeaders.Names.AUTHORIZATION)
                .build();
        pipeline.addLast(new CorsHandler(corsConfig));

        // Add your SocketIO server handler
        //pipeline.addLast(new NettySocketIOServerHandler());
    }
}
