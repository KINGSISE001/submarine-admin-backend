package com.htnova.mt.order.mapper;

import com.htnova.mt.order.entity.Detail;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DetailMapper {
    int deleteByPrimaryKey(Long orderId);
    int insertDetail(@Param("lists") List<Detail> lists);
    List<Detail> selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(Detail record);

    int updateByPrimaryKey(Detail record);
}
