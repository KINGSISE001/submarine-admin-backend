package com.htnova.mt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htnova.mt.order.entity.Completedorder;
import com.htnova.mt.order.entity.OrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

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
            @Param(value = "appEleCode") String appEleCode,
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
    List<Completedorder> findOrderInfoByPoiCodeAndStatus(
            @Param(value = "appPoiCode") String appPoiCode,
            @Param(value = "appEleCode") String appEleCode,
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime,
            @Param(value = "status") String status
    );

    @Select("select detail,app_poi_code,count(order_id) as sumOrder, sum(total) as total  from completedorder " +
            "where date >=#{start_time} and date <=#{end_time} and (app_poi_code=#{appPoiCode} or app_poi_code=#{appEleCode} ) and `status` =#{status} " +
            "GROUP BY detail,app_poi_code,`status`")
    List<Map<String, Object>> findSummaryTodayRevenueOrderAmountAndNumber(@Param("appPoiCode") String appPoiCode,
                                                                          @Param("appEleCode") String appEleCode,
                                                                         @Param("start_time") String start_time,
                                                                        @Param("end_time") String end_time,
                                                                        @Param("status") String status
    );

}
