package com.htnova.mt.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@TableName("detail")
public class Detail {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;

    private Double actualPrice;

    private String appFoodCode;

    private String appMedicineCode;

    private Integer boxNum;

    private Double boxPrice;

    private String detailExtra;

    private Integer foodDiscount;

    private String foodName;

    private String foodProperty;

    private String mtSkuId;

    private String mtSpuId;

    private String mtTagId;

    private Double originalPrice;

    private Double price;

    private Integer quantity;

    private String skuId;

    private String spec;

    private String unit;

    private String upc;

    private String weight;

    private String weightForUnit;

    private String weightUnit;


}
