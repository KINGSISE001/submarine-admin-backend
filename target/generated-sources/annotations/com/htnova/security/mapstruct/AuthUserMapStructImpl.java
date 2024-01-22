package com.htnova.security.mapstruct;

import com.htnova.mt.order.entity.UserPoi;
import com.htnova.security.entity.AuthUser;
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
public class AuthUserMapStructImpl implements AuthUserMapStruct {

    @Override
    public AuthUser toAuthUser(User user) {
        if ( user == null ) {
            return null;
        }

        AuthUser authUser = new AuthUser();

        authUser.setId( user.getId() );
        authUser.setRemark( user.getRemark() );
        authUser.setCreateBy( user.getCreateBy() );
        authUser.setCreatorName( user.getCreatorName() );
        authUser.setCreateTime( user.getCreateTime() );
        authUser.setUpdateBy( user.getUpdateBy() );
        authUser.setUpdateTime( user.getUpdateTime() );
        authUser.setDelFlag( user.getDelFlag() );
        authUser.setUsername( user.getUsername() );
        authUser.setPassword( user.getPassword() );
        authUser.setName( user.getName() );
        authUser.setPhone( user.getPhone() );
        authUser.setEmail( user.getEmail() );
        authUser.setAddress( user.getAddress() );
        authUser.setSex( user.getSex() );
        authUser.setAvatar( user.getAvatar() );
        authUser.setStatus( user.getStatus() );
        authUser.setDeptId( user.getDeptId() );
        authUser.setDeptIds( user.getDeptIds() );
        authUser.setDeptName( user.getDeptName() );
        List<Role> list = user.getRoleList();
        if ( list != null ) {
            authUser.setRoleList( new ArrayList<Role>( list ) );
        }
        List<Permission> list1 = user.getPermissionList();
        if ( list1 != null ) {
            authUser.setPermissionList( new ArrayList<Permission>( list1 ) );
        }
        List<UserPoi> list2 = user.getPoiShopList();
        if ( list2 != null ) {
            authUser.setPoiShopList( new ArrayList<UserPoi>( list2 ) );
        }

        return authUser;
    }
}
