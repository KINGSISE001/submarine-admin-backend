package com.htnova.mt.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.htnova.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@TableName("t_mt_poi")
public class MtPoi {
    private int openLevel;

    @JsonSerialize(using = ToStringSerializer.class)
    private long latitude;
    private int timeSelect;
    private int preBook;
    private String remark;
    private int locationId;
    private int invoiceSupport;
    private String logisticsCodes;

    @JsonSerialize(using = ToStringSerializer.class)
    private long ctime;
    private String invoiceDescription;
    private int isOnline;
    private int appId;
    private String shippingTime;

    @JsonSerialize(using = ToStringSerializer.class)
    private long longitude;
    private int preBookMinDays;
    private String address;

    @JsonSerialize(using = ToStringSerializer.class)
    private long utime;
    private String tagName;
    private int preBookMaxDays;
    private int shippingFee;
    private int invoiceMinPrice;

    @TableId
    private String appPoiCode;

    private String phone;
    private String name;
    private String promotionInfo;
    private String standbyTel;
    private String picUrl;
    private long cityId;
}
