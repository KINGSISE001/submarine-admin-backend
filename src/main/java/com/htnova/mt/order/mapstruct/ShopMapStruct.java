package com.htnova.mt.order.mapstruct;

import com.htnova.common.base.BaseMapStruct;
import com.htnova.mt.order.dto.ShopDto;
import com.htnova.mt.order.entity.Shop;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShopMapStruct extends BaseMapStruct<ShopDto, Shop> {
  ShopMapStruct INSTANCE = Mappers.getMapper(ShopMapStruct.class);
}
