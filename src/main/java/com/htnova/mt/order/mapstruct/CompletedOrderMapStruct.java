package com.htnova.mt.order.mapstruct;

import com.htnova.common.base.BaseMapStruct;
import com.htnova.mt.order.dto.CompletedOrderDto;
import com.htnova.mt.order.entity.Completedorder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CompletedOrderMapStruct extends BaseMapStruct<CompletedOrderDto, Completedorder> {
  CompletedOrderMapStruct INSTANCE = Mappers.getMapper(CompletedOrderMapStruct.class);
}
