package com.htnova.mt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htnova.mt.order.dto.ShopDto;
import com.htnova.mt.order.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopMapper extends BaseMapper<Shop> {

    IPage<Shop> findPage(IPage<Void> xPage, @Param("shopDto") ShopDto shopDto);

    List<Shop> findList(@Param("shopDto") ShopDto shopDto);

}
