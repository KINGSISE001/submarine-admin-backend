package com.htnova.mt.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.htnova.common.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class UserPoiDto extends BaseDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uId;
    private String userName;
    private String name;
    private Integer appId;
    private String poiId;
    private String poiName;
    private Integer poiStatus;
    private String eleId;
    private String eleName;
    private Integer eleStatus;
    private Integer autoOrders;
}
