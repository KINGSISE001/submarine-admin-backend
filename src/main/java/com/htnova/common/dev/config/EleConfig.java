package com.htnova.common.dev.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Data
public class EleConfig {
    // 初始化引擎类
    @Value("${ele.info.appkey}")
    String appkey ;
    @Value("${ele.info.secKey}")
    String secKey ;



}
