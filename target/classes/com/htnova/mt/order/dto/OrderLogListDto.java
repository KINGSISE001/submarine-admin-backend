package com.htnova.mt.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.htnova.common.base.BaseDto;
import com.htnova.mt.order.entity.OrderLogList;
import com.htnova.mt.order.mapstruct.OrderLogListMapStruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.mapstruct.factory.Mappers;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class OrderLogListDto {
    /**
     * 订单id
     */
    private String orderId;

    /**
     * 节点名称
     */
    private String title;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
}
