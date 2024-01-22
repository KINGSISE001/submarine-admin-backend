package com.htnova.system.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htnova.system.manage.dto.UserDto;
import com.htnova.system.manage.entity.User;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {
    IPage<User> findPage(IPage<Void> xPage, @Param("userDto") UserDto userDto);

    List<User> findList(@Param("userDto") UserDto userDto);

    /** 修改商户余额
     * @param id  <h>用户id</h>
     * @param merchant_balance  <h>商户余额</h>
     * **/
    @Select("update t_sys_user set merchant_balance =  #{merchant_balance} where id = #{id}")
    void updateMerchantBalance (@Param("id") long id,@Param("merchant_balance") BigDecimal merchant_balance);

    /** 修改运费余额
     * @param id  <h>用户id</h>
     * @param freight_balance  <h>运费余额</h>
     *
     * **/
    @Select("update t_sys_user set freight_balance =  #{freight_balance} where id = #{id}")
    void updateFreightBalance (@Param("id") long id,@Param("freight_balance") BigDecimal freight_balance);

    @Select("select id,username,merchant_balance,freight_balance from t_sys_user where id = #{id} and del_flag =0")
    User findUserBalanceById(long id);
}
