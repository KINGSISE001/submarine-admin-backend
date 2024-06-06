package com.htnova.mt.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.htnova.common.dto.EasyUIDataGridResult;
import com.htnova.mt.order.entity.Completedorder;
import com.htnova.mt.order.entity.Detail;
import com.htnova.mt.order.entity.OrderStatus;
import com.htnova.mt.order.entity.SettlementInformation;
import com.htnova.mt.order.mapper.CompletedorderMapper;
import com.htnova.mt.order.mapper.DetailMapper;
import com.htnova.mt.order.mapper.SettlementInformationMapper;
import com.htnova.mt.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
@Service
public class OrderServicelmpl implements OrderService {
    @Resource
    private CompletedorderMapper completedorderMapper;

    @Resource
    private DetailMapper detailMapper;

    @Resource
    private SettlementInformationMapper settlementinformationMapper;

    @Override
    public int insertCompletedorder(Completedorder completedorder) {
        return completedorderMapper.insertCompletedorder(completedorder);
    }

    @Override
    public int insertDetail(List<Detail> lists) {
        return detailMapper.insertDetail(lists);
    }

    @Override
    public int insertsettlementinformation(SettlementInformation settlementinformation) {
        LambdaQueryWrapper<SettlementInformation> qw = new LambdaQueryWrapper<>();
        qw.eq(SettlementInformation::getOrderId, settlementinformation.getOrderId());
        SettlementInformation settlementInformation = Optional.ofNullable(settlementinformationMapper.selectOne(qw)).orElseGet(SettlementInformation::new);
        if (settlementInformation.getOrderId() != null) {
            return settlementinformationMapper.updateById(settlementinformation);
        }
        return settlementinformationMapper.insertSettlementInformation(settlementinformation);
    }

    @Override
    public IPage<Completedorder> selectorderinformation(String AppPoiCode, int start, int size, String start_time, String end_time, Integer status, String orderId) {
        LambdaQueryWrapper<Completedorder> qw = new LambdaQueryWrapper<>();
        Page<Completedorder> page = new Page<>(start, size);
        page.setOptimizeCountSql(true);
        page.setMaxLimit(50L);
        qw.eq(Completedorder::getAppPoiCode, AppPoiCode);
        if (start_time != null && !"".equals(start_time)) {
            qw.ge(Completedorder::getDate, start_time);
        }
        if (end_time != null && !"".equals(end_time)) {
            qw.le(Completedorder::getDate, end_time);
        }
        if (!Strings.isNullOrEmpty(orderId)) {
            qw.eq(Completedorder::getOrderId, orderId);
        }
        if (status != null) {
            qw.eq(Completedorder::getStatus, status);
        }

        qw.orderByDesc(Completedorder::getDate);
        return completedorderMapper.selectPage(page, qw);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public EasyUIDataGridResult findOrderlistByPage(Completedorder completedorder, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        log.info(page + "::" + rows);
        List<Completedorder> list = completedorderMapper.selectOrderlistByPage(completedorder);
        PageInfo<Completedorder> pageInfo = new PageInfo<>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal((int) pageInfo.getTotal());
        result.setRows(pageInfo.getList());
        return result;
    }

    @Override
    public EasyUIDataGridResult findDetailByOrderId(Long order) {
        List<Detail> listDetails = detailMapper.selectByPrimaryKey(order);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(listDetails.size());
        result.setRows(listDetails);
        return result;
    }

    @Override
    public int updateStatus(Long orderId, String Status) {
        if (Status != null) {
            String s = String.valueOf(orderId);
            return completedorderMapper.updateStatus(s, Status);
        }
        return 0;
    }

    /**
     * 修改拣货状态
     */
    @Override
    public int updatePickingStatus(Long orderId, Integer Status) {
        if (!(Status == null)) {
            LambdaUpdateWrapper<Completedorder> qw = new LambdaUpdateWrapper<>();
            qw.set(Completedorder::getPickingCompleted, Status);
            qw.eq(Completedorder::getOrderId, orderId);
            return completedorderMapper.update(null, qw);
        }

        return 0;
    }

    @Override
    public Map<Integer, Integer> selectOrderCountByPoi(String poi,String appEleCode) {
        List<String> today = com.htnova.common.util.DateUtil.getToday();
        String startTime = today.get(0);
        String endTime = today.get(1);
        List<OrderStatus> orderStatuses = completedorderMapper.findOrderStatusByPoiCode(poi,appEleCode, startTime, endTime);
        int[] sta = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 13};
        Map<Integer, Integer> map = new HashMap<>();
        for (int st : sta) {
            map.put(st, 0);
            for (OrderStatus o : orderStatuses) {
                if (o.getStatus().equals(String.valueOf(st))) {
                    map.put(st, o.getCount());
                    break;
                }
            }
        }
        return map;
    }

    @Override
    public List<Completedorder> findOrderInfoByPoiCodeAndStatus(String poi,String appEleCode, String status) {
        List<String> today = com.htnova.common.util.DateUtil.getToday();
        String startTime = today.get(0);
        String endTime = today.get(1);
        return completedorderMapper.findOrderInfoByPoiCodeAndStatus(poi, appEleCode,startTime, endTime, status);
    }


    /**
     * Summary of today’s revenue order amount and number of orders
     * 今日营收订单金额和订单数汇总
     */
    @Override
    public List<Map<String, Object>> findSummaryTodayRevenueOrderAmountAndNumber(String poi,String appEleCode, String status) {
        List<String> today = com.htnova.common.util.DateUtil.getToday();
        String startTime = today.get(0);
        String endTime = today.get(1);
        return completedorderMapper.findSummaryTodayRevenueOrderAmountAndNumber(poi,appEleCode, startTime, endTime, status);
    }

    @Override
    public int updateRefundStatus(String refundStatus, String total_refund_price, int count_refund, String orderId, String skuId) {
        return detailMapper.updateRefundStatus(refundStatus, total_refund_price, count_refund, orderId, skuId);
    }

}
