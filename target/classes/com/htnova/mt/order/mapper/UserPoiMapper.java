package com.htnova.mt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htnova.mt.order.dto.UserPoiDto;
import com.htnova.mt.order.entity.UserPoi;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserPoiMapper extends BaseMapper<UserPoi> {
    IPage<UserPoi> findPage(IPage<Void> xPage, @Param("userPoiDto") UserPoiDto userPoiDto);

    List<UserPoi> findList(@Param("userPoiDto") UserPoiDto userPoiDto);
}
