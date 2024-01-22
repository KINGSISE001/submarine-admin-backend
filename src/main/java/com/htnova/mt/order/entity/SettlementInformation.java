package com.htnova.mt.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@TableName(value = "settlementinformation")
public class SettlementInformation {
    private Integer id;

    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;

    private BigDecimal offlineorderskpayamount;

    private BigDecimal packageBagMoney;

    private String orderTagList;

    private String activitydetails;

    private BigDecimal settleamount;

    private String shippingtype;

    private String allowance;

    private String extendsamount;

    private String sig;

    private BigDecimal userpayamount;

    private String appPoiCode;

    private BigDecimal foodamount;

    private BigDecimal commisionamount;

    private String reconciliationextras;

    private BigDecimal totalactivityamount;

    private BigDecimal shippingamount;

    private String payType;

    private String appId;

    private String giftinfos;

    private String status;

    private String timestamp;

}
