package com.htnova.mt.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@TableName("completedorder")
public class Completedorder implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId
    private Long orderId;

    private String orderTagList;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long wmOrderIdView;

    @TableField(value = "app_poi_code")
    private String appPoiCode;

    private String wmPoiName;

    private String wmPoiAddress;

    private String wmPoiPhone;

    private String recipientAddress;

    private String recipientAddressDetail;

    private String recipientPhone;

    private String backupRecipientPhone;

    private String recipientName;

    private Float shippingFee;

    private Double total;

    private Double originalPrice;

    private String caution;

    private String shipperPhone;

    private String status;

    private Integer cityId;

    private Integer hasInvoiced;

    private String invoiceTitle;

    private String taxpayerId;

    private String ctime;

    private String utime;

    private String deliveryTime;

    private Integer isThirdShipping;

    private Integer payType;

    private Integer pickType;

    private String latitude;

    private String longitude;

    private Integer daySeq;

    private String isFavorites;

    private String isPoiFirstOrder;

    private String isPreSaleOrder;

    private Integer dinnersNumber;

    private String logisticsCode;

    private String poiReceiveDetail;

    private String detail;

    private String extras;

    private String skuBenefitDetail;

    private String userMemberInfo;

    private String avgSendTime;

    private BigDecimal packageBagMoney;

    private String estimateArrivalTime;

    private String packageBagMoneyYuan;

    private String poiReceiveDetailYuan;

    private Integer totalWeight;

    private Integer incmpCode;

    private String incmpModules;

    private String orderPhoneNumber;

    private String date;

    private Integer pickingCompleted;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value= "TimeMillis")
    private Long TimeMillis;


}
