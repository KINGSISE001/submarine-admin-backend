package com.htnova.mt.order.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.htnova.common.constant.ResultStatus;
import com.htnova.common.dto.EasyUIDataGridResult;
import com.htnova.common.dto.MtResult;
import com.htnova.common.meituan.MeiTuanUtil;
import com.htnova.common.util.RedisUtil;
import com.htnova.common.util.SocketUtil;
import com.htnova.mt.order.entity.*;
import com.htnova.mt.order.mapper.TMtDeliveryPersonnelMapper;
import com.htnova.mt.order.service.OrderLogListService;
import com.htnova.mt.order.service.OrderService;
import com.htnova.mt.order.service.UserPoiService;
import com.htnova.mt.order.service.impl.TMtDeliveryPersonnelServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RequestMapping("order")
@Api(value = "美团订单相关")
@Controller
public class OrderController {
    @Resource
    private OrderService orderService;

    @Resource
    private OrderLogListService orderLogListService;

    @Resource
    private TMtDeliveryPersonnelServiceImpl tMtDeliveryPersonnelService;

    @Resource
    private UserPoiService userPoiService;

    @Resource
    private TMtDeliveryPersonnelMapper tMtDeliveryPersonnelMapper;

    @Resource
    private MeiTuanUtil mt;

    @Resource
    private RedisUtil redisUtil;



    @ApiIgnore
    @ApiOperation(value = "已支付订单接收")
    @PostMapping(value = "/paidOrder")
    @ResponseBody
    public MtResult PaidOrder(HttpServletRequest request) throws Exception {
        Map<Object, Object> map = objectObjectMap(request);
        if (map.size() == 0) {
            System.err.println("Completed:TEST!");
            return MtResult.ok();
        }
        // 将 Map 转换为 实体类
        Completedorder completedorder = JSON.parseObject(JSON.toJSONString(map), Completedorder.class);
        List<Detail> lists = new ArrayList<>();
        JSONArray detailJsonArray = JSONArray.parseArray(completedorder.getDetail());
        for (int i = 0; i < detailJsonArray.size(); i++) {
            JSONObject jsonObject = detailJsonArray.getJSONObject(i);
            Detail detail = JSON.parseObject(jsonObject.toJSONString(), Detail.class);
            detail.setOrderId(completedorder.getOrderId());
            lists.add(detail);
        }
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        completedorder.setDate(ft.format(dNow));
        completedorder.setTimeMillis(dNow.getTime());
        float reduce_fee = 0;
        StringBuilder remark = new StringBuilder();
        JSONArray extras = JSONArray.parseArray(completedorder.getExtras());
        if (extras.size() > 0) {
            for (int i = 0; i < extras.size(); i++) {
                JSONObject activityDetail = extras.getJSONObject(i);
                reduce_fee += activityDetail.getFloatValue("reduce_fee");
                remark.append(activityDetail.getString("remark")).append(";");
            }
        }
        String estraString = "共减去:" + reduce_fee + "元,\n分别为:" + remark;
        completedorder.setExtras(estraString);
        completedorder.setDetail("美团");
        completedorder.setStatus(String.valueOf(ResultStatus.ZHI_FU_ORDER.getCode()));
       String orderTagList = completedorder.getOrderTagList();
        Integer[] orderTagLists = JSON.parseObject(orderTagList, Integer[].class);
        for (Integer tag : orderTagLists) {
            log.info("tag:{}",tag);
        }
        completedorder.setOrderTagList("普通单");
           for (Integer tag : orderTagLists) {
               if (tag == 8){
                   completedorder.setOrderTagList("处方药");
               }
               if (tag == 23){
                   completedorder.setOrderTagList("医保药");
               }
               if (tag == 36){
                   completedorder.setOrderTagList("好药订单");
               }
           }
        int count1 = orderService.insertCompletedorder(completedorder);
        int count2 = orderService.insertDetail(lists);
        if (count1 > 0 && count2 > 0) {
            updateStatus(request, ResultStatus.DAI_JIE_ORDER, "已支付待接订单");
            //自动接单（0接单，1不接单）
            log.info("JSON:{}", JSON.toJSONString(getUserId2(completedorder.getAppPoiCode())));

            UserPoi user = getUserId2(completedorder.getAppPoiCode());
            if (!(user == null)) {
                int id = user.getAutoOrders();
                if (id == 0) {
                    mt.orderConfirm(String.valueOf(completedorder.getOrderId()));
                }
            }
            return MtResult.ok();
        } else {
            return MtResult.err(400, "err");
        }
    }

