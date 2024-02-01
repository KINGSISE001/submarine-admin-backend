package com.htnova.mt.order.controller;


import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.htnova.common.constant.ResultStatus;
import com.htnova.common.dto.EleResult;
import com.htnova.mt.order.mapper.CompletedorderMapper;
import com.htnova.mt.order.service.impl.MeEleServiceImpl;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@Api(value = "饿了么订单处理相关接口")
@RequestMapping("/ele")
public class EleOrderController {

    @Resource
    MeEleServiceImpl meEleService;

    @Resource
    CompletedorderMapper completedorderMapper;

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
        log.info("-----------------------开始-------------------------------");
        Map<String, Object> map = getUrlParams(body);
        String cmd = (String) map.get("cmd");
        JSONObject JsonBody = JSON.parseObject(JSON.toJSONString(JSON.parse(URLUtil.decode((String) map.get("body")))));
        if (JsonBody != null) {
            String order_id = (String) JsonBody.get("order_id");
            switch (cmd) {
                case "order.create":
                    log.info("ele:【订单创建通知】 : {}", order_id);
                    if (completedorderMapper.selectById(order_id) == null){
                        meEleService.getEleOrder(order_id);
                    }
                    return EleResult.ok(order_id);
                case "order.deliveryStatus.push":
                    meEleService.deliveryStatusPush(JsonBody);
                    log.info("ele:【订单物流状态推送】 : {}", order_id);
                    return EleResult.ok(order_id);
                case "order.status.push":
                    meEleService.updateOrderStatus(JsonBody);
                    log.info("ele:【订单状态推送】 : {}", order_id);
                    return EleResult.ok(order_id);
                case "order.remind.push":
                    meEleService.orderRemind(JsonBody);
                    log.info("ele:【订单催单通知】 : {}", order_id);
                    return EleResult.ok(order_id);
                case "order.reverse.push":
                    meEleService.orderReverse(JsonBody);
                    log.info("ele:【用户申请订单取消/退款】 : {}", order_id);
                    return EleResult.ok(order_id);
                case "order.partrefund.push":

                    log.info("ele:【订单部分退款推送】 : {}", order_id);
                    return EleResult.ok(order_id);
                case "sku.update.push":
                    log.info("ele:【商品库存更新推送】 : {}", order_id);
                    return EleResult.ok(order_id);
                default:
                    log.info("ele:【其他】 : {}", cmd);
                    return EleResult.ok(order_id);
            }
        }else {
            log.info("-----------------------结束---------------------------------");
            return EleResult.build(ResultStatus.SERVER_ERROR);
        }

    }

}
