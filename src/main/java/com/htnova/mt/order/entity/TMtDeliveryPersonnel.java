package com.htnova.mt.order.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @TableName t_mt_delivery_personnel
 */
@TableName(value ="t_mt_delivery_personnel")
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class TMtDeliveryPersonnel implements Serializable {
    /**
     * 订单号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId
    private String orderId;

    /**
     * 输入参数计算后的签名结果
     */
    private String sig;

    /**
     * APP方门店id
     */
    private String appPoiCode;

    /**
     *
     */
    private String orderTagList;

    /**
     *
     */
    private String mtPkgId;

    /**
     * 美团配送骑手的联系电话，取最新一次指派的骑手信息。 骑手手机号会以隐私号形式推送，请兼容13812345678和13812345678_123456两种号码格式，最多不超过20位，以便对接隐私号订单。
     */
    private String dispatcherMobile;

    /**
     *
     */
    private String time;

    /**
     *
     */
    private String logisticsCode;

    /**
     * 美团分配给APP方的id
     */
    private String appId;

    /**
     * 美团配送订单状态code，目前美团配送状态值有：0-配送单发往配送，5-配送侧压单，10-配送单已确认，15-骑手已到店，20-骑手已取货，40-骑手已送达，100-配送单已取消。
     */
    private String logisticsStatus;

    /**
     * 美团配送骑手的姓名，取最新一次指派的骑手信息
     */
    private String dispatcherName;

    /**
     *
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String timestamp;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
