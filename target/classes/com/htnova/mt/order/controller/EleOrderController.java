package com.htnova.mt.order.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.htnova.common.dto.EleResult;
import com.htnova.mt.order.service.impl.MeEleServiceImpl;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@Api(value = "饿了么订单处理相关接口")
@RequestMapping("/ele")
public class EleOrderController {

    @Resource
    MeEleServiceImpl meEleService;

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

    @PostMapping("/order")
    @ResponseBody
    public EleResult ele(@RequestBody String body) {
        Map<String, Object> map = getUrlParams(body);
        String cmd = (String) map.get("cmd");
        map.forEach((k, v) -> {
            log.info("ele:【{}】 : {}", k, v);
        });
        JSONObject JsonBody = JSON.parseObject(JSON.toJSONString(JSON.parse(URLUtil.decode((String) map.get("body")))));
        if (JsonBody != null) {
            String order_id = (String) JsonBody.get("order_id");
            switch (cmd) {
                case "order.create":
                    meEleService.getEleOrder(order_id);
                    log.info("ele:【订单创建通知】 : {}", order_id);

                    break;
                case "order.deliveryStatus.push":

                    log.info("ele:【订单物流状态推送】 : {}", order_id);
                    break;
                case "order.status.push":
                    meEleService.updateOrderStatus(JsonBody);
                    log.info("ele:【订单状态推送】 : {}", order_id);
                    break;
                case "order.remind.push":

                    log.info("ele:【订单催单通知】 : {}", order_id);
                    break;
                case "order.user.cancel":

                    log.info("ele:【用户申请订单取消/退款】 : {}", order_id);
                    break;

                case "order.partrefund.push":

                    log.info("ele:【订单部分退款推送】 : {}", order_id);
                    break;

                case "sku.update.push":
                    log.info("ele:【商品库存更新推送】 : {}", order_id);

                    break;
                default:

                    break;
            }
            log.info("\n-----------------------开始-------------------------------");
            System.err.println(JSON.toJSONString(JsonBody));
            log.info("\n-----------------------结束---------------------------------");
            return EleResult.ok(order_id);
        }
        return EleResult.ok();
    }

}
