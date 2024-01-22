package com.htnova.system.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.htnova.common.base.BaseEntity;
import com.htnova.mt.order.entity.UserPoi;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@TableName("t_sys_user")
public class User extends BaseEntity {
    /** 登录名 */
    private String username;

    /** 密码 */
    @JsonIgnore
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

    /** 运费余额 */
    private BigDecimal freightBalance ;

    /** 商户余额 */
    private BigDecimal merchantBalance;

    public BigDecimal getFreightBalance() {
        return freightBalance == null ? BigDecimal.ZERO : freightBalance;
    }

    public BigDecimal getMerchantBalance() {
        return merchantBalance == null? BigDecimal.ZERO : merchantBalance;
    }

    /** 状态（启用禁用） */
    private UserStatus status;

    /** 部门id */
    private Long deptId;

    /** 部门ids（包含自身） */
    private String deptIds;

    /** 部门名称 */
    private String deptName;

    /** APP登录密钥 */
    @JsonIgnore
    private String token;

    /** APP上次访问时间 */
    @JsonIgnore
    private LocalDateTime lastAccessDate;

    /** 角色 */
    @TableField(exist = false)
    private List<Role> roleList;

    /** 权限 */
    @TableField(exist = false)
    private List<Permission> permissionList;

    /** 门店列表 */
    @TableField(exist = false)
    private List<UserPoi> poiShopList;

    public enum UserStatus {
        enable,
        disable,
    }

    public SaveEvent saveEvent() {
        return new SaveEvent();
    }

    public class SaveEvent {

        public User getUser() {
            return User.this;
        }
    }

    public DeleteEvent deleteEvent() {
        return new DeleteEvent();
    }

    public class DeleteEvent {

        public User getUser() {
            return User.this;
        }
    }
}
