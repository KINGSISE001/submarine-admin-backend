package com.htnova.mt.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.common.BizResultWrapper;
import com.corundumstudio.socketio.SocketIOClient;
import com.htnova.common.constant.ResultStatus;
import com.htnova.common.dev.config.EleConfig;
import com.htnova.common.util.RedisUtil;
import com.htnova.common.util.SocketUtil;
import com.htnova.mt.order.entity.Completedorder;
import com.htnova.mt.order.entity.Detail;
import com.htnova.mt.order.entity.OrderLogList;
import com.htnova.mt.order.entity.UserPoi;
import com.htnova.mt.order.service.OrderLogListService;
import com.htnova.mt.order.service.OrderService;
import com.htnova.mt.order.service.UserPoiService;
import lombok.extern.slf4j.Slf4j;
import me.ele.retail.param.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service(value = "meEleService")
public class MeEleServiceImpl {

    @Resource
    EleConfig el;
    @Resource
    OrderLogListService orderLogListService;
    @Resource
    private OrderService orderService;
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserPoiService userPoiService;


    /**
     * 获取饿了么订单
     */
    public void getEleOrder(String orderId) {
        String appkey = el.getAppkey();
        String secKey = el.getSecKey();
        ApiExecutor apiExecutor = new ApiExecutor<>(appkey, secKey);
        OrderGetParam param = new OrderGetParam();
        param.setTicket(UUID.randomUUID().toString().toUpperCase());
        MeEleRetailOrderGetReqDto body
                = new MeEleRetailOrderGetReqDto();
        body.setOrder_id(orderId);
        param.setBody(body);
        try {
            BizResultWrapper result = apiExecutor.send(param);
            System.out.println("Result:" +
                    JSON.toJSONString(result));
            JSONObject resultJSON = JSON.parseObject(JSON.toJSONString(result));
            JSONObject JSONbody = resultJSON.getJSONObject("body");
            JSONObject JSONdata = JSONbody.getJSONObject("data");
            if (!(String.valueOf(JSONbody.getString("errno")).equals("0"))) {
                System.out.println("--请求结果失败--");
            } else {
                System.out.println("请求成功");
                MeEleNopDoaApiDtoOrderGetOrderGetDataResultDataDto data = JSON.parseObject(JSONdata.toJSONString(), MeEleNopDoaApiDtoOrderGetOrderGetDataResultDataDto.class);
                insertOrders(data);
            }
        } catch (Exception var8) {
            System.out.println("请求失败，请求异常");
            System.out.println(var8);
        }
    }


    public void insertOrders(MeEleNopDoaApiDtoOrderGetOrderGetDataResultDataDto data) {
        if (data != null) {
            Completedorder o = new Completedorder();
            o.setOrderId(Long.valueOf(data.getOrder().getOrder_id()));
            o.setWmPoiName(data.getShop().getName());
            o.setAppPoiCode(data.getShop().getBaidu_shop_id());
            o.setDaySeq(data.getOrder().getOrder_index());
            o.setCaution(data.getOrder().getRemark() + "\n收货人隐私号:" + data.getUser().getPhone() + "，手机号:" + data.getUser().getPrivacy_phone());
            o.setRecipientAddress(data.getUser().getAddress());
            o.setRecipientName(data.getUser().getName());
            o.setRecipientPhone(data.getUser().getPhone());
            o.setBackupRecipientPhone(data.getUser().getPrivacy_phone());
            MeEleNopDoaApiDtoOrderGetDiscount[] discounts = data.getDiscount();
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            o.setDate(ft.format(dNow));
            o.setTimeMillis(dNow.getTime());
            int reduce_fee = 0;
            StringBuilder remark = new StringBuilder();
            for (MeEleNopDoaApiDtoOrderGetDiscount discount : discounts) {
                reduce_fee += discount.getFee();
                remark.append("\n\r").append(discount.getDesc()).append("-").append((double) discount.getFee() / 100).append(";");
            }
            double fee = (double) reduce_fee / 100;
            double commission = (double) data.getOrder().getCommission() / 100;
            String estraString = "共减去:" + fee + "元,\n分别为:" + remark + "\r佣金:" + commission + "元(佣金（费用包含佣金（技术服务费）+履约服务费+支付服务费）)";
            o.setExtras(estraString);
            o.setDetail("饿了么");
            o.setStatus(String.valueOf(ResultStatus.ZHI_FU_ORDER.getCode()));
            o.setShippingFee((float) fee);
            o.setTotal((double) (data.getOrder().getTotal_fee() / 100));
            o.setOriginalPrice((double) (data.getOrder().getUser_fee() / 100));
            int tag = data.getOrder().getIs_prescription();
            if (tag == 1) {
                o.setOrderTagList("处方药");
            }
            if (tag == 0) {
                o.setOrderTagList("普通单");
            }
            List<Detail> lists = new ArrayList<>();
            for (MeEleNopDoaApiDtoOrderGetProduct product : data.getProducts()[0]) {
                Detail detail = new Detail();
                detail.setOrderId(Long.valueOf(data.getOrder().getOrder_id()));
                detail.setAppFoodCode(product.getCustom_sku_id());
                detail.setAppMedicineCode(product.getCustom_sku_id());
                detail.setUpc(product.getUpc());
                detail.setDetailExtra(JSON.toJSONString(product.getProduct_features()));
                detail.setFoodName(product.getProduct_name());
                detail.setQuantity(product.getProduct_amount());
                detail.setOriginalPrice(Double.valueOf(product.getProduct_price()) / 100);
                //商品总优惠金额，单位：分，计算公式：discount= agent_rate+baidu_rate+logistics_rate+shop_rate+user_rate
                detail.setActualPrice((double) (product.getProduct_price() - product.getProduct_subsidy().getDiscount()) / 100);
                detail.setPrice((double) (product.getProduct_price() - product.getProduct_subsidy().getDiscount()) / 100);
                lists.add(detail);
            }
            int count1 = orderService.insertCompletedorder(o);
            if (count1 > 0) {
                logListStatus(Long.parseLong(data.getOrder().getOrder_id()),data.getShop().getBaidu_shop_id(),ResultStatus.ZHI_FU_ORDER,"已支付订单");
                log.info("订单插入成功");
            }
            int count2 = orderService.insertDetail(lists);
            if (count2 > 0) {
                log.info("订单详情插入成功");
            }

        }
    }


