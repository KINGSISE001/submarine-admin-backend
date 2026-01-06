package com.htnova.mt.order.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htnova.mt.order.dto.CompletedOrderDto;
import com.htnova.mt.order.entity.Completedorder;
import com.htnova.mt.order.mapper.CompletedorderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CompletedOrderService extends ServiceImpl<CompletedorderMapper, Completedorder> {

    @Resource
    private CompletedorderMapper completedOrderMapper;

    @Transactional(readOnly = true)
    public IPage<Completedorder> findCompletedOrderList(CompletedOrderDto completedOrderDto, IPage<Void> xPage) {
        return completedOrderMapper.findPage(xPage, completedOrderDto);
    }

    @Transactional(readOnly = true)
    public List<Completedorder> findCompletedOrderList(CompletedOrderDto completedOrderDto) {
        return completedOrderMapper.findList(completedOrderDto);
    }

    @Transactional(readOnly = true)
    public Completedorder getCompletedOrderById(long id) {
        return completedOrderMapper.selectById(id);
    }



}
