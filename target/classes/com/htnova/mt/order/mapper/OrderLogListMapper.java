package com.htnova.mt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htnova.mt.order.dto.OrderLogListDto;
import com.htnova.mt.order.entity.OrderLogList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderLogListMapper extends BaseMapper<OrderLogList> {
    IPage<OrderLogList> findPage(IPage<Void> xPage, @Param("orderLogListDto") OrderLogListDto orderLogListDto);

    List<OrderLogList> findList(@Param("orderLogListDto") OrderLogListDto orderLogListDto);

    List<OrderLogList> findListByOrderId (@Param("orderId") String orderId);
}
