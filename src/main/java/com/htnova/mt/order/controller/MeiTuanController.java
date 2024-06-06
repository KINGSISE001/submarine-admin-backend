package com.htnova.mt.order.controller;


import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.htnova.common.constant.ResultStatus;
import com.htnova.common.dev.config.PayConfig;
import com.htnova.common.dto.Result;
import com.htnova.common.meituan.MeiTuanUtil;
import com.htnova.common.util.HttpRequestUtil;
import com.htnova.common.util.SignUtil;
import com.htnova.common.util.SocketUtil;
import com.htnova.common.util.SpringContextUtil;
import com.htnova.mt.order.entity.TSysKxPay;
import com.htnova.mt.order.entity.UserPoi;
import com.htnova.mt.order.service.TSysKxPayService;
import com.htnova.mt.order.service.UserPoiService;
import com.htnova.mt.order.service.impl.MeEleServiceImpl;
import com.htnova.security.entity.AuthUser;
import com.htnova.system.manage.entity.User;
import com.htnova.system.manage.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@Api(value = "美团订单处理相关接口")
@RequestMapping("/mt")
public class MeiTuanController {

    @Resource
    MeiTuanUtil mt;

    @Resource
    PayConfig pay;

    @Resource
    TSysKxPayService PayService;


    @Resource
    UserMapper userMapper;

    @Resource
    UserPoiService userPoiService;

    @Resource
    MeEleServiceImpl meEleService;


