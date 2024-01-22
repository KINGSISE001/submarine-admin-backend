package com.htnova.common.dto;

import java.io.Serializable;

public class MtResult implements Serializable {
    private static final long serialVersionUID = 1L;

    // 响应中的数据
    private Object data;
    private int code;

    public static MtResult ok() {
        return new MtResult();
    }

    public static MtResult err(Integer code, String mes) {
        return new MtResult(code, mes);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public MtResult() {
        this.code = 200;
        this.data = "ok";
    }

    public MtResult(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "MtResult [data=" + data + ", code=" + code + "]";
    }
}
