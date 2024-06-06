package com.htnova.mt.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htnova.common.constant.ResultStatus;
import com.htnova.common.converter.DtoConverter;
import com.htnova.common.dto.Result;
import com.htnova.common.dto.XPage;
import com.htnova.mt.order.dto.ShopDto;
import com.htnova.mt.order.entity.Shop;
import com.htnova.mt.order.mapstruct.ShopMapStruct;
import com.htnova.mt.order.service.ShopService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @menu 门店表
 */
@RestController
@RequestMapping("/shop")
public class ShopController {

    @Resource
    private ShopService shopService;

    /**
     * 分页查询
     */
    @PreAuthorize("hasAnyAuthority('shop.find')")
    @GetMapping("/list/page")
    public XPage<ShopDto> findListByPage(ShopDto shopDto, XPage<Void> xPage) {
        IPage<Shop> shopPage = shopService.findShopList(shopDto, XPage.toIPage(xPage));
        return DtoConverter.toDto(shopPage, ShopMapStruct.INSTANCE);
    }

    /**
     * 查询
     */
    @PreAuthorize("hasAnyAuthority('shop.find')")
    @GetMapping("/list/all")
    public List<ShopDto> findList(ShopDto shopDto) {
        List<Shop> shopList = shopService.findShopList(shopDto);
        return DtoConverter.toDto(shopList, ShopMapStruct.INSTANCE);
    }

    /**
     * 详情
     */
    @PreAuthorize("hasAnyAuthority('shop.find')")
    @GetMapping("/detail/{id}")
    public ShopDto getById(@PathVariable long id) {
        Shop shop = shopService.getShopById(id);
        return DtoConverter.toDto(shop, ShopMapStruct.INSTANCE);
    }

    /**
     * 保存
     */
    @PreAuthorize("hasAnyAuthority('shop.add', 'shop.edit')")
    @PostMapping("/save")
    public Result<Void> save(@Valid @RequestBody ShopDto shopDto) {
        shopService.saveShop(DtoConverter.toEntity(shopDto, ShopMapStruct.INSTANCE));
        return Result.build(ResultStatus.SAVE_SUCCESS);
    }

    /**
     * 删除
     */
    @PreAuthorize("hasAnyAuthority('shop.del')")
    @DeleteMapping("/del/{id}")
    public Result<Void> delete(@PathVariable long id) {
        shopService.deleteShop(id);
        return Result.build(ResultStatus.DELETE_SUCCESS);
    }
}