    /*
     * 已确定订单produces = MediaType.APPLICATION_JSON_VALUE,@RequestBody String string,
     */
    @ApiIgnore
    @ApiOperation(value = "已确定订单接收")
    @PostMapping(value = "/Confirmed")
    @ResponseBody
    public MtResult Confirmed(HttpServletRequest request) throws Exception {
        return updateStatus(request, ResultStatus.QUE_DING_ORDER, "确定订单");
    }


    /*
     * 催 单
     * cancel order,
     */
    @ApiIgnore
    @PostMapping(value = "/reminderOrder")
    @ResponseBody
    public MtResult reminderOrder(HttpServletRequest request) throws Exception {
        Map<Object, Object> map = objectObjectMap(request);
        if (map.size() == 0) {
            System.err.println("Completed:TEST!");
            return MtResult.ok();
        }
        Completedorder completedorder = JSON.parseObject(JSON.toJSONString(map), Completedorder.class);
        logListStatus(completedorder, ResultStatus.CUI_DAN, "催单");
        return MtResult.ok();
    }


    /*
     * 已完成订单
     */
    @ApiIgnore
    @ApiOperation(value = "已完成订单接收")
    @PostMapping(value = "/Completed")
    @ResponseBody
    public MtResult Completed(HttpServletRequest request) throws Exception {
        return updateStatus(request, ResultStatus.WAN_CHENG_ORDER, "完成订单");
    }

    /*
     * 取消订单cancel order,
     */
    @ApiIgnore
    @ApiOperation(value = "取消订单")
    @PostMapping(value = "/cancelOrder")
    @ResponseBody
    public MtResult CancelOrder(HttpServletRequest request) throws Exception {
        Map<Object, Object> map = objectObjectMap(request);
        if (map.size() == 0) {
            System.err.println("Completed:TEST!");
            return MtResult.ok();
        }
        JSONObject json = JSON.parseObject(JSON.toJSONString(map));
        Long OrderId = json.getLong("order_id");
        String reason;
        String reasons = json.getString("reason");
        String app_poi_code = json.getString("app_poi_code");
        ResultStatus RS;
        int deal_op_type = Integer.parseInt(json.getString("deal_op_type"));//当前订单取消操作人类型， 1-用户 2-商家端 3-客服 4-BD 5-系统 6-开放平台
        switch (deal_op_type) {
            case 1:
                reason = "用户取消" + ":" + reasons;
                RS = ResultStatus.QU_XIAO_ORDER;
                break;
            case 2:
                reason = "商家取消" + ":" + reasons;
                RS = ResultStatus.QU_XIAO_ORDER;
                break;
            case 3:
                reason = "客服取消" + ":" + reasons;
                RS = ResultStatus.YI_QU_XIAO_ORDER;
                break;
            case 4:
                reason = "DB取消" + ":" + reasons;
                RS = ResultStatus.YI_QU_XIAO_ORDER;
                break;
            case 5:
                reason = "系统取消" + ":" + reasons;
                RS = ResultStatus.YI_QU_XIAO_ORDER;
                break;
            case 6:
                reason = "开放平台取消" + ":" + reasons;
                RS = ResultStatus.YI_QU_XIAO_ORDER;
                break;
            default:
                reason = "未知" + ":" + reasons;
                RS = ResultStatus.YI_QU_XIAO_ORDER;
                break;
        }
        orderService.updateStatus(OrderId, String.valueOf(RS.getCode()));
        Date date3 = DateUtil.date(System.currentTimeMillis());
        OrderLogList or = new OrderLogList();
        or.setOrderId(OrderId);
        or.setTitle(RS.getMsg());
        or.setContent(reason);
        or.setCode(RS.getCode());
        or.setCreatedTime(date3);
        orderLogListService.saveOrderLogList(or);
        UserPoi user = getUserId2(app_poi_code);
        if (!(user == null)) {
            List<SocketIOClient> list1 = SocketUtil.getClientList(user.getuId());
            if (CollUtil.isNotEmpty(list1)) {
                list1.forEach(
                        item -> {
                            Map<String, Object> info = new LinkedHashMap<>();
                            info.put("orderId", OrderId);
                            info.put("WmPoiName", app_poi_code);
                            info.put("Msg", reason);
                            info.put("Content", RS.getMsg());
                            info.put("StatusCode", RS.getCode());
                            info.put("PoiCode", app_poi_code);
                            item.sendEvent("welcome", JSON.toJSONString(info));
                        }
                );
            }
        }
        return MtResult.ok();
    }

