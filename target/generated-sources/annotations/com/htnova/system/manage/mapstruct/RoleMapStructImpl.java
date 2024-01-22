package com.htnova.system.manage.mapstruct;

import com.htnova.system.manage.dto.PermissionDto;
import com.htnova.system.manage.dto.RoleDto;
import com.htnova.system.manage.entity.Permission;
import com.htnova.system.manage.entity.Role;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-18T11:06:29+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_341 (Oracle Corporation)"
)
public class RoleMapStructImpl implements RoleMapStruct {

    @Override
    public Role toEntity(RoleDto dto) {
        if ( dto == null ) {
            return null;
        }

        Role role = new Role();

        role.setId( dto.getId() );
        role.setRemark( dto.getRemark() );
        role.setCreateBy( dto.getCreateBy() );
        role.setCreatorName( dto.getCreatorName() );
        role.setCreateTime( dto.getCreateTime() );
        role.setUpdateBy( dto.getUpdateBy() );
        role.setUpdateTime( dto.getUpdateTime() );
        role.setDelFlag( dto.getDelFlag() );
        role.setName( dto.getName() );
        role.setCode( dto.getCode() );
        role.setOrgAdminDisplay( dto.getOrgAdminDisplay() );
        role.setPermissionList( permissionDtoListToPermissionList( dto.getPermissionList() ) );

        return role;
    }

    @Override
    public RoleDto toDto(Role entity) {
        if ( entity == null ) {
            return null;
        }

        RoleDto roleDto = new RoleDto();

        roleDto.setId( entity.getId() );
        roleDto.setRemark( entity.getRemark() );
        roleDto.setCreateBy( entity.getCreateBy() );
        roleDto.setCreatorName( entity.getCreatorName() );
        roleDto.setCreateTime( entity.getCreateTime() );
        roleDto.setUpdateBy( entity.getUpdateBy() );
        roleDto.setUpdateTime( entity.getUpdateTime() );
        roleDto.setDelFlag( entity.getDelFlag() );
        roleDto.setName( entity.getName() );
        roleDto.setCode( entity.getCode() );
        roleDto.setOrgAdminDisplay( entity.getOrgAdminDisplay() );
        roleDto.setPermissionList( permissionListToPermissionDtoList( entity.getPermissionList() ) );

        return roleDto;
    }

    @Override
    public List<Role> toEntity(List<RoleDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Role> list = new ArrayList<Role>( dtoList.size() );
        for ( RoleDto roleDto : dtoList ) {
            list.add( toEntity( roleDto ) );
        }

        return list;
    }

    @Override
    public List<RoleDto> toDto(List<Role> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<RoleDto> list = new ArrayList<RoleDto>( entityList.size() );
        for ( Role role : entityList ) {
            list.add( toDto( role ) );
        }

        return list;
    }

    protected List<Permission> permissionDtoListToPermissionList(List<PermissionDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Permission> list1 = new ArrayList<Permission>( list.size() );
        for ( PermissionDto permissionDto : list ) {
            list1.add( permissionDtoToPermission( permissionDto ) );
        }

        return list1;
    }

    protected Permission permissionDtoToPermission(PermissionDto permissionDto) {
        if ( permissionDto == null ) {
            return null;
        }

        Permission permission = new Permission();

        permission.setId( permissionDto.getId() );
        permission.setRemark( permissionDto.getRemark() );
        permission.setCreateBy( permissionDto.getCreateBy() );
        permission.setCreatorName( permissionDto.getCreatorName() );
        permission.setCreateTime( permissionDto.getCreateTime() );
        permission.setUpdateBy( permissionDto.getUpdateBy() );
        permission.setUpdateTime( permissionDto.getUpdateTime() );
        permission.setDelFlag( permissionDto.getDelFlag() );
        permission.setPid( permissionDto.getPid() );
        permission.setPids( permissionDto.getPids() );
        permission.setChildren( permissionDtoListToPermissionList( permissionDto.getChildren() ) );
        permission.setType( permissionDto.getType() );
        permission.setFrontType( permissionDto.getFrontType() );
        permission.setName( permissionDto.getName() );
        permission.setValue( permissionDto.getValue() );

        return permission;
    }

    protected List<PermissionDto> permissionListToPermissionDtoList(List<Permission> list) {
        if ( list == null ) {
            return null;
        }

        List<PermissionDto> list1 = new ArrayList<PermissionDto>( list.size() );
        for ( Permission permission : list ) {
            list1.add( permissionToPermissionDto( permission ) );
        }

        return list1;
    }

    protected PermissionDto permissionToPermissionDto(Permission permission) {
        if ( permission == null ) {
            return null;
        }

        PermissionDto permissionDto = new PermissionDto();

        permissionDto.setId( permission.getId() );
        permissionDto.setRemark( permission.getRemark() );
        permissionDto.setCreateBy( permission.getCreateBy() );
        permissionDto.setCreatorName( permission.getCreatorName() );
        permissionDto.setCreateTime( permission.getCreateTime() );
        permissionDto.setUpdateBy( permission.getUpdateBy() );
        permissionDto.setUpdateTime( permission.getUpdateTime() );
        permissionDto.setDelFlag( permission.getDelFlag() );
        permissionDto.setPid( permission.getPid() );
        permissionDto.setPids( permission.getPids() );
        permissionDto.setChildren( permissionListToPermissionDtoList( permission.getChildren() ) );
        permissionDto.setType( permission.getType() );
        permissionDto.setFrontType( permission.getFrontType() );
        permissionDto.setName( permission.getName() );
        permissionDto.setValue( permission.getValue() );

        return permissionDto;
    }
}
