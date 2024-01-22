package com.htnova.system.tool.mapstruct;

import com.htnova.system.tool.dto.LocationDto;
import com.htnova.system.tool.entity.Location;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-18T11:06:29+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_341 (Oracle Corporation)"
)
public class LocationMapStructImpl implements LocationMapStruct {

    @Override
    public Location toEntity(LocationDto dto) {
        if ( dto == null ) {
            return null;
        }

        Location location = new Location();

        location.setId( dto.getId() );
        location.setRemark( dto.getRemark() );
        location.setCreateBy( dto.getCreateBy() );
        location.setCreatorName( dto.getCreatorName() );
        location.setCreateTime( dto.getCreateTime() );
        location.setUpdateBy( dto.getUpdateBy() );
        location.setUpdateTime( dto.getUpdateTime() );
        location.setDelFlag( dto.getDelFlag() );
        location.setPid( dto.getPid() );
        location.setPids( dto.getPids() );
        location.setChildren( toEntity( dto.getChildren() ) );
        location.setDeep( dto.getDeep() );
        location.setName( dto.getName() );
        location.setPoint( dto.getPoint() );

        return location;
    }

    @Override
    public LocationDto toDto(Location entity) {
        if ( entity == null ) {
            return null;
        }

        LocationDto locationDto = new LocationDto();

        locationDto.setId( entity.getId() );
        locationDto.setRemark( entity.getRemark() );
        locationDto.setCreateBy( entity.getCreateBy() );
        locationDto.setCreatorName( entity.getCreatorName() );
        locationDto.setCreateTime( entity.getCreateTime() );
        locationDto.setUpdateBy( entity.getUpdateBy() );
        locationDto.setUpdateTime( entity.getUpdateTime() );
        locationDto.setDelFlag( entity.getDelFlag() );
        locationDto.setPid( entity.getPid() );
        locationDto.setPids( entity.getPids() );
        locationDto.setChildren( toDto( entity.getChildren() ) );
        locationDto.setDeep( entity.getDeep() );
        locationDto.setName( entity.getName() );
        locationDto.setPoint( entity.getPoint() );

        return locationDto;
    }

    @Override
    public List<Location> toEntity(List<LocationDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Location> list = new ArrayList<Location>( dtoList.size() );
        for ( LocationDto locationDto : dtoList ) {
            list.add( toEntity( locationDto ) );
        }

        return list;
    }

    @Override
    public List<LocationDto> toDto(List<Location> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<LocationDto> list = new ArrayList<LocationDto>( entityList.size() );
        for ( Location location : entityList ) {
            list.add( toDto( location ) );
        }

        return list;
    }
}