    /*
     * 订单结算信息
     * Order settlement information...
     * produces = MediaType.APPLICATION_JSON_VALUE,@RequestBody String string,
     */
    @ApiIgnore
    @ApiOperation(value = "订单结算信息")
    @PostMapping(value = "/Settlement")
    @ResponseBody
    public GlobalResult Settlement(HttpServletRequest request) throws Exception {
        Map<Object, Object> map = objectObjectMap(request);
        if (map.size() == 0) {
            System.err.println("Settlement:TEST!");
            return GlobalResult.ok("ok");
        }
        SettlementInformation settlementinformation = JSON.parseObject(
                JSON.toJSON(map).toString(),
                SettlementInformation.class
        );
        JSONArray activityDetailsjsonArray = JSONArray.parseArray(settlementinformation.getActivitydetails());
        float chargeAmount = 0;
        StringBuilder chargeDesc = new StringBuilder();
        for (int i = 0; i < activityDetailsjsonArray.size(); i++) {
            JSONObject activityDetail = activityDetailsjsonArray.getJSONObject(i);
            chargeAmount += activityDetail.getFloatValue("chargeAmount");
            chargeDesc.append(activityDetail.getString("chargeDesc")).append(";");
        }
        settlementinformation.setActivitydetails("共减去:" + chargeAmount + "元,分别为:" + chargeDesc);
        int count = orderService.insertsettlementinformation(settlementinformation);
        if (count > 0) {
            return GlobalResult.ok("ok");
        } else {
            return GlobalResult.build(404, "Request failed");
        }
    }

