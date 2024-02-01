package com.htnova.mt.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.htnova.common.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserPoiDto extends BaseDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uId;
    private String userName;
    private String name;
    private String storeName;
    private Integer appId;
    private String poiId;
    private String poiName;
    private Integer poiStatus;
    private String eleId;
    private String eleName;
    private Integer eleStatus;
    private Integer autoOrders;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Long getUId() {
        return uId;
    }

    public void setUId(Long uId) {
        this.uId = uId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public Integer getPoiStatus() {
        return poiStatus;
    }

    public void setPoiStatus(Integer poiStatus) {
        this.poiStatus = poiStatus;
    }

    public String getEleId() {
        return eleId;
    }

    public void setEleId(String eleId) {
        this.eleId = eleId;
    }

    public String getEleName() {
        return eleName;
    }

    public void setEleName(String eleName) {
        this.eleName = eleName;
    }

    public Integer getEleStatus() {
        return eleStatus;
    }

    public void setEleStatus(Integer eleStatus) {
        this.eleStatus = eleStatus;
    }

    public Integer getAutoOrders() {
        return autoOrders;
    }

    public void setAutoOrders(Integer autoOrders) {
        this.autoOrders = autoOrders;
    }
}
