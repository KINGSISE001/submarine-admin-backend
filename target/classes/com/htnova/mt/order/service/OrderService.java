package com.htnova.mt.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.htnova.common.dto.EasyUIDataGridResult;
import com.htnova.mt.order.entity.Completedorder;
import com.htnova.mt.order.entity.Detail;
import com.htnova.mt.order.entity.OrderStatus;
import com.htnova.mt.order.entity.SettlementInformation;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface OrderService  {
    int insertCompletedorder(@Param("completedorder") Completedorder completedorder);

    int insertDetail(List<Detail> lists);

    int updateStatus(Long orderId, String Status);


    int updatePickingStatus(Long orderId, Integer Status);

    int insertsettlementinformation(@Param("settlementinformation") SettlementInformation settlementinformation);

    IPage selectorderinformation(@Param("AppPoiCode") String AppPoiCode, int start, int size, String start_time, String end_time, Integer status,String order_id);

    public EasyUIDataGridResult findOrderlistByPage(Completedorder completedorder, Integer page, Integer rows);

    EasyUIDataGridResult findDetailByOrderId(Long order);


    Map<Integer, Integer> selectOrderCountByPoi(String poi);

    List<Completedorder> findOrderInfoByPoiCodeAndStatus(String poi, String status);
    List<Map<String, Object>> findSummaryTodayRevenueOrderAmountAndNumber(String poi, String status);

}
