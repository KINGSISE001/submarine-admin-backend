package com.htnova.mt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htnova.mt.order.entity.Completedorder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.htnova.mt.order.entity.OrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CompletedorderMapper extends BaseMapper<Completedorder> {
    int deleteByPrimaryKey(Long orderId);

    int insertCompletedorder(Completedorder completedorder);
    Long getTotalOrders(Map<String, Object> map);
    Map<String, Object> selectByPrimaryKey(Long orderId);
    List selectByAppPoiCode(Map<String, Object> map);
    int updateByPrimaryKeySelective(Completedorder record);
    List<Completedorder> selectOrderlistByPage(Completedorder completedorder);
    int updateByPrimaryKey(Completedorder record);
    int updateStatus(@Param(value = "orderId") String orderId, @Param(value = "status") String Status);

    List<OrderStatus> findOrderStatusByPoiCode(
            @Param(value = "appPoiCode") String appPoiCode,
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
    List<Completedorder> findOrderInfoByPoiCodeAndStatus(
            @Param(value = "appPoiCode") String appPoiCode,
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime,
            @Param(value = "status") String status
    );

    @Select("select app_poi_code,count(order_id) as sumOrder, sum(total) as total  from completedorder\n" +
            "where date >=#{start_time} and date <=#{end_time} and app_poi_code=#{appPoiCode} and `status` =#{status}\n" +
            "GROUP BY app_poi_code,`status`")
    List<Map<String, Object>> findSummaryTodayRevenueOrderAmountAndNumber(@Param("appPoiCode") String appPoiCode,
                                                                         @Param("start_time") String start_time,
                                                                        @Param("end_time") String end_time,
                                                                        @Param("status") String status
    );

}
