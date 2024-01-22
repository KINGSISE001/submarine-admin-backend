package com.htnova.mt.order.mapstruct;

import com.htnova.common.base.BaseMapStruct;
import com.htnova.mt.order.dto.UserPoiDto;
import com.htnova.mt.order.entity.UserPoi;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserPoiMapStruct extends BaseMapStruct<UserPoiDto, UserPoi> {
    UserPoiMapStruct INSTANCE = Mappers.getMapper(UserPoiMapStruct.class);
}
