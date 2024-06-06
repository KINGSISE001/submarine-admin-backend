package com.htnova.mt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htnova.mt.order.entity.TSysKxPay;

/**
* @author RISE
* @description 针对表【t_sys_kx_pay】的数据库操作Service
* @createDate 2023-12-21 16:30:00
*/
public interface TSysKxPayService extends IService<TSysKxPay> {

    public TSysKxPay getPayStatus(String orderNo, long userId, int payType);

}
