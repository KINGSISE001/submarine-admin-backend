package com.htnova.mt.order.mapstruct;

import com.htnova.mt.order.dto.UserPoiDto;
import com.htnova.mt.order.entity.UserPoi;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-18T11:06:29+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_341 (Oracle Corporation)"
)
public class UserPoiMapStructImpl implements UserPoiMapStruct {

    @Override
    public UserPoi toEntity(UserPoiDto dto) {
        if ( dto == null ) {
            return null;
        }

        UserPoi userPoi = new UserPoi();

        userPoi.setId( dto.getId() );
        userPoi.setRemark( dto.getRemark() );
        userPoi.setCreateBy( dto.getCreateBy() );
        userPoi.setCreatorName( dto.getCreatorName() );
        userPoi.setCreateTime( dto.getCreateTime() );
        userPoi.setUpdateBy( dto.getUpdateBy() );
        userPoi.setUpdateTime( dto.getUpdateTime() );
        userPoi.setDelFlag( dto.getDelFlag() );
        userPoi.setUId( dto.getUId() );
        userPoi.setUserName( dto.getUserName() );
        userPoi.setName( dto.getName() );
        userPoi.setAppId( dto.getAppId() );
        userPoi.setPoiId( dto.getPoiId() );
        userPoi.setPoiName( dto.getPoiName() );
        userPoi.setPoiStatus( dto.getPoiStatus() );
        userPoi.setEleId( dto.getEleId() );
        userPoi.setEleName( dto.getEleName() );
        userPoi.setEleStatus( dto.getEleStatus() );
        userPoi.setAutoOrders( dto.getAutoOrders() );

        return userPoi;
    }

    @Override
    public UserPoiDto toDto(UserPoi entity) {
        if ( entity == null ) {
            return null;
        }

        UserPoiDto userPoiDto = new UserPoiDto();

        userPoiDto.setId( entity.getId() );
        userPoiDto.setRemark( entity.getRemark() );
        userPoiDto.setCreateBy( entity.getCreateBy() );
        userPoiDto.setCreatorName( entity.getCreatorName() );
        userPoiDto.setCreateTime( entity.getCreateTime() );
        userPoiDto.setUpdateBy( entity.getUpdateBy() );
        userPoiDto.setUpdateTime( entity.getUpdateTime() );
        userPoiDto.setDelFlag( entity.getDelFlag() );
        userPoiDto.setUId( entity.getUId() );
        userPoiDto.setUserName( entity.getUserName() );
        userPoiDto.setName( entity.getName() );
        userPoiDto.setAppId( entity.getAppId() );
        userPoiDto.setPoiId( entity.getPoiId() );
        userPoiDto.setPoiName( entity.getPoiName() );
        userPoiDto.setPoiStatus( entity.getPoiStatus() );
        userPoiDto.setEleId( entity.getEleId() );
        userPoiDto.setEleName( entity.getEleName() );
        userPoiDto.setEleStatus( entity.getEleStatus() );
        userPoiDto.setAutoOrders( entity.getAutoOrders() );

        return userPoiDto;
    }

    @Override
    public List<UserPoi> toEntity(List<UserPoiDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<UserPoi> list = new ArrayList<UserPoi>( dtoList.size() );
        for ( UserPoiDto userPoiDto : dtoList ) {
            list.add( toEntity( userPoiDto ) );
        }

        return list;
    }

    @Override
    public List<UserPoiDto> toDto(List<UserPoi> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<UserPoiDto> list = new ArrayList<UserPoiDto>( entityList.size() );
        for ( UserPoi userPoi : entityList ) {
            list.add( toDto( userPoi ) );
        }

        return list;
    }
}
