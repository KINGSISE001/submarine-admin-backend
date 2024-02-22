package com.htnova.common.meituan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.htnova.common.constant.ResultStatus;
import com.htnova.common.dto.Result;
import com.htnova.mt.order.controller.OrderController;
import com.htnova.mt.order.entity.Completedorder;
import com.htnova.mt.order.entity.SettlementInformation;
import com.htnova.mt.order.entity.UserPoi;
import com.htnova.mt.order.mapper.CompletedorderMapper;
import com.htnova.mt.order.mapper.SettlementInformationMapper;
import com.htnova.mt.order.service.OrderService;
import com.htnova.mt.order.service.UserPoiService;
import com.htnova.mt.order.service.impl.MeEleServiceImpl;
import com.htnova.mt.order.service.impl.O2oBrandInfoServiceImpl;
import com.sankuai.meituan.shangou.open.sdk.domain.SystemParam;
import com.sankuai.meituan.shangou.open.sdk.exception.SgOpenException;
import com.sankuai.meituan.shangou.open.sdk.request.*;
import com.sankuai.meituan.shangou.open.sdk.response.SgOpenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class MeiTuanUtil {

    @Resource
    SettlementInformationMapper settlementInformationMapper;

    @Resource
    O2oBrandInfoServiceImpl o2oBrandInfoService;


    @Resource
    OrderController orderController;


    @Resource
    UserPoiService userPoiService;

    @Resource
    OrderService orderService;

    @Resource
    CompletedorderMapper completedorderMapper;

    @Resource
    MeEleServiceImpl meEleServiceImpl;

    public SystemParam getSystemParam(String order_no) {
        SettlementInformation appId = settlementInformationMapper.selectById(order_no);
        if (appId == null) {
            return null;
        }
        return new SystemParam(appId.getAppId(), o2oBrandInfoService.getById(appId.getAppId()).getMtappkey());
    }

    ;

    public SystemParam getSystemParam2(String poi_id) {
        List<UserPoi> appId = userPoiService.getUserPoiByPoiId(poi_id);
        if (appId.isEmpty()) {
            return null;
        }
        return new SystemParam(String.valueOf(appId.get(0).getAppId()), o2oBrandInfoService.getById(appId.get(0).getAppId()).getMtappkey());
    }

    ;

    public Result<Object> updateAutoOrders(String poi_id, int auto) {
        LambdaUpdateWrapper<UserPoi> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserPoi::getId, poi_id);
        updateWrapper.set(UserPoi::getAutoOrders, auto);
        boolean update = userPoiService.update(updateWrapper);
        if (update) {
            return Result.build(HttpStatus.OK, ResultStatus.EDIT_SUCCESS, "成功了，刷新下");
        } else {
            return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR, "没成功(⊙o⊙)");
        }

    }


    /**
     * 美团订单确认
     *
     * @return
     */
    public Result orderConfirm(String order_id) {
        Completedorder com = completedorderMapper.selectById(order_id);
        if (com != null) {
            if (com.getDetail().equals("饿了么")) {
                return meEleServiceImpl.orderConfirm(order_id);
            } else if (com.getDetail().equals("美团")) {
                SystemParam systemParam = getSystemParam(order_id);
                log.info("orderConfirm systemParam:{}", systemParam);
                if (systemParam == null) {
                    return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR);
                }
                OrderConfirmRequest orderConfirmRequest = new OrderConfirmRequest(systemParam);
                orderConfirmRequest.setOrder_id(order_id);
                SgOpenResponse sgOpenResponse;
                try {
                    sgOpenResponse = orderConfirmRequest.doRequest();
                } catch (SgOpenException e) {
                    e.printStackTrace();
                    return Result.build(HttpStatus.OK, ResultStatus.SERVER_ERROR);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.build(HttpStatus.OK, ResultStatus.SERVER_ERROR);
                }
                //发起请求时的sig，用来联系美团员工排查问题时使用
                String requestSig = sgOpenResponse.getRequestSig();
                System.out.println(requestSig);
                //请求返回的结果，按照官网的接口文档自行解析即可
                String requestResult = sgOpenResponse.getRequestResult();
                System.out.println(requestResult);
                return Result.build(HttpStatus.OK, ResultStatus.REQUEST_SUCCESS, requestResult);
            } else {
                return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR);
            }
        }
        return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR);
    }

    /**
     * 美团订单取消
     * APP方发出、美团接收的取消原因列表
     * <p>
     * 原因code
     * 原因描述
     * 2001	APP方商家超时接单
     * 2002	APP方非顾客原因修改订单
     * 2003	APP方非顾客原因取消订单
     * 2004	APP方配送延迟
     * 2005	APP方售后投诉
     * 2006	APP方用户要求取消
     * 2007	APP方其他原因取消（未传code，默认为此原因）
     * 2008	店铺太忙
     * 2009	商品已售完
     * 2010	地址无法配送
     * 2011	店铺已打烊
     * 2012	联系不上用户
     * 2013	重复订单
     * 2014	配送员取餐慢
     * 2015	配送员送餐慢
     * 2016	配送员丢餐、少餐、餐洒
     *
     * @return
     */
    public Result orderCancel(String order_id, Integer reason_code, String reason) {
        Completedorder com = completedorderMapper.selectById(order_id);
        if (com == null) {
            return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR);
        } else {
            if (com.getDetail().equals("饿了么")) {
                return meEleServiceImpl.orderCancel(order_id, reason_code, reason);
            } else if (com.getDetail().equals("美团")) {

                SystemParam systemParam = getSystemParam(order_id);
                if (systemParam == null) {
                    return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR);
                }
                OrderCancelRequest orderCancelRequest = new OrderCancelRequest(systemParam);
                orderCancelRequest.setOrder_id(order_id);
                orderCancelRequest.setReason(reason);
                orderCancelRequest.setReason_code(reason_code);
                SgOpenResponse sgOpenResponse;
                try {
                    sgOpenResponse = orderCancelRequest.doRequest();
                } catch (SgOpenException e) {
                    e.printStackTrace();
                    return Result.build(HttpStatus.OK, ResultStatus.SERVER_ERROR);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.build(HttpStatus.OK, ResultStatus.SERVER_ERROR);
                }
                //请求返回的结果，按照官网的接口文档自行解析即可
                String requestResult = sgOpenResponse.getRequestResult();
                System.out.println(requestResult);
                SettlementInformation appId = settlementInformationMapper.selectById(order_id);
                Completedorder completedorder = new Completedorder();
                completedorder.setOrderId(Long.valueOf(order_id));
                completedorder.setAppPoiCode(appId.getAppPoiCode());
                orderController.logListStatus(completedorder, ResultStatus.QU_XIAO_ORDER, "商家自主取消订单:" + reason);
                orderService.updateStatus(Long.parseLong(order_id), String.valueOf(ResultStatus.QU_XIAO_ORDER.getCode()));
                return Result.build(HttpStatus.OK, ResultStatus.REQUEST_SUCCESS, requestResult);
            }
        }
        return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR, "未知错误！");
    }

    /**
     * 订单确认退款请求
     * 当商家收到用户发起的部分或全额退款申请，如商家同意退款，可调用此接口操作确认退款申请
     * 注意：部分退款成功后不影响订单当前的订单状态；全额退款成功后，订单状态会变更为“订单已取消”(status=9)。
     *
     * @return OrderReverseProcess
     */
    public Result orderRefundAgree(String order_id, String reason) {
        Completedorder com = completedorderMapper.selectById(order_id);
        if (com == null) {
            return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR);
        } else {
            if (com.getDetail().equals("饿了么")) {
                return meEleServiceImpl.OrderReverseProcess(order_id, com.getAppPoiCode(), "1", reason);
            } else if (com.getDetail().equals("美团")) {
                SystemParam systemParam = getSystemParam(order_id);
                if (systemParam == null) {
                    return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR);
                }
                OrderRefundAgreeRequest request = new OrderRefundAgreeRequest(systemParam);
                request.setOrder_id(order_id);
                request.setReason(reason);
                SgOpenResponse sgOpenResponse;
                try {
                    sgOpenResponse = request.doRequest();
                } catch (SgOpenException e) {
                    e.printStackTrace();
                    return Result.build(HttpStatus.OK, ResultStatus.SERVER_ERROR);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.build(HttpStatus.OK, ResultStatus.SERVER_ERROR);
                }
                //发起请求时的sig，用来联系美团员工排查问题时使用
                String requestSig = sgOpenResponse.getRequestSig();
                System.out.println(requestSig);
                //请求返回的结果，按照官网的接口文档自行解析即可
                String requestResult = sgOpenResponse.getRequestResult();
                System.out.println(requestResult);
                return Result.build(HttpStatus.OK, ResultStatus.REQUEST_SUCCESS, requestResult);
            }
        }
        return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR, "未知错误！");
    }

    /**
     * 驳回订单退款申请
     * 1.当商家收到用户发起的部分或全额退款申请，如商家拒绝申请，可调用此接口操作驳回退款申请。
     * 注意：部分退款成功后不影响订单当前的订单状态；全额退款成功后，订单状态会变更为“订单已取消”(status=9)。
     * <p>
     * 2.商家调用此接口驳回用户退款申请的操作会记录在开发者中心->订单查询->订单脚印 里，订单脚印页面仅支持查询近30天内的订单记录。
     * <p>
     * 3.若商家调用接口驳回用户发起的退款申请，平台会向商家系统推送退款消息。
     *
     * @return
     */
    public Result orderRefundReject(String order_id, String reason) {

        Completedorder com = completedorderMapper.selectById(order_id);
        if (com == null) {
            return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR);
        } else {
            if (com.getDetail().equals("饿了么")) {
                return meEleServiceImpl.OrderReverseProcess(order_id, com.getAppPoiCode(), "2", reason);
            } else if (com.getDetail().equals("美团")) {
                SystemParam systemParam = getSystemParam(order_id);
                if (systemParam == null) {
                    return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR);
                }
                OrderRefundRejectRequest request = new OrderRefundRejectRequest(systemParam);
                request.setOrder_id(order_id);
                request.setReason(reason);
                SgOpenResponse sgOpenResponse;
                try {
                    sgOpenResponse = request.doRequest();
                } catch (SgOpenException e) {
                    e.printStackTrace();
                    return Result.build(HttpStatus.OK, ResultStatus.SERVER_ERROR);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.build(HttpStatus.OK, ResultStatus.SERVER_ERROR);
                }
                //发起请求时的sig，用来联系美团员工排查问题时使用
                String requestSig = sgOpenResponse.getRequestSig();
                System.out.println(requestSig);
                //请求返回的结果，按照官网的接口文档自行解析即可
                String requestResult = sgOpenResponse.getRequestResult();
                System.out.println(requestResult);
                return Result.build(HttpStatus.OK, ResultStatus.REQUEST_SUCCESS, requestResult);
            }
        }
        return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR, "未知错误！");

    }

    /**
     * 商家确认已完成拣货
     * 1.如商家确认已完成备货，可调用此接口确认已完成。
     * 2.此接口适用于美团专送、快送、混合送的订单确认备货完成，不适用于到店自取和自配送订单，美团配送转自配的订单也不支持使用此接口。
     * 3.如订单当前配送状态为骑手已取货或订单状态为订单已取消，调用此接口会返回错误信息。
     * 4.若是美团配送订单，商家接单时间小于美团预设备货时间（目前为1分钟，预订单为预订单到时提醒时间+1分钟），调用此接口返回值中会报错“商家接单后1分钟内不能确认已完成出货”。
     * 5.若商家在商家端后台已操作发货，再调用此接口时会报错"商家已完成备货，不能重复备货”。
     * 6.医药B2C商家不适用于该接口。
     *
     * @return
     */
    public Result orderPreparationMealComplete(String order_id) {
        Completedorder com = completedorderMapper.selectById(order_id);
        if (com == null) {
            return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR);
        } else {
            if (com.getDetail().equals("饿了么")) {
                return meEleServiceImpl.orderPickcomplete(order_id);
            } else if (com.getDetail().equals("美团")) {
                SystemParam systemParam = getSystemParam(order_id);
                if (systemParam == null) {
                    return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR);
                }
                OrderPreparationMealCompleteRequest request = new OrderPreparationMealCompleteRequest(systemParam);
                request.setOrder_id(order_id);
                SgOpenResponse sgOpenResponse;
                try {
                    sgOpenResponse = request.doRequest();
                } catch (SgOpenException e) {
                    e.printStackTrace();
                    return Result.build(HttpStatus.OK, ResultStatus.SERVER_ERROR);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.build(HttpStatus.OK, ResultStatus.SERVER_ERROR);
                }
                //发起请求时的sig，用来联系美团员工排查问题时使用
                String requestSig = sgOpenResponse.getRequestSig();
                System.out.println(requestSig);
                //请求返回的结果，按照官网的接口文档自行解析即可
                String requestResult = sgOpenResponse.getRequestResult();
                System.out.println(requestResult);
                return Result.build(HttpStatus.OK, ResultStatus.REQUEST_SUCCESS, requestResult);
            }
        }
        return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR, "未知错误");
    }

    /**
     * third_platform_id : 三方平台类型ID ：1-美团 ，2-饿了么 ，3-京东到家
     * 门店的营业状态，取值范围：1-可配送；3-休息中。
     * 设置门店恢复营业状态的角色的权限需要与上一次设置门店置休的角色的权限一致。
     * 所以，即使当前门店是营业状态，但是这个门店上次置休的时候是总部（商家总账号）操作的，所以再次设置营业状态时仍需要总部设置才可以。
     * 如使用接口操作，会返回没有权限的提示。B2C医药门店，暂不支持设置休息中。
     *
     * @param poi_id
     * @param open_level 3-将门店设置为休息状态 1-将门店设置为营业状态；
     */
    public Result<Object> poiOpenOrClose(String poi_id, int open_level, int third_platform_id) {
        if (third_platform_id == 1) {
            SystemParam systemParam = getSystemParam2(poi_id);
            SgOpenResponse sgOpenResponse;
            try {
                //组建请求参数
                if (open_level == 3) {
                    PoiCloseRequest poiCloseRequest = new PoiCloseRequest(systemParam);
                    poiCloseRequest.setApp_poi_code(poi_id);
                    sgOpenResponse = poiCloseRequest.doRequest();
                } else if (open_level == 1) {
                    PoiOpenRequest request = new PoiOpenRequest(systemParam);
                    request.setApp_poi_code(poi_id);
                    sgOpenResponse = request.doRequest();
                } else {
                    return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR, "参数错误");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Result.build(HttpStatus.OK, ResultStatus.SERVER_ERROR, e.getMessage());
            }
            //发起请求时的sig，用来联系美团员工排查问题时使用
            String requestSig = sgOpenResponse.getRequestSig();
            System.out.println(requestSig);
            //请求返回的结果，按照官网的接口文档自行解析即可
            String requestResult = sgOpenResponse.getRequestResult();
            System.out.println(requestResult);
            return Result.build(HttpStatus.OK, ResultStatus.REQUEST_SUCCESS, requestResult);
        } else if (third_platform_id == 2) {
            if (open_level == 3) {
                return meEleServiceImpl.ShopClose(poi_id);
            } else if (open_level == 1) {
                return meEleServiceImpl.ShopOpen(poi_id);
            }
        } else if (third_platform_id == 3) {
            return Result.build(HttpStatus.OK, ResultStatus.REQUEST_SUCCESS, "未开发");
        }else {
            return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR, "参数错误");
        }

        return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR,"未知错误");
    }


    /**
     * 更新门店营业时间
     * 门店营业时间
     * (1)一天中的营业时间支持传多个时段,以英文逗号分隔。
     * (2)一周的营业时间，支持按周一至周日的顺序分别设置营业时间，以英文分号隔开；支持通过上传空值操作门店当天不营业，不支持全为空。
     * (3)若只上传一个时间段，则表示7天营业时间相同
     * (4)注意格式:每个时段之间不能存在交集。
     */
    public Result<Object> poiUpdateShippingtime(String poi_id, String time, int third_platform_id) {
        if (third_platform_id == 1) {
            SystemParam systemParam = getSystemParam2(poi_id);
            PoiShippingTimeUpdateRequest request = new PoiShippingTimeUpdateRequest(systemParam);
            request.setApp_poi_code(poi_id);
            request.setShipping_time(time);
            SgOpenResponse sgOpenResponse;
            try {
                sgOpenResponse = request.doRequest();
            } catch (Exception e) {
                e.printStackTrace();
                return Result.build(HttpStatus.OK, ResultStatus.SERVER_ERROR, e.getMessage());
            }
            //发起请求时的sig，用来联系美团员工排查问题时使用
            String requestSig = sgOpenResponse.getRequestSig();
            System.out.println(requestSig);
            //请求返回的结果，按照官网的接口文档自行解析即可
            String requestResult = sgOpenResponse.getRequestResult();
            System.out.println(requestResult);
            return Result.build(HttpStatus.OK, ResultStatus.REQUEST_SUCCESS, requestResult);
        } else if (third_platform_id == 2) {
            return meEleServiceImpl.ShopUpdate(poi_id,time);
        } else if (third_platform_id == 3) {
            return Result.build(HttpStatus.OK, ResultStatus.REQUEST_SUCCESS,"未开发");
        }else{
            return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR, "未知状态");
        }
    }

    /**
     * 获取门店信息
     *
     * @param poi_id
     * @return
     */
    public Result<Object> getPoiInfo(String poi_id) {


        SystemParam systemParam = getSystemParam2(poi_id);
        if (systemParam == null) {
            return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR, "系统参数为空或错误");
        }
        //组建请求参数
        PoiMGetRequest poiMGetRequest = new PoiMGetRequest(systemParam);
        // 上传单门店
        poiMGetRequest.setApp_poi_codes(poi_id);
        //发起请求
        SgOpenResponse sgOpenResponse;
        try {
            sgOpenResponse = poiMGetRequest.doRequest();
        } catch (SgOpenException | IOException e) {
            e.printStackTrace();
            return Result.build(HttpStatus.OK, ResultStatus.SERVER_ERROR, e.getMessage());
        }
        //发起请求时的sig，用来联系美团员工排查问题时使用
        String requestSig = sgOpenResponse.getRequestSig();
        //请求返回的结果，按照官网的接口文档自行解析即可
        String requestResult = sgOpenResponse.getRequestResult();
        JSONArray jsonArray = JSON.parseObject(requestResult).getJSONArray("data");

        if (jsonArray.size() == 0) {
            return Result.build(HttpStatus.OK, ResultStatus.BIND_ERROR, "门店信息为空");
        }

        LambdaQueryWrapper<UserPoi> LQW = new LambdaQueryWrapper<>();
        LQW.eq(UserPoi::getPoiId, poi_id);
        UserPoi userPoi = userPoiService.getOne(LQW);

        JSONObject jsonObject = jsonArray.getJSONObject(0);
        jsonObject.put("auto_order", userPoi.getAutoOrders());
        return Result.build(HttpStatus.OK, ResultStatus.REQUEST_SUCCESS, jsonObject);
    }

}
