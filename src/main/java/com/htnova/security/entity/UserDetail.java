package com.htnova.security.entity;

import com.htnova.mt.order.entity.UserPoi;
import com.htnova.system.manage.entity.Permission;
import com.htnova.system.manage.entity.User;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail implements UserDetails {
    private User user;

    //.ofNullable(user.getPermissionList())

    /**
     *   public Collection<? extends GrantedAuthority> getAuthorities() {
     *         return Optional
     *             .ofNullable(user.getPermissionList())
     *             .orElseGet(ArrayList::new)
     *             .stream()
     *             .filter(item -> Objects.nonNull(item.getPoiId()))
     *             .filter(item -> Permission.PermissionType.button == item.getType())
     *             .map(permission -> new SimpleGrantedAuthority(permission.getValue()))
     *             .collect(Collectors.toList());
     *     }
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional
            .ofNullable(user.getPermissionList())
            .orElseGet(ArrayList::new)
            .stream()
            .filter(item -> Objects.nonNull(item.getValue()))
            .filter(item -> Permission.PermissionType.button == item.getType())
            .map(permission -> new SimpleGrantedAuthority(permission.getValue()))
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return User.UserStatus.enable.equals(user.getStatus());
    }

    public static UserDetail createByUser(User user) {
        return new UserDetail(user);
    }
}
