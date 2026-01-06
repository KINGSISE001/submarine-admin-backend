package com.htnova.mt.order.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htnova.common.converter.DtoConverter;
import com.htnova.common.dto.XPage;
import com.htnova.mt.order.dto.CompletedOrderDto;
import com.htnova.mt.order.entity.Completedorder;
import com.htnova.mt.order.mapstruct.CompletedOrderMapStruct;
import com.htnova.mt.order.service.CompletedOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @menu 订单
 */
@Slf4j
@RestController
@RequestMapping("/completedorder")
public class CompletedorderController {

    @Resource
    private CompletedOrderService CompletedorderService;

    /**
     * 分页查询
     */
    //@PreAuthorize("hasAnyAuthority('Completedorder.find')")
    @GetMapping("/list/page")
    public XPage<Completedorder> findListByPage(CompletedOrderDto CompletedOrderDto, XPage<Void> xPage) {
        IPage<Completedorder> CompletedorderPage = CompletedorderService.findCompletedOrderList(CompletedOrderDto, XPage.toIPage(xPage));
        log.info(JSON.toJSONString(XPage.fromIPage(CompletedorderPage)) );
        return XPage.fromIPage(CompletedorderPage);
    }

    /**
     * 查询
     */
    @PreAuthorize("hasAnyAuthority('Completedorder.find')")
    @GetMapping("/list/all")
    public List<CompletedOrderDto> findList(CompletedOrderDto CompletedOrderDto) {
        List<Completedorder> CompletedorderList = CompletedorderService.findCompletedOrderList(CompletedOrderDto);
        return DtoConverter.toDto(CompletedorderList, CompletedOrderMapStruct.INSTANCE);
    }

    /**
     * 详情
     */
    @PreAuthorize("hasAnyAuthority('Completedorder.find')")
    @GetMapping("/detail/{id}")
    public CompletedOrderDto getById(@PathVariable long id) {
        Completedorder Completedorder = CompletedorderService.getCompletedOrderById(id);
        return DtoConverter.toDto(Completedorder, CompletedOrderMapStruct.INSTANCE);
    }




}
