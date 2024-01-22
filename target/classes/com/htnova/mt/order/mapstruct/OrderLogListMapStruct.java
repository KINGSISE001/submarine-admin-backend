package com.htnova.mt.order.mapstruct;

import com.htnova.common.base.BaseMapStruct;
import com.htnova.mt.order.dto.OrderLogListDto;
import com.htnova.mt.order.entity.OrderLogList;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderLogListMapStruct extends BaseMapStruct<OrderLogListDto, OrderLogList> {
    OrderLogListMapStruct INSTANCE = Mappers.getMapper(OrderLogListMapStruct.class);
}