    //
    /*
     * 订单部分退款信息
     * OrderRefundDetail
     * produces = MediaType.APPLICATION_JSON_VALUE,@RequestBody String string,
     */
    @ApiIgnore
    @ApiOperation(value = "订单部分退款信息接收")
    @PostMapping(value = "/PartialRefund")
    @ResponseBody
    public GlobalResult PartialRefund(HttpServletRequest request) throws Exception {
        Map<Object, Object> map = objectObjectMap(request);
        if (map.size() == 0) {
            System.err.println("Refund:TEST!");
            return GlobalResult.ok("ok");
        }
        /*
          {"reason":"不想要了 / 临时有事","incmp_code":"0","order_tag_list":"[]","is_appeal":"0","refund_id":"65295873034","pictures":"","res_type":"6","reason_code":"21111","sig":"f0c8c6aec3d7e49142846010dc0632a3","notify_type":"apply","app_poi_code":"5430_2701469","service_type":"0","res_reason":"商家开通极速退款服务，用户申请系统自动通过","ctime":"1702277776","incmp_modules":"[]","order_id":"2100849052260077854","app_id":"5430","timestamp":"1702277777"}
         */
        Long OrderId = Long.parseLong(map.get("order_id").toString());
        String app_poi_code = map.get("app_poi_code").toString();
        String food = map.get("food").toString();
        log.info("food:" + food);
        Completedorder completedorder = new Completedorder();
        completedorder.setAppPoiCode(app_poi_code);
        completedorder.setOrderId(OrderId);
        String reason;
        /*
          退款状态类型，参考值：0-等待处理中；1-商家驳回退款请求；2-商家同意退款；3-客服驳回退款请求；
          4-客服帮商家同意退款；5-超时未处理系统自动同意；6-系统自动确认；7-用户取消退款申请；8-用户取消退款申诉。
          支持退货退款业务的门店，订单退款消息中notify_type和res_type字段不返回，
          退款状态类型请参考status字段。未开通退货退款业务的门店，订单退款保持原逻辑不变
         */
        log.info("退款状态类型：" + map.get("res_type"));
        int res_type = Integer.parseInt((String)map.get("res_type") ) ;
        switch (res_type) {
            case 0:
                reason = "等待处理中";
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.TUI_DAN.getCode()));
                break;
            case 1:
                reason = "商家驳回退款请求";
                updateRefundStatus("0",food,OrderId);
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.QUE_DING_ORDER.getCode()));
                break;
            case 2:
                reason = "商家同意退款";
                updateRefundStatus("1",food,OrderId);
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.QU_XIAO_ORDER.getCode()));
                break;
            case 3:
                reason = "客服驳回退款请求";
                updateRefundStatus("0",food,OrderId);
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.QUE_DING_ORDER.getCode()));
                break;
            case 4:
                reason = "客服帮商家同意退款";
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.YI_QU_XIAO_ORDER.getCode()));
                updateRefundStatus("1",food,OrderId);
                break;
            case 5:
                reason = "超时未处理系统自动同意";
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.YI_QU_XIAO_ORDER.getCode()));
                updateRefundStatus("1",food,OrderId);
                break;
            case 6:
                reason = "系统自动确认";
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.YI_QU_XIAO_ORDER.getCode()));
                updateRefundStatus("1",food,OrderId);
                break;
            case 7:
                reason = "用户取消退款申请";
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.QUE_DING_ORDER.getCode()));
                updateRefundStatus("0",food,OrderId);
                break;
            case 8:
                reason = "用户取消退款申诉";
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.QUE_DING_ORDER.getCode()));
                updateRefundStatus("0",food,OrderId);
                break;
            default:
                reason = "未知";
        }
        reason = reason + "(" + map.get("reason").toString() + ")";
        logListStatus(completedorder, ResultStatus.TUI_KUAN, reason);
        return GlobalResult.ok("ok");
    }


    public void updateRefundStatus (String refundStatus,String food,Long orderId){
        JSONArray foods=JSONArray.parseArray(food);
        if (CollUtil.isNotEmpty(foods)) {
            for (int i = 0; i < foods.size(); i++) {
                JSONObject food1 = foods.getJSONObject(i);
                String mt_sku_id = food1.getString("mt_sku_id");
                String count=food1.getString("count");
                String total_refund_price=food1.getString("total_refund_price");
                orderService.updateRefundStatus( refundStatus,total_refund_price,Integer.parseInt(count), String.valueOf(orderId),mt_sku_id);
            }
        }


    };

    /*
     * 订单退款信息
     * OrderRefundDetail
     * produces = MediaType.APPLICATION_JSON_VALUE,@RequestBody String string,
     */
    @ApiIgnore
    @ApiOperation(value = "订单退款信息接收")
    @PostMapping(value = "/Refund")
    @ResponseBody
    public GlobalResult Refund(HttpServletRequest request) throws Exception {
        Map<Object, Object> map = objectObjectMap(request);
        if (map.size() == 0) {
            System.err.println("Refund:TEST!");
            return GlobalResult.ok("ok");
        }
        /*
          {"reason":"不想要了 / 临时有事","incmp_code":"0","order_tag_list":"[]","is_appeal":"0","refund_id":"65295873034","pictures":"","res_type":"6","reason_code":"21111","sig":"f0c8c6aec3d7e49142846010dc0632a3","notify_type":"apply","app_poi_code":"5430_2701469","service_type":"0","res_reason":"商家开通极速退款服务，用户申请系统自动通过","ctime":"1702277776","incmp_modules":"[]","order_id":"2100849052260077854","app_id":"5430","timestamp":"1702277777"}
         */
        Long OrderId = Long.parseLong(map.get("order_id").toString());
        String app_poi_code = map.get("app_poi_code").toString();
        Completedorder completedorder = new Completedorder();
        completedorder.setAppPoiCode(app_poi_code);
        completedorder.setOrderId(OrderId);
        String reason;
        /*
          退款状态类型，参考值：0-等待处理中；1-商家驳回退款请求；2-商家同意退款；3-客服驳回退款请求；4-客服帮商家同意退款；
          5-超时未处理系统自动同意；6-系统自动确认；7-用户取消退款申请；8-用户取消退款申诉。
         */
        log.info("退款状态类型：" + map.get("res_type"));
        int res_type = Integer.parseInt((String)map.get("res_type") ) ;
        switch (res_type) {
            case 0:
                reason = "等待处理中";
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.TUI_DAN.getCode()));
                break;
            case 1:
                reason = "商家驳回退款请求";
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.QUE_DING_ORDER.getCode()));
                break;
            case 2:
                reason = "商家同意退款";
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.QU_XIAO_ORDER.getCode()));
                break;
            case 3:
                reason = "客服驳回退款请求";
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.QUE_DING_ORDER.getCode()));
                break;
            case 4:
                reason = "客服帮商家同意退款";
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.YI_QU_XIAO_ORDER.getCode()));
                break;
            case 5:
                reason = "超时未处理系统自动同意";
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.YI_QU_XIAO_ORDER.getCode()));
                break;
            case 6:
                reason = "系统自动确认";
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.YI_QU_XIAO_ORDER.getCode()));
                break;
            case 7:
                reason = "用户取消退款申请";
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.QUE_DING_ORDER.getCode()));
                break;
            case 8:
                reason = "用户取消退款申诉";
                orderService.updateStatus(OrderId, String.valueOf(ResultStatus.QUE_DING_ORDER.getCode()));
                break;
            default:
                reason = "未知";
        }
        reason = reason + "(" + map.get("reason").toString() + ")";
        logListStatus(completedorder, ResultStatus.TUI_KUAN, reason);
        return GlobalResult.ok("ok");
    }

    /*
     * 拉取订单信息
     * Pull order
     * produces = MediaType.APPLICATION_JSON_VALUE,@RequestBody String string,
     */

    @ApiOperation(value = "门店历史订单信息查询", notes = "门店历史订单信息查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = " 当前页", paramType = "query", dataTypeClass = Integer.class, required = true),
            @ApiImplicitParam(name = "rows", value = "每页显示条数", paramType = "query", dataTypeClass = Integer.class, required = true),
            @ApiImplicitParam(name = "AppPoiCode", value = "门店ID", paramType = "query", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "start_time", value = "起始时间", paramType = "query", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "end_time", value = "结束时间", paramType = "query", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "status", value = "订单状态", paramType = "query", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "order_id", value = "订单号", paramType = "query", dataTypeClass = String.class),
    })
    @GetMapping(value = "/PullOrder")
    @ResponseBody
    public GlobalResult PullOrder(@RequestParam(value = "AppPoiCode") String AppPoiCode,
                                  @RequestParam(value = "order_id", required = false) String order_id,
                                  @RequestParam(value = "status", required = false) Integer status,
                                  @RequestParam(value = "start_time") String start_time,
                                  @RequestParam(value = "end_time") String end_time,
                                  @RequestParam(value = "page", defaultValue = "1") Integer page,
                                  @RequestParam(value = "rows", defaultValue = "20") Integer rows) {


        Date start_date = DateUtil.parse(start_time);
        Date end_date = DateUtil.parse(end_time);
        if (DateUtil.between(start_date, end_date, DateUnit.DAY) > 120) {
            return GlobalResult.build(201, "时间范围不能超过120天");
        }
        return GlobalResult.build(
                200,
                "ok",
                orderService.selectorderinformation(AppPoiCode, page, rows, start_time, end_time, status, order_id)
        );
    }

    /**
     * @return GlobalResult
     */
    @ApiIgnore
    @ApiOperation(value = "拣货完成")
    @PostMapping(value = "/PickingCompleted")
    @ResponseBody
    public GlobalResult PickingCompleted(HttpServletRequest request) throws Exception {
        Map<Object, Object> map = objectObjectMap(request);
        if (map.size() == 0) {
            System.err.println("Refund:TEST!");
            return GlobalResult.ok("ok");
        }
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
        JSONObject json = jsonObject.getJSONObject("pick_up_data");
        Completedorder completedorder = new Completedorder();
        completedorder.setAppPoiCode(json.get("app_poi_code").toString());
        completedorder.setOrderId(Long.parseLong(json.get("order_id").toString()));
        String reason = "拣货完成";
        logListStatus(completedorder, ResultStatus.JIAN_HUO, reason);
        orderService.updatePickingStatus(completedorder.getOrderId(), 1);
        orderService.updateStatus(Long.parseLong(json.get("order_id").toString()),String.valueOf(ResultStatus.DAI_PEI_SONG_ORDER.getCode()));
        return GlobalResult.ok("ok");
    }

    /*
     * 拉取订单信息
     * Pull order
     * produces = MediaType.APPLICATION_JSON_VALUE,@RequestBody String string,
     */
    @ApiIgnore
    @ApiOperation(value = "门店历史订单信息（废弃）", notes = "历史订单信息")
    @GetMapping(value = "/PullOrders", params = {"appPoiCode", "page", "rows"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = " 当前页", paramType = "query", dataTypeClass = Integer.class, required = true),
            @ApiImplicitParam(name = "rows", value = "每页显示条数", paramType = "query", dataTypeClass = Integer.class, required = true),
            @ApiImplicitParam(name = "AppPoiCode", value = "门店ID", paramType = "query", dataTypeClass = String.class, required = true)
    })
    @ResponseBody
    public EasyUIDataGridResult PullOrders(
            @RequestParam(value = "appPoiCode", defaultValue = "") String appPoiCode,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows
    ) {
        Completedorder completedorder = new Completedorder();
        completedorder.setAppPoiCode(appPoiCode);
        System.err.println("传入的ID" + completedorder.getOrderId());
        return orderService.findOrderlistByPage(completedorder, page, rows);
    }

    /*
     * 拉取订单信息
     * Pull order
     * produces = MediaType.APPLICATION_JSON_VALUE,@RequestBody String string,
     */
    @ApiOperation(value = "拉取订单明细")
    @GetMapping(value = "/PullOrdersDetail")
    @ResponseBody
    public EasyUIDataGridResult PullOrdersDetail(@RequestParam(value = "orderId") Long orderId) {

        return orderService.findDetailByOrderId(orderId);
    }


    /*
     * 配送人员信息 Delivery personnel information
     */
    @ApiIgnore
    @ApiOperation(value = "配送人员信息")
    @PostMapping(value = "/deliveryPersonnel")
    @ResponseBody
    public MtResult deliveryPersonnel(HttpServletRequest request) throws Exception {
        Map<Object, Object> map = objectObjectMap(request);
        if (map.size() == 0) {
            System.err.println("Completed:TEST!");
            return MtResult.ok();
        }
        TMtDeliveryPersonnel dp = JSON.parseObject(JSON.toJSONString(map), TMtDeliveryPersonnel.class);
        tMtDeliveryPersonnelService.saveOrUpdate(dp);
        Long OrderId = Long.valueOf(dp.getOrderId()); //订单号
        String app_poi_code = dp.getAppPoiCode(); //APP方门店id
        int logistics_status = Integer.parseInt(dp.getLogisticsStatus());//美团配送订单状态code ,0-配送单发往配送，5-配送侧压单，10-配送单已确认，15-骑手已到店，20-骑手已取货，40-骑手已送达，100-配送单已取消。
        String dispatcher_name = dp.getDispatcherName(); //美团配送骑手的姓名，取最新一次指派的骑手信息。
        // String dispatcher_mobile =dp.getDispatcherMobile(); //美团配送骑手的联系电话，取最新一次指派的骑手信息。
        // int time= Integer.parseInt(dp.getTime()); // 订单配送状态变更为当前状态的时间，推送10位秒级的时间戳。
        String reason;
        ResultStatus RS = ResultStatus.DAI_PEI_SONG_ORDER;
        switch (logistics_status) {
            case 0:
                reason = "配送单发往配送";
                break;
            case 5:
                reason = "配送侧压单";
                break;
            case 10:
                reason = "配送单已确认";
                break;
            case 15:
                RS = ResultStatus.PEI_SONG_ZHONG_ORDER;
                orderService.updateStatus(OrderId, String.valueOf(RS.getCode()));
                reason = "骑手已到店";
                break;
            case 20:
                RS = ResultStatus.PEI_SONG_ZHONG_ORDER;
                orderService.updateStatus(OrderId, String.valueOf(RS.getCode()));
                reason = "骑手已取货";
                break;
            case 40:
                RS = ResultStatus.WAN_CHENG_ORDER;
                orderService.updateStatus(OrderId, String.valueOf(RS.getCode()));
                reason = "骑手已送达";
                break;
            case 100:
                RS = ResultStatus.YI_QU_XIAO_ORDER;
                reason = "配送单已取消";
                break;
            default:
                RS = ResultStatus.PEI_SONG_YI_CHANG_ORDER;
                orderService.updateStatus(OrderId, String.valueOf(RS.getCode()));
                reason = "异常";
        }

        Date date3 = DateUtil.date(System.currentTimeMillis());
        OrderLogList or = new OrderLogList();
        or.setOrderId(OrderId);
        or.setTitle(reason);
        or.setContent(dispatcher_name);
        or.setCode(RS.getCode());
        or.setCreatedTime(date3);
        orderLogListService.saveOrderLogList(or);

        UserPoi user = getUserId2(app_poi_code);
        if (!(user == null)) {
            List<SocketIOClient> list1 = SocketUtil.getClientList(user.getuId());
            if (CollUtil.isNotEmpty(list1)) {
                ResultStatus finalRS = RS;
                list1.forEach(
                        item -> {
                            Map<String, Object> info = new LinkedHashMap<>();
                            info.put("orderId", OrderId);
                            info.put("WmPoiName", app_poi_code);
                            info.put("Msg", finalRS.getMsg());
                            info.put("Content", dispatcher_name);
                            info.put("StatusCode", finalRS.getCode());
                            info.put("PoiCode", app_poi_code);
                            item.sendEvent("welcome", JSON.toJSONString(info));
                        }
                );
            }
        }
        return MtResult.ok();
    }


    /**
     * 拉取订单日志及配送信息
     */
    @ApiOperation(value = "拉取订单日志及配送信息")
    @GetMapping(value = "/orderLogList")
    @ResponseBody
    public Map<String, Object> orderLogList(@RequestParam(value = "orderId") Long orderId) {
        Map<String, Object> maps = new LinkedHashMap<>();
        maps.put("log_list", orderLogListService.findListByOrderId(String.valueOf(orderId)));
        maps.put("order_delivery_personnel_info", tMtDeliveryPersonnelMapper.selectById(orderId));
        return maps;
    }

    /**
     * 拉取订单统计信息
     */
    @ApiOperation(value = "拉取订单统计信息", notes = "" +
            "merchant_id : 用户主id\n" +
            "\n" +
            "shop_id : 门店id\n" +
            "\n" +
            "select_type: 订单类型`(1-用户已提交订单；2-向商家推送订单 待接单； 3-商家取消订单;4-商家已确认；5-待配送;6-配送中;7-配送异常;8-订单已完成；9-订单已取消;13-退单/退款。)`\n" +
            "\n" +
            "type : 请求类型\n" +
            "\n" +
            "platform: 平台类型")
    @PostMapping(value = "/orderStatistical")
    @ResponseBody
    public Map<String, Object> orderStatistical(@RequestBody String string) {
        // SocketUtil.saveClient(authUser.getId(), client);
        Map<String, Object> maps = new LinkedHashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(string);
        String merchant_id = jsonObject.getString("merchant_id");
        UserPoi user =userPoiService.getUserPoiById(merchant_id).stream().findFirst().orElse(null);
        if (user != null) {
            String shop_id = jsonObject.getString("shop_id");
            String select_type = jsonObject.getString("select_type");
            String type = jsonObject.getString("type");
            String mtId ="";
            String eleId = "";
            if (user.getPoiStatus() == 1) {
                mtId =  user.getPoiId();
            }
            if (user.getEleStatus() == 1) {
                eleId =  user.getEleId();
            }
            maps.put("order_count", orderService.selectOrderCountByPoi(mtId,eleId));
            maps.put("shop_id", merchant_id);
            maps.put("order_list", orderService.findOrderInfoByPoiCodeAndStatus(mtId,eleId, select_type).stream().sorted(Comparator.comparing(Completedorder::getDaySeq).reversed()).collect(Collectors.toList()));
            maps.put("select_type", select_type);
            maps.put("type", type);
            maps.put("OrderNumber", orderService.findSummaryTodayRevenueOrderAmountAndNumber(mtId,eleId, "8"));
        }
        return maps;
    }

    public Map<Object, Object> objectObjectMap(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<Object, Object> map = new HashMap<>();
        Enumeration<String> er = request.getParameterNames();
        while (er.hasMoreElements()) {
            String name = er.nextElement();
            String value = URLDecoder.decode(request.getParameter(name), "utf-8");
            map.put(name, value);
        }
        return map;
    }

    public void logListStatus(Completedorder completedorder, ResultStatus Status, String Content) {
        Date date3 = DateUtil.date(System.currentTimeMillis());
        OrderLogList or = new OrderLogList();
        or.setOrderId(completedorder.getOrderId());
        or.setTitle(Status.getMsg());
        or.setContent(Content);
        or.setCode(Status.getCode());
        or.setCreatedTime(date3);
        orderLogListService.saveOrderLogList(or);

        UserPoi user = getUserId2(completedorder.getAppPoiCode());
        if (!(user == null)) {
            List<SocketIOClient> list1 = SocketUtil.getClientList(user.getuId());
            if (CollUtil.isNotEmpty(list1)) {
                list1.forEach(
                        item -> {
                            Map<String, Object> info = new LinkedHashMap<>();
                            info.put("orderId", completedorder.getOrderId());
                            info.put("WmPoiName", completedorder.getWmPoiName());
                            info.put("Msg", Status.getMsg());
                            info.put("Content", Content);
                            info.put("StatusCode", Status.getCode());
                            info.put("PoiCode", completedorder.getAppPoiCode());
                            item.sendEvent("welcome", JSON.toJSONString(info));
                        }
                );
            }
        }
    }

    public MtResult updateStatus(HttpServletRequest request, ResultStatus status, String content)
            throws UnsupportedEncodingException {
        Map<Object, Object> map = objectObjectMap(request);
        if (map.size() == 0) {
            System.err.println("Completed:TEST!");
            return MtResult.ok();
        }
        // 将 Map 转换为实体类
        Completedorder completedorder = JSON.parseObject(JSON.toJSONString(map), Completedorder.class);
        Long OrderId = completedorder.getOrderId();
        String Status = completedorder.getStatus();
        int r = orderService.updateStatus(OrderId, Status);
        if (r > 0) {
            logListStatus(completedorder, status, content);
            return MtResult.ok();
        } else {
            return MtResult.err(400, "err");
        }
    }

    public long getUserId(String poiId) {
        UserPoi userPoi = userPoiService.getUserPoiByPoiId(poiId).stream().findFirst().orElse(new UserPoi());
        return userPoi.getuId() == null ? 0 : userPoi.getuId();
    }

    public UserPoi setWorkshopPersonToProcess(String poiId) {
        List<UserPoi> list = userPoiService.getUserPoiByPoiId(poiId);
        if (list.size() > 0) {
            UserPoi userPoi = list.stream().findFirst().orElse(new UserPoi());
            log.info("userPoi:{}", JSON.toJSONString(userPoi));
            redisUtil.set(poiId, userPoi);
        }
        return null;
    }

    /**
     * 获取用户id
     */
    public UserPoi getUserId2(String poiId) {

        log.info("redisUtil.hasKey:{}", redisUtil.hasKey(poiId));
        if (redisUtil.hasKey(poiId)) {
            log.info("redisUtil.hasKey:{}", JSON.toJSONString(redisUtil.get(poiId)));
            return (UserPoi) redisUtil.get(poiId);
        } else {
            return setWorkshopPersonToProcess(poiId);
        }
    }
}
