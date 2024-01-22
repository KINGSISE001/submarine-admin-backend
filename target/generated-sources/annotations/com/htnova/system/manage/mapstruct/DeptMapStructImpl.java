package com.htnova.system.manage.mapstruct;

import com.htnova.system.manage.dto.DeptDto;
import com.htnova.system.manage.entity.Dept;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-18T11:06:29+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_341 (Oracle Corporation)"
)
public class DeptMapStructImpl implements DeptMapStruct {

    @Override
    public Dept toEntity(DeptDto dto) {
        if ( dto == null ) {
            return null;
        }

        Dept dept = new Dept();

        dept.setId( dto.getId() );
        dept.setRemark( dto.getRemark() );
        dept.setCreateBy( dto.getCreateBy() );
        dept.setCreatorName( dto.getCreatorName() );
        dept.setCreateTime( dto.getCreateTime() );
        dept.setUpdateBy( dto.getUpdateBy() );
        dept.setUpdateTime( dto.getUpdateTime() );
        dept.setDelFlag( dto.getDelFlag() );
        dept.setPid( dto.getPid() );
        dept.setPids( dto.getPids() );
        dept.setChildren( toEntity( dto.getChildren() ) );
        dept.setName( dto.getName() );
        dept.setCode( dto.getCode() );

        return dept;
    }

    @Override
    public DeptDto toDto(Dept entity) {
        if ( entity == null ) {
            return null;
        }

        DeptDto deptDto = new DeptDto();

        deptDto.setId( entity.getId() );
        deptDto.setRemark( entity.getRemark() );
        deptDto.setCreateBy( entity.getCreateBy() );
        deptDto.setCreatorName( entity.getCreatorName() );
        deptDto.setCreateTime( entity.getCreateTime() );
        deptDto.setUpdateBy( entity.getUpdateBy() );
        deptDto.setUpdateTime( entity.getUpdateTime() );
        deptDto.setDelFlag( entity.getDelFlag() );
        deptDto.setPid( entity.getPid() );
        deptDto.setPids( entity.getPids() );
        deptDto.setChildren( toDto( entity.getChildren() ) );
        deptDto.setName( entity.getName() );
        deptDto.setCode( entity.getCode() );

        return deptDto;
    }

    @Override
    public List<Dept> toEntity(List<DeptDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Dept> list = new ArrayList<Dept>( dtoList.size() );
        for ( DeptDto deptDto : dtoList ) {
            list.add( toEntity( deptDto ) );
        }

        return list;
    }

    @Override
    public List<DeptDto> toDto(List<Dept> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<DeptDto> list = new ArrayList<DeptDto>( entityList.size() );
        for ( Dept dept : entityList ) {
            list.add( toDto( dept ) );
        }

        return list;
    }
}
