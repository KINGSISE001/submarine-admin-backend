package com.htnova.system.manage.mapstruct;

import com.htnova.system.manage.dto.PermissionDto;
import com.htnova.system.manage.entity.Permission;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-18T11:06:29+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_341 (Oracle Corporation)"
)
public class PermissionMapStructImpl implements PermissionMapStruct {

    @Override
    public Permission toEntity(PermissionDto dto) {
        if ( dto == null ) {
            return null;
        }

        Permission permission = new Permission();

        permission.setId( dto.getId() );
        permission.setRemark( dto.getRemark() );
        permission.setCreateBy( dto.getCreateBy() );
        permission.setCreatorName( dto.getCreatorName() );
        permission.setCreateTime( dto.getCreateTime() );
        permission.setUpdateBy( dto.getUpdateBy() );
        permission.setUpdateTime( dto.getUpdateTime() );
        permission.setDelFlag( dto.getDelFlag() );
        permission.setPid( dto.getPid() );
        permission.setPids( dto.getPids() );
        permission.setChildren( toEntity( dto.getChildren() ) );
        permission.setType( dto.getType() );
        permission.setFrontType( dto.getFrontType() );
        permission.setName( dto.getName() );
        permission.setValue( dto.getValue() );

        return permission;
    }

    @Override
    public PermissionDto toDto(Permission entity) {
        if ( entity == null ) {
            return null;
        }

        PermissionDto permissionDto = new PermissionDto();

        permissionDto.setId( entity.getId() );
        permissionDto.setRemark( entity.getRemark() );
        permissionDto.setCreateBy( entity.getCreateBy() );
        permissionDto.setCreatorName( entity.getCreatorName() );
        permissionDto.setCreateTime( entity.getCreateTime() );
        permissionDto.setUpdateBy( entity.getUpdateBy() );
        permissionDto.setUpdateTime( entity.getUpdateTime() );
        permissionDto.setDelFlag( entity.getDelFlag() );
        permissionDto.setPid( entity.getPid() );
        permissionDto.setPids( entity.getPids() );
        permissionDto.setChildren( toDto( entity.getChildren() ) );
        permissionDto.setType( entity.getType() );
        permissionDto.setFrontType( entity.getFrontType() );
        permissionDto.setName( entity.getName() );
        permissionDto.setValue( entity.getValue() );

        return permissionDto;
    }

    @Override
    public List<Permission> toEntity(List<PermissionDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Permission> list = new ArrayList<Permission>( dtoList.size() );
        for ( PermissionDto permissionDto : dtoList ) {
            list.add( toEntity( permissionDto ) );
        }

        return list;
    }

    @Override
    public List<PermissionDto> toDto(List<Permission> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<PermissionDto> list = new ArrayList<PermissionDto>( entityList.size() );
        for ( Permission permission : entityList ) {
            list.add( toDto( permission ) );
        }

        return list;
    }
}
