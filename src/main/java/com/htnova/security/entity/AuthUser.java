package com.htnova.security.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.htnova.common.base.BaseEntity;
import com.htnova.mt.order.entity.UserPoi;
import com.htnova.system.manage.entity.Permission;
import com.htnova.system.manage.entity.Role;
import com.htnova.system.manage.entity.User;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AuthUser extends BaseEntity {
    /** 登录名 */
    private String username;

    /** 密码 */
    private String password;

    /** 姓名 */
    private String name;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 地址 */
    private String address;

    /** 性别 */
    private String sex;

    /** 头像 */
    private String avatar;

    /** 状态（启用禁用） */
    private User.UserStatus status;

    /** 部门id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;

    /** 部门ids（包含自身） */
    private String deptIds;

    /** 部门名称 */
    private String deptName;

    /** 角色 */
    private List<Role> roleList;

    /** 权限 */
    private List<Permission> permissionList;

    /** 门店列表 */
    private List<UserPoi> poiShopList;

    public List<String> getRoles() {
        if (CollectionUtils.isEmpty(roleList)) return Collections.emptyList();
        return roleList.stream().map(Role::getCode).collect(Collectors.toList());
    }

    public boolean isSuperAdmin() {
        if (CollectionUtils.isEmpty(roleList)) return false;
        return roleList.stream().map(Role::getCode).anyMatch(item -> Role.RoleCode.SuperAdmin.toString().equals(item));
    }

    public boolean isAdmin() {
        if (CollectionUtils.isEmpty(roleList)) return false;
        return roleList.stream().map(Role::getCode).anyMatch(item -> Role.RoleCode.Admin.toString().equals(item));
    }
}
