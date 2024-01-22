package com.htnova.mt.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * t_sys_kx_pay 表名
 */
@TableName(value ="t_sys_kx_pay")
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class TSysKxPay implements Serializable {
    /**
     * 系统订单号，唯一值
     */
    @TableId
    private String orderid;

    /**
     * 接口名称
     */
    private String service;

    /**
     * 商户外部订单号,注意：H5订单不返回orderid，支付查询只能用mch_orderid,必须保证mch_orderid商户系统唯一
     */
    private String mchOrderid;

    /**
     * 下单时间,时间戳格式，精确到秒
     */
    private Integer orderTime;


    /**
     * 用户id
     */
    private long userId;

    /**
     * 聚合支付的支付地址
     */
    private String url;

    /**
     * 接口版本号，3.0
     */
    private String version;

    /**
     * 接口字符编码，UTF-8
     */
    private String charset;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 错误状态
     */
    private int status;

    /**
     * 签名方式，MD5
     */
    private String signType;

    /**
     * 签名字符串，参照签名方法
     */
    private String sign;

    /**
     * 通道流水号
     */
    private String tradeNo;

    /**
     *  充值类型
     */
    private Integer payType;

    /**
     * 支付状态：1 支付成功 ，0待付款，2付款失败
     */
    private String paystatus;

    /**
     * 下单支付金额，单位元
     */
    private Double paymoney;

    /**
     * 实际支付金额，单位元
     */
    private Double priPaymoney;

    /**
     * 商家实收金额，单位元
     */
    private Double receiptAmount;

    /**
     * 买家付款金额，单位元
     */
    private Double buyerPayAmount;

    /**
     * 代金券金额，单位元
     */
    private Double couponFee;

    /**
     * 消费者账号
     */
    private String buyerAccount;

    /**
     * 支付方式
     */
    private String mPaytype;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
