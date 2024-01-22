package com.htnova.common.dto;

import com.htnova.common.constant.ResultStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 统一的返回数据封装 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EleResult<E> {
    private static final ResultStatus ELE_SUCCESS = ResultStatus.ELE_SUCCESS;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
   public static class Body<E> {
       private Integer errno;
       private String error;
       private Data1 data;
   }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data1 {
        private String source_order_id;
    }

    private Body<Object> body;


    public static EleResult<Void> ok(String orderid) {
        return EleResult
                .<Void>builder()
                .body(Body.builder().errno(ELE_SUCCESS.getCode()).error(ELE_SUCCESS.getMsg()).data(Data1.builder().source_order_id(orderid).build()).build()).build();
    }

    public static EleResult<Void> ok() {
        return EleResult
                .<Void>builder()
                .body(Body.builder().errno(ELE_SUCCESS.getCode()).error(ELE_SUCCESS.getMsg()).build()).build();
    }

    public static EleResult<Void> build(ResultStatus resultStatus) {
        return EleResult
            .<Void>builder()
                .body(Body.builder().errno(resultStatus.getCode()).error(resultStatus.getMsg()).build()).build();

    }


    /** 用于接收前端拼接的 msg 数组 */
    public static EleResult<Object> build(ResultStatus resultStatus,String orderid) {
        return EleResult
            .<Object>builder()
                .body(Body.builder().errno(resultStatus.getCode()).error(resultStatus.getMsg()).data(Data1.builder().source_order_id(orderid).build()).build()).build();

    }

    public static <E> EleResult<E> build(ResultStatus status, String msg, String orderid) {
        return EleResult.<E>builder().body(Body.<Object>builder().errno(status.getCode()).error(msg).data(Data1.builder().source_order_id(orderid).build()).build()).build();
    }
}
