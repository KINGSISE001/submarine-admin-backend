package com.htnova.mt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htnova.mt.order.entity.SettlementInformation;

public interface SettlementInformationMapper extends BaseMapper<SettlementInformation> {
    int deleteByPrimaryKey(Integer id);
    int insertSettlementInformation(SettlementInformation settlementinformation);
    SettlementInformation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettlementInformation record);

    int updateByPrimaryKey(SettlementInformation record);
}
