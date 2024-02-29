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
public class JdResult<E> {
    private static final ResultStatus ELE_SUCCESS = ResultStatus.ELE_SUCCESS;

    private Integer code;
    private String msg;
    private E data;


    public static JdResult<Void> build() {
        return JdResult
                .<Void>builder()
                .code(ELE_SUCCESS.getCode())
                .msg(ELE_SUCCESS.getMsg())
                .build();
    }
    public static JdResult<Void> build(ResultStatus resultStatus) {
        return JdResult
            .<Void>builder()
                .code(resultStatus.getCode())
                .msg(resultStatus.getMsg())
                .build();
    }


    /** 用于接收前端拼接的 msg 数组 */
    public static <E> JdResult<E> build(ResultStatus resultStatus, E data) {
        return JdResult
            .<E>builder()
                .code(resultStatus.getCode())
                .msg(resultStatus.getMsg())
                .data(data)
                .build();
    }

    public static JdResult<Object> build(ResultStatus resultStatus, Object... data) {
        return JdResult
                .<Object>builder()
                .code(resultStatus.getCode())
                .msg(resultStatus.getMsg())
                .data(data)
                .build();
    }

    public static <E> JdResult<E> build(ResultStatus status, String msg) {
        return JdResult.<E>builder()
                .code(status.getCode())
                .msg(msg)
                .build();
    }
}
