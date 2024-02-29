package com.htnova.mt.order.controller;

import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.htnova.common.constant.ResultStatus;
import com.htnova.common.dto.JdResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("jd")
public class JdDaoJiaController {
    public static Map<String, Object> getUrlParams(String param) {
        Map<String, Object> map = new HashMap<>(0);
        if (Strings.isBlank(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (String s : params) {
            String[] p = s.split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }
    // http://raphz6beo8jq.hk1.xiaomiqiu123.top/api/jd/token
    @PostMapping("token")
    @ResponseBody
    public JdResult<Void> token(@RequestBody String body) {
        Map<String, Object> map = getUrlParams(body);
       JSONObject jsonObject = JSON.parseObject(URLUtil.decode((String) map.get("token")));
       log.info("body:{}", jsonObject.toJSONString());
       return JdResult.build();
    }
    // http://raphz6beo8jq.hk1.xiaomiqiu123.top/api/jd/messages
    @GetMapping("messages")
    @ResponseBody
    public JdResult<Object> messages(@RequestParam(value ="echostr" ) String body) {
        Map<String, Object> map = getUrlParams(body);
        map.forEach((k, v) -> log.info("key:{} value:{}", k, v));
        log.info("body:{}", JSON.toJSONString(body));
        return JdResult.build(ResultStatus.REQUEST_SUCCESS,body);
    }
}
