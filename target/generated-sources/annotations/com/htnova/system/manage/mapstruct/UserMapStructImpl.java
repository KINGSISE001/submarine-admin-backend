package com.htnova.system.manage.mapstruct;

import com.htnova.system.manage.dto.PermissionDto;
import com.htnova.system.manage.dto.RoleDto;
import com.htnova.system.manage.dto.UserDto;
import com.htnova.system.manage.entity.Permission;
import com.htnova.system.manage.entity.Role;
import com.htnova.system.manage.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-18T11:06:29+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_341 (Oracle Corporation)"
)
public class UserMapStructImpl implements UserMapStruct {

    @Override
    public User toEntity(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setId( dto.getId() );
        user.setRemark( dto.getRemark() );
        user.setCreateBy( dto.getCreateBy() );
        user.setCreatorName( dto.getCreatorName() );
        user.setCreateTime( dto.getCreateTime() );
        user.setUpdateBy( dto.getUpdateBy() );
        user.setUpdateTime( dto.getUpdateTime() );
        user.setDelFlag( dto.getDelFlag() );
        user.setUsername( dto.getUsername() );
        user.setPassword( dto.getPassword() );
        user.setName( dto.getName() );
        user.setPhone( dto.getPhone() );
        user.setEmail( dto.getEmail() );
        user.setAddress( dto.getAddress() );
        user.setSex( dto.getSex() );
        user.setAvatar( dto.getAvatar() );
        user.setStatus( dto.getStatus() );
        user.setDeptId( dto.getDeptId() );
        user.setDeptIds( dto.getDeptIds() );
        user.setDeptName( dto.getDeptName() );
        user.setRoleList( roleDtoListToRoleList( dto.getRoleList() ) );
        user.setPermissionList( permissionDtoListToPermissionList( dto.getPermissionList() ) );

        toDtoRoleIdList( dto, user );

        return user;
    }

    @Override
    public UserDto toDto(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( entity.getId() );
        userDto.setRemark( entity.getRemark() );
        userDto.setCreateBy( entity.getCreateBy() );
        userDto.setCreatorName( entity.getCreatorName() );
        userDto.setCreateTime( entity.getCreateTime() );
        userDto.setUpdateBy( entity.getUpdateBy() );
        userDto.setUpdateTime( entity.getUpdateTime() );
        userDto.setDelFlag( entity.getDelFlag() );
        userDto.setUsername( entity.getUsername() );
        userDto.setPassword( entity.getPassword() );
        userDto.setName( entity.getName() );
        userDto.setPhone( entity.getPhone() );
        userDto.setEmail( entity.getEmail() );
        userDto.setAddress( entity.getAddress() );
        userDto.setSex( entity.getSex() );
        userDto.setAvatar( entity.getAvatar() );
        userDto.setStatus( entity.getStatus() );
        userDto.setDeptId( entity.getDeptId() );
        userDto.setDeptIds( entity.getDeptIds() );
        userDto.setDeptName( entity.getDeptName() );
        userDto.setRoleList( roleListToRoleDtoList( entity.getRoleList() ) );
        userDto.setPermissionList( permissionListToPermissionDtoList( entity.getPermissionList() ) );

        toEntityRoleIdList( entity, userDto );

        return userDto;
    }

    @Override
    public List<User> toEntity(List<UserDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( dtoList.size() );
        for ( UserDto userDto : dtoList ) {
            list.add( toEntity( userDto ) );
        }

        return list;
    }

    @Override
    public List<UserDto> toDto(List<User> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( entityList.size() );
        for ( User user : entityList ) {
            list.add( toDto( user ) );
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

    protected Role roleDtoToRole(RoleDto roleDto) {
        if ( roleDto == null ) {
            return null;
        }

        Role role = new Role();

        role.setId( roleDto.getId() );
        role.setRemark( roleDto.getRemark() );
        role.setCreateBy( roleDto.getCreateBy() );
        role.setCreatorName( roleDto.getCreatorName() );
        role.setCreateTime( roleDto.getCreateTime() );
        role.setUpdateBy( roleDto.getUpdateBy() );
        role.setUpdateTime( roleDto.getUpdateTime() );
        role.setDelFlag( roleDto.getDelFlag() );
        role.setName( roleDto.getName() );
        role.setCode( roleDto.getCode() );
        role.setOrgAdminDisplay( roleDto.getOrgAdminDisplay() );
        role.setPermissionList( permissionDtoListToPermissionList( roleDto.getPermissionList() ) );

        return role;
    }

    protected List<Role> roleDtoListToRoleList(List<RoleDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Role> list1 = new ArrayList<Role>( list.size() );
        for ( RoleDto roleDto : list ) {
            list1.add( roleDtoToRole( roleDto ) );
        }

        return list1;
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

    protected RoleDto roleToRoleDto(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleDto roleDto = new RoleDto();

        roleDto.setId( role.getId() );
        roleDto.setRemark( role.getRemark() );
        roleDto.setCreateBy( role.getCreateBy() );
        roleDto.setCreatorName( role.getCreatorName() );
        roleDto.setCreateTime( role.getCreateTime() );
        roleDto.setUpdateBy( role.getUpdateBy() );
        roleDto.setUpdateTime( role.getUpdateTime() );
        roleDto.setDelFlag( role.getDelFlag() );
        roleDto.setName( role.getName() );
        roleDto.setCode( role.getCode() );
        roleDto.setOrgAdminDisplay( role.getOrgAdminDisplay() );
        roleDto.setPermissionList( permissionListToPermissionDtoList( role.getPermissionList() ) );

        return roleDto;
    }

    protected List<RoleDto> roleListToRoleDtoList(List<Role> list) {
        if ( list == null ) {
            return null;
        }

        List<RoleDto> list1 = new ArrayList<RoleDto>( list.size() );
        for ( Role role : list ) {
            list1.add( roleToRoleDto( role ) );
        }

        return list1;
    }
}
