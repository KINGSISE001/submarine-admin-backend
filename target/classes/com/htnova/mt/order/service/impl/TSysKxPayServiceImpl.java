package com.htnova.mt.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htnova.common.dev.config.PayConfig;
import com.htnova.common.util.HttpRequestUtil;
import com.htnova.common.util.SignUtil;
import com.htnova.mt.order.entity.TSysKxPay;
import com.htnova.mt.order.mapper.TSysKxPayMapper;
import com.htnova.mt.order.service.TSysKxPayService;
import com.htnova.system.manage.entity.User;
import com.htnova.system.manage.mapper.UserMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author RISE
 * @description 针对表【t_sys_kx_pay】的数据库操作Service实现
 * @createDate 2023-12-21 16:30:00
 */
@Service(value = "tSysKxPayService")
public class TSysKxPayServiceImpl extends ServiceImpl<TSysKxPayMapper, TSysKxPay>
        implements TSysKxPayService {

    @Resource
    PayConfig pay;

    @Resource
    private UserMapper userMapper;

    public void getPayByOrderNo() {
        long currentTimestamp = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimestamp);
        calendar.add(Calendar.MINUTE, -2);
        long newTimestamp = calendar.getTimeInMillis() / 1000;
        LambdaQueryWrapper<TSysKxPay> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(i -> i.isNull(TSysKxPay::getPaystatus)
                .or()
                .eq(TSysKxPay::getPaystatus, 0)
                .or()
                .eq(TSysKxPay::getPaystatus, 2));
        queryWrapper.ge(TSysKxPay::getOrderTime, newTimestamp);
        List<TSysKxPay> list = super.getBaseMapper().selectList(queryWrapper);
        if (ObjectUtil.isNotEmpty(list)) {
            list.forEach(item -> getPayStatus(item.getOrderid(), item.getUserId(), item.getPayType())
            );
        }
    }

    public void getPayStatus(String orderNo, long userId , int payType) {

        Map<String, String> map = new HashMap<>();
        map.put("service", "pay.comm.query_order");
        map.put("orderid", orderNo);
        map.put("nonce_str", RandomUtil.randomString(32));
        map.put("apikey", pay.getApikey());
        map.put("sign", SignUtil.sign(map, pay.getSignkey(), "sign"));
        String sr = HttpRequestUtil.sendPost(pay.getDoMainUrl() + "/payapi/pay/query_order", HttpRequestUtil.getParams(map));
        JSONObject jsonObject = JSON.parseObject(sr);
        System.out.println(jsonObject.toJSONString());
        TSysKxPay KxPay = JSON.parseObject(JSON.parse(sr).toString(), TSysKxPay.class);
        KxPay.setUserId(userId);
        KxPay.setPayType(payType);
        if (ObjectUtils.isNotEmpty(KxPay)) {
            if (KxPay.getStatus() != 10000) {
                TSysKxPay Pay = new TSysKxPay();
                Pay.setOrderid(orderNo);
                Pay.setVersion(KxPay.getVersion());
                Pay.setStatus(KxPay.getStatus());
                Pay.setPaystatus("2");
                Pay.setMessage(KxPay.getMessage());
                Pay.setSignType(KxPay.getSignType());
                Pay.setCharset(KxPay.getCharset());
                Pay.setUserId(userId);
                Pay.setPayType(payType);
                super.getBaseMapper().updateById(Pay);
            } else if (KxPay.getPaystatus().equals("1") && KxPay.getStatus() == 10000) {
                User user = userMapper.selectById(userId);
                if (user.getMerchantBalance() == null) {
                    if (payType == 1){userMapper.updateMerchantBalance(KxPay.getUserId(), BigDecimal.ZERO.add(BigDecimal.valueOf(KxPay.getPriPaymoney())));}
                    if (payType == 2){userMapper.updateFreightBalance(KxPay.getUserId(), BigDecimal.ZERO.add(BigDecimal.valueOf(KxPay.getPriPaymoney())));}
                    super.getBaseMapper().updateById(KxPay);
                } else {
                    if (payType == 1){userMapper.updateMerchantBalance(KxPay.getUserId(), user.getMerchantBalance().add(BigDecimal.valueOf(KxPay.getPriPaymoney())));}
                    if (payType == 2){userMapper.updateFreightBalance(KxPay.getUserId(), user.getFreightBalance().add(BigDecimal.valueOf(KxPay.getPriPaymoney())));}
                    super.getBaseMapper().updateById(KxPay);
                }
            } else {
                super.getBaseMapper().updateById(KxPay);
            }

        }
    }

}