    @ApiOperation(value = "美团订单确认", notes = "1.美团向商家推送新订单消息后，商家可调用此接口确认订单，即接单。接单后订单状态将变更为“商家已确认”(status=4)。\n" +
            "\n" +
            "注意，除了调用此接口可操作接单，商家也可以通过美团商家端接单：\n" +
            "\n" +
            "(1)手动操作接单，PC客户端页面路径：订单管理->接单。\n" +
            "\n" +
            "(2)设置门店自动接单，PC客户端页面路径：店铺设置->系统设置->自动接单设置。 如订单已被商家端接单，则无需再调用此接口重复接单，调用则报错808“操作失败,订单已经确认过了”。\n" +
            "\n" +
            "2.已接入美团配送的商家，当商家操作确认订单成功，美团会即时自动将订单发往美团配送中心；医药商家无需重复调用下发美团配送订单的接口，点击查看订单状态流转。\n" +
            "\n" +
            "3.商家调用此接口确认订单的操作会记录在开发者中心->订单查询->订单脚印 里，订单脚印页面仅支持查询近30天内的订单记录。\n" +
            "\n" +
            "4.如商家已对接并配置了【已确认订单推送回调URL】接口，当商家接单后，平台会向商家系统推送已确认订单消息。\n" +
            "\n" +
            "5.目前平台的订单状态参考值有：1-用户已提交订单；2-向商家推送订单；4-商家已确认；8-订单已完成；9-订单已取消。\n" +
            "\n" +
            "6.医药B2C商家自动接单，无需对接该接口。")
    @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataTypeClass = String.class)
    @GetMapping("/orderConfirm")
    @ResponseBody
    public Result getOrderConfirm(@RequestParam(value = "orderId") String orderId) {
        return mt.orderConfirm(orderId);
    }


    @ApiOperation(value = "商家取消订单", notes = "接口说明：\n" +
            "1.本接口用于商家操作取消订单，取消后订单状态将变更为订单已取消（status=9）\n" +
            "2.商家调用此接口取消订单的操作会记录在开发者中心->订单查询->订单脚印 里，订单脚印页面仅支持查询近30天内的订单记录。\n" +
            "3.商家调用此接口取消订单，平台不会向商家系统推送取消订单消息和退款消息。如商家是在美团商家端后台操作取消订单，且已对接并配置了【美团用户或客服取消URL】回调接口，平台会向商家系统推送取消订单消息，但不推送退款消息。\n" +
            "4.目前平台的订单状态，包括：1-用户已提交订单；2-向商家推送订单；4-商家已确认；8-订单已完成；9-订单已取消。\n" +
            "5.医药B2C商家暂不支持主动取消用户订单。\n" +
            "6.企客远距离（全城送）订单，骑手取餐状态(20)后不能取消订单" +
            "APP方发出、美团接收的取消原因列表\n" +
            "\n" +
            "原因code\n" +
            "原因描述\n" +
            "2001\tAPP方商家超时接单\n" +
            "2002\tAPP方非顾客原因修改订单\n" +
            "2003\tAPP方非顾客原因取消订单\n" +
            "2004\tAPP方配送延迟\n" +
            "2005\tAPP方售后投诉\n" +
            "2006\tAPP方用户要求取消\n" +
            "2007\tAPP方其他原因取消（未传code，默认为此原因）\n" +
            "2008\t店铺太忙\n" +
            "2009\t商品已售完\n" +
            "2010\t地址无法配送\n" +
            "2011\t店铺已打烊\n" +
            "2012\t联系不上用户\n" +
            "2013\t重复订单\n" +
            "2014\t配送员取餐慢\n" +
            "2015\t配送员送餐慢\n" +
            "2016\t配送员丢餐、少餐、餐洒")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "reason_code", value = "取消原因编码", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "reason", value = "取消原因", dataTypeClass = String.class)
    })
    @GetMapping("/orderCancel")
    @ResponseBody
    public Result getOrderCancel(
            @RequestParam(value = "orderId") String orderId,
            @RequestParam(value = "reason_code", required = false) Integer reason_code,
            @RequestParam(value = "reason", required = false) String reason
    ) {
        return mt.orderCancel(orderId, reason_code, reason);
    }
    @ApiOperation(value = "商家主动发起部分退款", notes = "接口说明：\n" +
            "1.商家接单后，如需主动发起部分退款，可调用此接口操作。\n" +
            "\n" +
            "2.不支持调用此接口发起部分退款的情况有：订单未接单、订单已取消、超过退款发起时效、订单中当前仅有1件商品。\n" +
            "\n" +
            "3.此接口不支持发起全部退流程，如商家需要退订单中当前全部商品，在支持商家主动取消订单的条件下，可调用接口【order/cancel 商家取消订单】操作取消订单。\n" +
            "\n" +
            "4.如购买多件活动商品，其中部分享受优惠价，当发起部分退款时，此商品sku退款价格是优惠金额合计后进行等比分摊计算的结果。另外，订单维度的打包袋和配送费不参与均摊。")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "food_data", value = "部分退款商品数据集合的json格式数组([{\"app_food_code\":\"354654\",\"sku_id\":\"424w32t4e3t5\",\"count\":1,\"item_id\":\"\"}])", dataTypeClass = String.class),
            @ApiImplicitParam(name = "reason", value = "取消原因", dataTypeClass = String.class)
    })
    @PostMapping("/orderApplyPartRefund")
    @ResponseBody
    public Result getOrderApplyPartRefund(
            @RequestParam(value = "orderId") String orderId,
            @RequestParam(value = "reason") String reason,
            @RequestParam(value = "food_data") String food_data
    ) {
        return mt.orderApplyPartRefund(orderId,reason ,food_data);
    }

    @GetMapping("/orderRefundAgree")
    @ApiOperation(value = "订单确认退款请求", notes = "接口说明：\n" +
            "\n" +
            "1.当商家收到用户发起的部分或全额退款申请，如商家同意退款，可调用此接口操作确认退款申请。注意：部分退款成功后不影响订单当前的订单状态；全额退款成功后，订单状态会变更为“订单已取消”(status=9)。\n" +
            "\n" +
            "2.商家调用此接口同意用户退款申请的操作会记录在开发者中心->订单查询->订单脚印 里，订单脚印页面仅支持查询近30天内的订单记录。\n" +
            "\n" +
            "3.若商家调用接口同意用户申请的全额退款，平台会向商家系统推送退款消息，但不会推送取消订单消息；若商家是在商家端后台手动操作同意全额退款，平台会推送退款消息和取消订单消息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_id", value = "订单id", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "reason", value = "退款原因", required = true, dataTypeClass = String.class)
    })
    @ResponseBody
    public Result getOrderRefundAgree(
            @RequestParam(value = "order_id") String order_id,
            @RequestParam(value = "reason") String reason) {
        return mt.orderRefundAgree(order_id, reason);
    }


    @GetMapping("/orderRefundRefuse")
    @ApiOperation(value = "订单拒绝退款请求", notes = "接口说明：\n" +
            "\n" +
            "1.当商家收到用户发起的部分或全额退款申请，如商家拒绝申请，可调用此接口操作驳回退款申请。\n" +
            "\n" +
            "注意：部分退款成功后不影响订单当前的订单状态；全额退款成功后，订单状态会变更为“订单已取消”(status=9)。\n" +
            "\n" +
            "2.商家调用此接口驳回用户退款申请的操作会记录在开发者中心->订单查询->订单脚印 里，订单脚印页面仅支持查询近30天内的订单记录。\n" +
            "\n" +
            "3.若商家调用接口驳回用户发起的退款申请，平台会向商家系统推送退款消息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_id", value = "订单id", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "reason", value = "拒绝原因", required = true, dataTypeClass = String.class)
    })
    @ResponseBody
    public Result GetOrderRefundReject(
            @RequestParam(value = "order_id") String order_id,
            @RequestParam(value = "reason") String reason) {
        return mt.orderRefundReject(order_id, reason);
    }


    @GetMapping("/orderPreparationMealComplete")
    @ApiOperation(value = "商家确认已完成拣货", notes = "接口说明：\n" +
            "\n" +
            "1.如商家确认已完成备货，可调用此接口确认已完成。\n" +
            "\n" +
            "2.此接口适用于美团专送、快送、混合送的订单确认备货完成，不适用于到店自取和自配送订单，美团配送转自配的订单也不支持使用此接口。\n" +
            "\n" +
            "3.如订单当前配送状态为骑手已取货或订单状态为订单已取消，调用此接口会返回错误信息。\n" +
            "\n" +
            "4.若是美团配送订单，商家接单时间小于美团预设备货时间（目前为1分钟，预订单为预订单到时提醒时间+1分钟），调用此接口返回值中会报错“商家接单后1分钟内不能确认已完成出货”。\n" +
            "\n" +
            "5.若商家在商家端后台已操作发货，再调用此接口时会报错\"商家已完成备货，不能重复备货”。\n" +
            "\n" +
            "6.医药B2C商家不适用于该接口。")
    @ApiImplicitParam(name = "order_id", value = "订单id", required = true, dataTypeClass = String.class)
    @ResponseBody
    public Result GetOrderPreparationMealComplete(@RequestParam(value = "order_id") String order_id) {
        return mt.orderPreparationMealComplete(order_id);
    }


    @GetMapping("/poiOpenOrClose")
    @ApiOperation(value = "门店的营业状态", notes = "接口说明：\n" +
            "1. 本接口用于商家将门店设置为营业状态。\n" +
            "open_level: 1-可配送；3-休息中。\n" +
            "third_platform_id : 三方平台类型ID ：1-美团 ，2-饿了么 ，3-京东到家\n" +
            "2. 设置门店恢复营业状态的角色的权限需要与上一次设置门店置休的角色的权限一致。" +
            "所以，即使当前门店是营业状态，但是这个门店上次置休的时候是总部（商家总账号）操作的，" +
            "所以再次设置营业状态时仍需要总部设置才可以。如使用接口操作，会返回没有权限的提示。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "poi_id", value = "门店id(不单指美团ID)", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "open_level", value = "1-可配送；3-休息中", required = true, dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "third_platform_id", value = "三方平台类型ID ：1-美团 ，2-饿了么 ，3-京东到家", required = true, dataTypeClass = Integer.class)
    })
    @ResponseBody
    public Result<Object> updatePoiOpenOrClose(@RequestParam(value = "poi_id") String poi_id,
                                       @RequestParam(value = "open_level") int open_level,
                                       @RequestParam(value = "third_platform_id") int third_platform_id) {
        return mt.poiOpenOrClose(poi_id, open_level,third_platform_id);
    }
