package com.htnova.common.dev.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pay")
@Data
public class PayConfig {

    @Value("${pay.doMainUrl}")
    private String doMainUrl;
    @Value("${pay.apikey}")
    private String apikey;

    @Value("${pay.signkey}")
    private String signkey;

}
