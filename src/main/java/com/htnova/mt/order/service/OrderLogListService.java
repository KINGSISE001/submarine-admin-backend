package com.htnova.mt.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htnova.common.converter.DtoConverter;
import com.htnova.mt.order.dto.OrderLogListDto;
import com.htnova.mt.order.entity.OrderLogList;
import com.htnova.mt.order.mapper.OrderLogListMapper;
import java.util.List;
import javax.annotation.Resource;

import com.htnova.mt.order.mapstruct.OrderLogListMapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderLogListService extends ServiceImpl<OrderLogListMapper, OrderLogList> {
    @Resource
    private OrderLogListMapper orderLogListMapper;

    @Transactional(readOnly = true)
    public IPage<OrderLogList> findOrderLogListList(OrderLogListDto orderLogListDto, IPage<Void> xPage) {
        return orderLogListMapper.findPage(xPage, orderLogListDto);
    }

    @Transactional(readOnly = true)
    public List<OrderLogList> findOrderLogListList(OrderLogListDto orderLogListDto) {
        return orderLogListMapper.findList(orderLogListDto);
    }

    @Transactional
    public void saveOrderLogList(OrderLogList orderLogList) {
        super.save(orderLogList);
    }

    @Transactional(readOnly = true)
    public OrderLogList getOrderLogListById(long id) {
        return orderLogListMapper.selectById(id);
    }

    @Transactional
    public void deleteOrderLogList(Long id) {
        super.removeById(id);
    }


   public List<OrderLogListDto> findListByOrderId (String orderId) {
        List<OrderLogList> orderLogListList= orderLogListMapper.findListByOrderId(orderId);
        return DtoConverter.toDto(orderLogListList, OrderLogListMapStruct.INSTANCE);
    };
}