/*
    @GetMapping("/poiOfflineOrOnline")
    @ApiOperation(value = "门店的营业状态", notes = "接口说明：\n" +
            "门店上下线状态，取值范围：0-下线，1-上线:。\n" +
            "B2C医药门店，暂不支持设置为下线。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "poi_id", value = "门店id", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "is_online", value = "0-下线，1-上线", required = true, dataTypeClass = Integer.class)
    })
    @ResponseBody
    public Result updatePoiOfflineOrOnline(String poi_id, int is_online) {
        return Result.build(HttpStatus.OK, ResultStatus.FORBIDDEN, "sss");//mt.poiOfflineOrOnline(poi_id,is_online);
    }*/

    @GetMapping("/poiUpdateShippingtime")
    @ApiOperation(value = "门店的营业时间", notes = "接口说明：本接口用于商家修改门店的营业时间\n" +
            "B2C医药门店，暂不支持修改门店营业时间。" +
            "门店营业时间 \n" +
            "poi_id : 那个类型的门店就传那个门店的id \n" +
            "third_platform_id : 三方平台类型ID ：1-美团 ，2-饿了么 ，3-京东到家\n" +
            "(1)一天中的营业时间支持传多个时段,以英文逗号分隔。 \n" +
            "(2)一周的营业时间，支持按周一至周日的顺序分别设置营业时间，以英文分号隔开；支持通过上传空值操作门店当天不营业，不支持全为空。\n" +
            "(3)若只上传一个时间段，则表示7天营业时间相同 \n" +
            "(4)注意格式:每个时段之间不能存在交集 \n" +
            "例如：\"07:00-12:00,12:30-23:00;06:00-22:00;07:00-12:00;07:00-12:00;07:00-12:00;08:00-23:00;07:00-12:00\"")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "poi_id", value = "门店id", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "time", value = "营业时间", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "third_platform_id", value = "三方平台类型ID ：1-美团 ，2-饿了么 ，3-京东到家", required = true, dataTypeClass = Integer.class)
    })
    @ResponseBody
    public Result<Object> poiUpdateShippingtime(String poi_id, String time,Integer third_platform_id) {
        return mt.poiUpdateShippingtime(poi_id, time,third_platform_id);
    }


    @GetMapping("/getPoiInfo")
    @ApiOperation(value = "获取门店详细信息", notes = "本接口用于获取门店详细信息。" +
            "third_platform_id: 三方平台类型ID ：1-美团 ，2-饿了么 ，3-京东到家 \n"+
            "poi_id ：对应门店id \n"+
            "hours :营业时间 \n"+
            "status :门店状态 ,1-可配送；3-休息中\n")
    @ApiImplicitParam(name = "merchant_id", value = "门店主id", required = true, dataTypeClass = String.class)
    @ResponseBody
    public Result<Object> getPoiInfo(String merchant_id) {
        UserPoi user =userPoiService.getUserPoiById(merchant_id).stream().findFirst().orElse(null);
        List<bussiness_hours> bussiness_hours = new ArrayList<>();
        if (user != null) {
            bussiness_hours b1 = new bussiness_hours();
            String mt_id = user.getPoiId();
            Result<Object> mt_result = mt.getPoiInfo(mt_id);
            if (mt_result.getCode() != ResultStatus.BIND_ERROR.getCode()) {
                JSONObject json = JSON.parseObject(JSON.toJSONString(mt_result.getData()));
                b1.setStatus(json.getString("open_level"));
                b1.setHours(json.getString("shipping_time"));
            }
            b1.setPoi_id(mt_id);
            b1.setThird_platform_id("1");
            bussiness_hours.add(b1);
            bussiness_hours b2 = new bussiness_hours();
            String ele_id = user.getEleId();
            Result<Object> me_result = meEleService.getShopBusstatus(ele_id);
            b2.setStatus(String.valueOf(me_result.getData()));
            b2.setHours(meEleService.GetShopInfo(ele_id).getData());
            b2.setPoi_id(ele_id);
            b2.setThird_platform_id("2");
            bussiness_hours.add(b2);
            return Result.build(HttpStatus.OK,ResultStatus.REQUEST_SUCCESS,bussiness_hours);
        }
        return Result.build(HttpStatus.OK,ResultStatus.BIND_ERROR,"门店不存在");
    }

    @Data
    static class bussiness_hours {
       String  third_platform_id;
        String  poi_id;
        String  hours;
        String  status;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchant_id", value = "门店主id", required = true, dataTypeClass = String.class),
    })
    @GetMapping("/getAutoOrders")
    @ApiOperation(value = "是否自动接单", notes = "自动接单（autoOrders）（0-接单，1-不接单）")
    @ResponseBody
    public Result<Object> getAutoOrders(@RequestParam(value = "merchant_id") String merchant_id ){
        Map<String, Object> map = new HashMap<>();
       UserPoi userPoi = userPoiService.getUserById(merchant_id);
       if (userPoi != null){
            map.put("merchant_id",merchant_id);
            map.put("autoOrders",userPoi.getAutoOrders());
            return Result.build(HttpStatus.OK,ResultStatus.REQUEST_SUCCESS,map);
        }else {
           map.put("merchant_id",merchant_id);
           map.put("autoOrders","门店不存在");
           return Result.build(HttpStatus.OK,ResultStatus.BIND_ERROR,map);
       }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchant_id", value = "门店主id", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "auto", value = "0-接单，1-不接单", required = true, dataTypeClass = Integer.class)
    })
    @GetMapping("/updateAutoOrders")
    @ApiOperation(value = "是否自动接单", notes = "自动接单（0-接单，1-不接单）")
    @ResponseBody
    public Result updateAutoOrders(@RequestParam(value = "merchant_id") String merchant_id,
                                   @RequestParam(value = "auto", defaultValue = "0") int auto) {
        return mt.updateAutoOrders(merchant_id, auto);
    }

    @GetMapping("/getQrCode/{height}/{width}/{pay_type}/{money}")
    @ApiOperation(value = "获取客户支付二维码", notes = "支付二维码用于预充值费用:" +
            "\n\r 请求头需携带SESSION" +
            "\n\r 充值类型：1-运营费 2-三方配送费" +
            "\n\r 金额：单位是元" +
            "\n\r 二维码宽度：单位是像素" +
            "\n\r 二维码高度：单位是像素" +
            "\n\r 二维码宽度和高度不能超过400像素"
                  )
    @ApiImplicitParams({
                @ApiImplicitParam(name = "height", value = "二维码高度", required =true, dataTypeClass = Integer.class),
                @ApiImplicitParam(name = "width", value = "二维码宽度", required =true, dataTypeClass = Integer.class),
                @ApiImplicitParam(name = "money", value = "金额", required =true, dataTypeClass = Double.class),
                @ApiImplicitParam(name = "pay_type", value = "充值类型", required =true, dataTypeClass = Integer.class)
                }
    )
    @ResponseBody
    public void getQrCode(HttpServletResponse response, HttpServletRequest request, @PathVariable("height") int height, @PathVariable("width") int width,@PathVariable("pay_type") int pay_type,@PathVariable("money") Double money) throws IOException {

        String httpSessionId = SocketUtil.getHttpSessionId2(request.getHeader("SESSION"));
        if (!Strings.isNullOrEmpty(httpSessionId)){

            AuthUser authUser = SpringContextUtil.getAuthUser(httpSessionId);
            if (authUser != null) {
                log.info("-----------------{}--客户充值---------------------", authUser.getId());
                Map<String, String> map = new HashMap<>();
                map.put("service", "pay.comm.jspay");
                map.put("nonce_str",RandomUtil.randomString(32));
                map.put("money", String.valueOf(money));
                map.put("apikey", pay.getApikey());
                map.put("sign", SignUtil.sign(map, pay.getSignkey(), "sign"));
                String sr = HttpRequestUtil.sendPost(pay.getDoMainUrl() + "/payapi/trans/kxpay", HttpRequestUtil.getParams(map));
                TSysKxPay KxPay = JSON.parseObject(JSON.parse(sr).toString(), TSysKxPay.class);
                KxPay.setPayType(pay_type);
                KxPay.setUserId(authUser.getId());
                boolean isSave = PayService.save(KxPay);
                if (isSave) {
                    response.setContentType("image/jpg");
                    QrCodeUtil.generate(KxPay.getUrl(), new QrConfig(width, height), ImgUtil.IMAGE_TYPE_JPG,response.getOutputStream());
                }
            }
        }
    }


    /** 获取充值的余额 */
    @GetMapping("/getBalance")
    @ApiOperation(value = "获取充值的余额", notes = "获取充值的余额\n\r 请求头需携带SESSION" +
            "\n\r merchant_balance 商户余额" +
            "\n\r freight_balance 运费余额")
    @ResponseBody
    public Result getBalance( HttpServletResponse response,HttpServletRequest request) {
        String httpSessionId = SocketUtil.getHttpSessionId2(request.getHeader("SESSION"));
        if (!Strings.isNullOrEmpty(httpSessionId)){
            AuthUser authUser = SpringContextUtil.getAuthUser(httpSessionId);
            if (authUser != null) {
                log.info("-----------------{}--客户查寻充值---------------------", authUser.getId());
              User user = userMapper.findUserBalanceById(authUser.getId());
              if (user!= null) {
                   Map<String, Object> map = new HashMap<>();
                   map.put("merchant_balance", user.getMerchantBalance());
                   map.put("freight_balance", user.getFreightBalance());
                   map.put("message", "元");
                   response.setContentType("application/json; charset=utf-8");
                   return Result.build(ResultStatus.REQUEST_SUCCESS, map);
               }
            }
        }
        return Result.build(ResultStatus.SERVER_ERROR);
    }



    /** 获取充值状态 */
    @GetMapping("/getPayStatus")
    @ApiOperation(value = "获取充值状态", notes = "获取充值状态\n\r 请求头需携带SESSION" +
            "\n\r //支付状态：1 支付成功 ，0待付款，2付款失败")
    @ApiImplicitParams({
                @ApiImplicitParam(name = "payType", value = "充值类型", required =true, dataTypeClass = Integer.class),
                @ApiImplicitParam(name = "orderNo", value = "支付订单号", required =true, dataTypeClass = String.class)
                }
    )
    @ResponseBody
    public Result getPayStatus( HttpServletRequest request ,
                                @RequestParam(value = "orderNo") String orderNo,
                                @RequestParam(value = "payType") int payType) {
        String httpSessionId = SocketUtil.getHttpSessionId2(request.getHeader("SESSION"));
        if (!Strings.isNullOrEmpty(httpSessionId)){
            AuthUser authUser = SpringContextUtil.getAuthUser(httpSessionId);
            if (authUser != null) {
                log.info("-----------------{}--客户获取充值状态---------------------", authUser.getId());
                TSysKxPay status= PayService.getPayStatus(orderNo,authUser.getId(),payType);
                if (status!= null) {
                    Map<String, Object> info = new HashMap<>();
                    info.put("orderNo", orderNo);
                    info.put("userId", authUser.getId());
                    info.put("payStatus",status);
                    info.put("payType",payType);
                    info.put("Msg","支付信息");
                    return Result.build(ResultStatus.REQUEST_SUCCESS,info);
                }
            }
        }
        Map<String, Object> info = new HashMap<>();
        info.put("orderNo", orderNo);
        info.put("userId", "");
        info.put("payStatus","99");
        info.put("payType",payType);
        info.put("Msg","支付已超时");
        return Result.build(HttpStatus.OK,ResultStatus.REQUEST_SUCCESS,info);
    }


}
