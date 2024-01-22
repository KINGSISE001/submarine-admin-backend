package com.htnova.mt.order.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.htnova.common.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class MtPoiDto {
    private int openLevel;
    private long latitude;
    private int timeSelect;
    private int preBook;
    private String remark;
    private int locationId;
    private int invoiceSupport;
    private String logisticsCodes;
    private long ctime;
    private String invoiceDescription;
    private int isOnline;
    private int appId;
    private String shippingTime;
    private long longitude;
    private int preBookMinDays;
    private String address;
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
