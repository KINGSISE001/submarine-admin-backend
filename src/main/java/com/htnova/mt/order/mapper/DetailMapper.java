package com.htnova.mt.order.mapper;

import com.htnova.mt.order.entity.Detail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DetailMapper {
    int deleteByPrimaryKey(Long orderId);
    int insertDetail(@Param("lists") List<Detail> lists);
    List<Detail> selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(Detail record);

    int updateByPrimaryKey(Detail record);

    /**
     * 订单部分退款信息状态更新
     * @param refundStatus
     * @param total_refund_price
     * @param count_refund
     * @param orderId
     * @param skuId
     * @return
     */
    @Update("update detail set if_refund = #{refundStatus}," +
            " total_refund_price=#{total_refund_price} ," +
            "count_refund = #{count_refund} " +
            "where  order_id = #{orderId} and mt_sku_id=#{skuId}")
    int updateRefundStatus(String refundStatus, String total_refund_price, int count_refund ,String orderId, String skuId);
}