    public void updateOrderStatus(JSONObject body) {
        //订单状态,目前推送的状态有：1、待确认；
        // 5、订单已确认(订单已接单)；
        // 7、骑士已接单开始取餐（此时可通过订单详情接口获取骑士手机号);
        // 8、骑士已取餐正在配送;
        // 9、订单完成;
        // 10、订单取消。推送地址和创建订单地址相同。
        String order_id = body.getString("order_id");
        String platform_shop_id = body.getString("platform_shop_id");
        Integer status = body.getInteger("status");
        switch (status) {
            case 1:
                log.info("订单状态为待确认");
                updateEleOrderStatus(Long.valueOf(order_id),platform_shop_id, ResultStatus.DAI_JIE_ORDER,"待确认");
                break;
            case 5:
                log.info("订单状态为订单已确认");
                updateEleOrderStatus(Long.valueOf(order_id),platform_shop_id, ResultStatus.QUE_DING_ORDER,"已确认");
                break;
            case 7:
                log.info("订单状态为骑士已接单开始取餐");
                updateEleOrderStatus(Long.valueOf(order_id),platform_shop_id, ResultStatus.DAI_PEI_SONG_ORDER,"骑士已接单开始取餐");
                break;
            case 8:
                log.info("订单状态为骑士已取餐正在配送");
                updateEleOrderStatus(Long.valueOf(order_id),platform_shop_id, ResultStatus.PEI_SONG_ZHONG_ORDER,"已取餐正在配送");
                orderService.updatePickingStatus(Long.valueOf(order_id), 1);
                break;
            case 9:
                log.info("订单状态为订单完成");
                updateEleOrderStatus(Long.valueOf(order_id),platform_shop_id, ResultStatus.WAN_CHENG_ORDER,"订单完成");
                break;
            case 10:
                log.info("订单状态为订单取消");
                updateEleOrderStatus(Long.valueOf(order_id),platform_shop_id, ResultStatus.YI_QU_XIAO_ORDER,"订单取消");
                break;
            default:
                log.info("订单状态为其他");
        }

    }

    public void updateEleOrderStatus(long order_id, String eleId, ResultStatus Status, String Content) {
        orderService.updateStatus(order_id, String.valueOf(Status.getCode()));
        logListStatus(order_id,eleId,Status,Content);
    }


    public void logListStatus(long order_id, String eleId, ResultStatus Status, String Content) {
        Date date3 = DateUtil.date(System.currentTimeMillis());
        OrderLogList or = new OrderLogList();
        or.setOrderId(order_id);
        or.setTitle(Status.getMsg());
        or.setContent(Content);
        or.setCode(Status.getCode());
        or.setCreatedTime(date3);
        orderLogListService.saveOrderLogList(or);
        UserPoi user = getUserId2(eleId);
        if (!(user == null)) {
            List<SocketIOClient> list1 = SocketUtil.getClientList(user.getUId());
            if (CollUtil.isNotEmpty(list1)) {
                list1.forEach(
                        item -> {
                            Map<String, Object> info = new LinkedHashMap<>();
                            info.put("orderId", order_id);
                            info.put("WmPoiName", user.getEleName());
                            info.put("Msg", Status.getMsg());
                            info.put("Content", Content);
                            info.put("StatusCode", Status.getCode());
                            info.put("PoiCode", eleId);
                            item.sendEvent("welcome", JSON.toJSONString(info));
                        }
                );
            }
        }
    }


    public UserPoi getUserId2(String eleId) {

        log.info("redisUtil.hasKey:{}", redisUtil.hasKey(eleId));
        if (redisUtil.hasKey(eleId)) {
            log.info("redisUtil.hasKey:{}", JSON.toJSONString(redisUtil.get(eleId)));
            return (UserPoi) redisUtil.get(eleId);
        } else {
            return setWorkshopPersonToProcess(eleId);
        }
    }

    public UserPoi setWorkshopPersonToProcess(String eleId) {
        List<UserPoi> list = userPoiService.getUserEleById(eleId);
        if (list.size() > 0) {
            UserPoi userPoi = list.stream().findFirst().orElse(new UserPoi());
            log.info("userPoi:{}", JSON.toJSONString(userPoi));
            redisUtil.set(eleId, userPoi);
        }
        return null;
    }

}

