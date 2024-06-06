package com.htnova.mt.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htnova.mt.order.dto.ShopDto;
import com.htnova.mt.order.entity.Shop;
import com.htnova.mt.order.mapper.ShopMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ShopService extends ServiceImpl<ShopMapper, Shop> {

    @Resource
    private ShopMapper shopMapper;

    @Transactional(readOnly = true)
    public IPage<Shop> findShopList(ShopDto shopDto, IPage<Void> xPage) {
        return shopMapper.findPage(xPage, shopDto);
    }

    @Transactional(readOnly = true)
    public List<Shop> findShopList(ShopDto shopDto) {
        return shopMapper.findList(shopDto);
    }

    @Transactional
    public void saveShop(Shop shop) {
        super.saveOrUpdate(shop);
    }

    @Transactional(readOnly = true)
    public Shop getShopById(long id) {
        return shopMapper.selectById(id);
    }

    @Transactional
    public void deleteShop(Long id) {
        super.removeById(id);
    }
}
