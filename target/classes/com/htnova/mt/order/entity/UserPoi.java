package com.htnova.mt.order.entity;

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
@TableName("t_sys_user_poi")
public class UserPoi extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long uId;
    private String userName;
    private String name;
    private Integer appId;
    private String poiId;
    private String poiName;
    private Integer poiStatus;
    // 饿了么id
    private String eleId;
    // 饿了么名称
    private String eleName;
    // 饿了么状态
    private Integer eleStatus;

    private Integer autoOrders;

}
