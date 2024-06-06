package com.htnova.common.dev.config;

import com.sankuai.meituan.shangou.open.sdk.domain.SystemParam;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MtConfig {
    private String mtappid;
    private String mtkey;

    public MtConfig() {
        this.mtappid = "5430";
        this.mtkey = "bb971f25af7ce4d9a832d7a4500306dd";
    }

    public String getMtappid() {
        return mtappid;
    }

    public String getMtkey() {
        return mtkey;
    }

    SystemParam systemParam = new SystemParam(getMtappid(), getMtkey());
}
