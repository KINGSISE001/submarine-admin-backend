package com.htnova.mt.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.common.BizResultWrapper;
import com.corundumstudio.socketio.SocketIOClient;
import com.htnova.common.constant.EleStatus;
import com.htnova.common.constant.ResultStatus;
import com.htnova.common.dev.config.EleConfig;
import com.htnova.common.dto.Result;
import com.htnova.common.util.RedisUtil;
import com.htnova.common.util.SocketUtil;
import com.htnova.mt.order.entity.*;
import com.htnova.mt.order.service.OrderLogListService;
import com.htnova.mt.order.service.OrderService;
import com.htnova.mt.order.service.TMtDeliveryPersonnelService;
import com.htnova.mt.order.service.UserPoiService;
import lombok.extern.slf4j.Slf4j;
import me.ele.retail.param.*;
import org.springframework.http.HttpStatus;
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

    @Resource
    private TMtDeliveryPersonnelService tMtDeliveryPersonnelService;


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

   public Result orderConfirm(String orderId){
       String appkey = el.getAppkey();
       String secKey = el.getSecKey();
       ApiExecutor apiExecutor = new ApiExecutor<>(appkey, secKey);
       OrderConfirmParam param = new OrderConfirmParam();
       param.setTicket(UUID.randomUUID().toString().toUpperCase());
       MeEleRetailOrderConfirmInputParam body
               = new MeEleRetailOrderConfirmInputParam();
       body.setOrder_id(orderId);
       param.setBody(body);
       try {
           BizResultWrapper result = apiExecutor.send(param);
           System.out.println("Result:" +
                   JSON.toJSONString(result));
           JSONObject resultJSON = JSON.parseObject(JSON.toJSONString(result));
           JSONObject JSONbody = resultJSON.getJSONObject("body");
           Boolean JSONdata = JSONbody.getBooleanValue("data");
           if (!(String.valueOf(JSONbody.getString("errno")).equals("0"))) {
               System.out.println("--请求结果失败--");
               return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR, JSONdata);
           } else {
               return Result.build(HttpStatus.OK, ResultStatus.REQUEST_SUCCESS, JSONdata);
           }
       } catch (Exception var8) {
           System.out.println("请求失败，请求异常");
           System.out.println(var8);
       }
       return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR,"未知错误");
   };


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
                logListStatus(Long.parseLong(data.getOrder().getOrder_id()), data.getShop().getBaidu_shop_id(), ResultStatus.ZHI_FU_ORDER, "已支付订单");
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
                updateEleOrderStatus(Long.parseLong(order_id), platform_shop_id, ResultStatus.DAI_JIE_ORDER, "待确认");
                break;
            case 5:
                log.info("订单状态为订单已确认");
                updateEleOrderStatus(Long.parseLong(order_id), platform_shop_id, ResultStatus.QUE_DING_ORDER, "已确认");
                break;
            case 7:
                log.info("订单状态为骑士已接单开始取餐");
                updateEleOrderStatus(Long.parseLong(order_id), platform_shop_id, ResultStatus.DAI_PEI_SONG_ORDER, "骑士已接单开始取餐");
                break;
            case 8:
                log.info("订单状态为骑士已取餐正在配送");
                updateEleOrderStatus(Long.parseLong(order_id), platform_shop_id, ResultStatus.PEI_SONG_ZHONG_ORDER, "已取餐正在配送");
                orderService.updatePickingStatus(Long.valueOf(order_id), 1);
                break;
            case 9:
                log.info("订单状态为订单完成");
                updateEleOrderStatus(Long.parseLong(order_id), platform_shop_id, ResultStatus.WAN_CHENG_ORDER, "订单完成");
                break;
            case 10:
                log.info("订单状态为订单取消");
                updateEleOrderStatus(Long.parseLong(order_id), platform_shop_id, ResultStatus.YI_QU_XIAO_ORDER, "订单取消");
                break;
            default:
                log.info("订单状态为其他");
        }

    }

    /**
     * 运单状态。 目前推送的状态有：0:无配送状态；1:待请求配送;2:生成运单; 3:请求配送; 4:等待分配骑士;7:骑士接单;8:骑士取餐;15:配送取消;16:配送完成;17:配送异常;18:自行配送;19:不再配送;20:配送拒单;21:骑士到店。
     * 半日达专用物流状态：30:生成物流单;31:物流接单;32:物流分配运力;33:开始揽收;34:物流已揽收;35:物流到达站点;36:物流开始配送;37:物流送达;38:物流失败。
     * 快递发货专用物流状态：100:商家已接单;101:商家拣货完成;102:商家已发货;103:快递部分揽收;104:快递全部揽收;105:快递中转;106:开始配送;107:部分签收;108:全部签收;109:部分拒收;110:全部拒收;111:物流取消;112:商家发货异常（超时）;113:商家发货失败（底层失败）。
     *
     * @param body
     */
    public void deliveryStatusPush(JSONObject body) {
        String order_id = body.getString("order_id");
        String eleId = body.getString("platform_shop_id");
        Integer status = body.getInteger("status");
        switch (status) {
            case 1: //待请求配送
                log.info("订单状态为待请求配送");
                logListStatus(Long.parseLong(order_id), eleId, ResultStatus.DAI_PEI_SONG_ORDER, "待请求配送");
                break;
            case 2: //生成运单
                log.info("订单状态为生成运单");
                logListStatus(Long.parseLong(order_id), eleId, ResultStatus.DAI_PEI_SONG_ORDER, "生成运单");
                break;
            case 3:  //请求配送
                log.info("订单状态为请求配送");
                break;
            case 4: //等待分配骑士
                log.info("订单状态为等待分配骑士");
                logListStatus(Long.parseLong(order_id), eleId, ResultStatus.DAI_PEI_SONG_ORDER, "等待分配骑士");
                break;
            case 7: //骑士接单
                log.info("订单状态为骑士接单");
                getOrderRefundDelivery(order_id, eleId);
                logListStatus(Long.parseLong(order_id), eleId, ResultStatus.DAI_PEI_SONG_ORDER, "骑士接单");
                break;
            case 8: //骑士取餐
                log.info("订单状态为骑士取餐");
                break;
            case 15: //配送取消
                logListStatus(Long.parseLong(order_id), eleId, ResultStatus.PEI_SONG_YI_CHANG_ORDER, "配送取消");
                log.info("订单状态为配送取消");
                break;
            case 16: //配送完成
                log.info("订单状态为配送完成");
                getOrderRefundDelivery(order_id, eleId);
                logListStatus(Long.parseLong(order_id), eleId, ResultStatus.WAN_CHENG_ORDER, "配送完成");
                break;
            case 17: //配送异常
                log.info("订单状态为配送异常");
                logListStatus(Long.parseLong(order_id), eleId, ResultStatus.PEI_SONG_YI_CHANG_ORDER, "配送取消");
                break;
            case 18: //自行配送
                log.info("订单状态为自行配送");
                logListStatus(Long.parseLong(order_id), eleId, ResultStatus.PEI_SONG_ZHONG_ORDER, "自行配送");
                break;
            case 19: //不再配送
                log.info("订单状态为不再配送");
                logListStatus(Long.parseLong(order_id), eleId, ResultStatus.PEI_SONG_YI_CHANG_ORDER, "不再配送");
                break;
            case 20: //配送拒单
                log.info("订单状态为配送拒单");
                logListStatus(Long.parseLong(order_id), eleId, ResultStatus.PEI_SONG_YI_CHANG_ORDER, "配送拒单");
                break;
            case 21: //骑士到店
                log.info("订单状态为骑士到店");
                getOrderRefundDelivery(order_id, eleId);
                break;
            default:
                log.info("订单状态为其他");
                logListStatus(Long.parseLong(order_id), eleId, ResultStatus.PEI_SONG_YI_CHANG_ORDER, "订单状态为其他");
                break;
        }

    }

    /**
     * 订单催单
     *
     * @param body body
     */
    public void orderRemind(JSONObject body) {
        String order_id = body.getString("order_id");
        String eleId = body.getString("platform_shop_id");
        // String create_time = body.getString("create_time");
        updateEleOrderStatus(Long.parseLong(order_id), eleId, ResultStatus.CUI_DAN, "客户催单了");
    }

    /**
     * 此接口主要用于订单退款/退货场景。当订单发生退款/退货时，平台会推送退款/退货订单信息给合作方。
     * 退款消息仅返回订单退款关键信息，详细订单退款信息、退商品信息、是否部分/全单退、退款/退货请调用order.reverse.query查询接口获取
     *
     * @param body body
     */
    public void orderReverse(JSONObject body) {
        System.err.println(JSON.toJSONString(body));
        String platform_shop_id = body.getString("platform_shop_id");
        String order_id = body.getString("order_id");//正常单号
        String refund_order_id = body.getString("refund_order_id");//逆向单号
        String cur_reverse_event = body.getString("cur_reverse_event");
        log.error(JSON.toJSONString(cur_reverse_event));
        CurReverseEvent curReverseEvent = JSON.parseObject(JSON.parseObject(cur_reverse_event).toJSONString(), CurReverseEvent.class);
        //逆向单操作前的逆向单状态：0-初始化，10-申请，20-拒绝，30-仲裁，40-关闭，50-成功，60-失败
        //https://open-retail.ele.me/#/msgdoc/detail?topicName=order.reverse.push&aopApiCategory=order_msg_group&type=push_menu
       String Operator_role ="";
        switch (curReverseEvent.getOperator_role()) { //逆向单操作者角色：10 用户 ,20商户,30客服 ,25 API商家代客发起,40系统
            case 10:
                log.info("用户发起逆向单");
                Operator_role="用户";
                break;
            case 20:
                log.info("商户发起逆向单");
                Operator_role="商户";
                break;
            case 30:
                log.info("客服发起逆向单");
                Operator_role="客服";
                break;
            case 25:
                log.info("API商家代客发起逆向单");
                Operator_role="API商家代客";
                break;
            case 40:
                log.info("系统发起逆向单");
                Operator_role="系统";
                break;
            default:
                log.info("其他操作者发起逆向单");
                Operator_role="其他操作者";
                break;
        }
        String reason_content = EleStatus.getValue(curReverseEvent.getReason_code()); //逆向单操作原因对应
        String last_refund_content =get_refund_status(curReverseEvent.getLast_refund_status());//逆向单操作前的逆向单状态
        String refund_content = get_refund_status(curReverseEvent.getRefund_status());//逆向单操作后的逆向单后状态
        // String last_return_goods_status =get_goods_status(curReverseEvent.getLast_return_goods_status());//逆向单操作前的退货状态
        // String return_goods_status= get_goods_status(curReverseEvent.getReturn_goods_status());//逆向单操作后的退货状态
        StringBuilder s = new StringBuilder();
        s.append(Operator_role)
                .append(",操作前状态：")
                .append(last_refund_content)
                .append("，")
                .append("操作后状态：")
                .append(refund_content)
                .append("，")
                .append("操作原因:")
                .append(reason_content)
                .append(curReverseEvent.getReason_content());
        log.info("订单退款/退货：{}",s.toString());
        updateEleOrderStatus(Long.parseLong(order_id),platform_shop_id, ResultStatus.TUI_DAN, s.toString());
    }

    public void getOrderRefundDelivery(String orderId, String eleId) {
        String appkey = el.getAppkey();
        String secKey = el.getSecKey();
        ApiExecutor apiExecutor = new ApiExecutor<>(appkey, secKey);
        OrderDeliveryGetParam param = new OrderDeliveryGetParam();
        param.setTicket(UUID.randomUUID().toString().toUpperCase());
        MeEleNopDoaApiParamRequestOrderOrderCmdBaseReqDtoOrderId body
                = new MeEleNopDoaApiParamRequestOrderOrderCmdBaseReqDtoOrderId();
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
                MeEleNopDoaApiParamRequestOfcDeliveryInfoResultDataDto data = JSON.parseObject(JSONdata.toJSONString(), MeEleNopDoaApiParamRequestOfcDeliveryInfoResultDataDto.class);
                TMtDeliveryPersonnel dp = new TMtDeliveryPersonnel();
                dp.setOrderId(orderId);
                dp.setDispatcherName(data.getName());
                dp.setDispatcherMobile(data.getPhone());
                dp.setAppId(appkey);
                dp.setTime(data.getUpdate_time());
                dp.setTimestamp(data.getUpdate_time());
                dp.setAppPoiCode(eleId);
                dp.setLogisticsStatus(String.valueOf(data.getStatus()));
                tMtDeliveryPersonnelService.saveOrUpdate(dp);
            }
        } catch (Exception e) {
            log.error("异常【{}】", e.getMessage());
        }
    }

    ;

    public void updateEleOrderStatus(long order_id, String eleId, ResultStatus Status, String Content) {
        orderService.updateStatus(order_id, String.valueOf(Status.getCode()));
        logListStatus(order_id, eleId, Status, Content);
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
            log.error("userID:{}", user.getuId());
            List<SocketIOClient> list1 = SocketUtil.getClientList(user.getuId());
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

    /**
     * 逆向单操作后的退货状态（即当前的退货状态）：0-无效状态，1001-买家已申请退货，等待审批处理，1002-商家拒绝退货申请，1003-退货仲裁已发起，客服介入中，
     * 1004-已同意退货，等待发货，1005-已发货，等待卖家确认收货，1006-已收到货，并同意退款， 1007-未收到货，不同意退款，1008-退货关闭
     *
     * @param code code
     */
    public String get_goods_status(int code) {
        switch (code) {
            case 0:
                return "无效状态";
            case 1001:
                return "买家已申请退货";
            case 1002:
                return "商家拒绝退货申请";
            case 1003:
                return "退货仲裁已发起，客服介入中";
            case 1004:
                return "已同意退货，等待发货";
            case 1005:
                return "已发货，等待卖家确认收货";
            case 1006:
                return "已收到货，并同意退款";
            case 1007:
                return "未收到货，不同意退款";
            case 1008:
                return "退货关闭";
            default:
                return "未知状态";
        }


    }


    /**
     * 逆向单操作后的逆向单后状态（即当前的逆向单状态）：0-初始化，10-申请，20-拒绝，30-仲裁，40-关闭，50-成功，60-失败
     */
    public String get_refund_status(int code) {
        switch (code) {
            case 0:
                return "初始化";
            case 10:
                return "申请";
            case 20:
                return "拒绝";
            case 30:
                return "仲裁";
            case 40:
                return "关闭";
            case 50:
                return "成功";
            case 60:
                return "失败";
            default:
                return "未知状态";
            }
        }
}

