package com.htnova.mt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htnova.mt.order.entity.MtPoi;

/**
 * @author RISE
 * @description 针对表【t_mt_poi】的数据库操作Service
 * @createDate 2023-09-20 15:58:20
 */
public interface MtPoiService extends IService<MtPoi> {
    void poiGetIds();
}
