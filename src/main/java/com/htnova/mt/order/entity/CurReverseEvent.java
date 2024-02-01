package com.htnova.mt.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class CurReverseEvent {
    private String event_id;
    private List<String> image_list;
    private int last_refund_status;
    private int last_return_goods_status;
    private long occur_time;
    private int operator_role;
    private String order_id;
    private int reason_code;
    private String reason_content;
    private long refund_order_id;
    private String refund_reason_desc;
    private int refund_status;
    private int return_goods_status;
    private String platform_shop_id;
}
