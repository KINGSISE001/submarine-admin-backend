package com.htnova.common.dev.config;

import com.sankuai.meituan.shangou.open.sdk.domain.SystemParam;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MtConfig {
    private String mtappid;
    private String mtkey;

    public MtConfig() {
        this.mtappid = "125564";
        this.mtkey = "c72c1d81a14417345e6908ae87fe621b";
    }

    public String getMtappid() {
        return mtappid;
    }

    public String getMtkey() {
        return mtkey;
    }

    SystemParam systemParam = new SystemParam(getMtappid(), getMtkey());
}
