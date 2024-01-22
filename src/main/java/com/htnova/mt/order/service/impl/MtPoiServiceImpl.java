package com.htnova.mt.order.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htnova.common.dev.config.MtConfig;
import com.htnova.mt.order.entity.MtPoi;
import com.htnova.mt.order.mapper.MtPoiMapper;
import com.htnova.mt.order.service.MtPoiService;
import com.sankuai.meituan.shangou.open.sdk.domain.SystemParam;
import com.sankuai.meituan.shangou.open.sdk.exception.SgOpenException;
import com.sankuai.meituan.shangou.open.sdk.request.PoiGetIdsRequest;
import com.sankuai.meituan.shangou.open.sdk.request.PoiMGetRequest;
import com.sankuai.meituan.shangou.open.sdk.response.SgOpenResponse;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 获取美团店铺信息
 */
@Service
public class MtPoiServiceImpl extends ServiceImpl<MtPoiMapper, MtPoi> implements MtPoiService {
    @Resource
    MtConfig m;

    @Override
    public void poiGetIds() {
        SystemParam systemParam = new SystemParam(m.getMtappid(), m.getMtkey());
        PoiGetIdsRequest request = new PoiGetIdsRequest(systemParam);
        SgOpenResponse sgOpenResponse;
        try {
            sgOpenResponse = request.doRequest();
        } catch (SgOpenException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        //请求返回的结果，按照官网的接口文档自行解析即可
        String requestResult = sgOpenResponse.getRequestResult();
        System.out.println(requestResult);
        List<String> list = JSON.parseArray(JSON.parseObject(requestResult).getString("data"), String.class);
        List<List<String>> l = ListUtil.split(list, 200);
        l.forEach(
            f -> {
                StringBuilder t = new StringBuilder();
                f.forEach(
                    S -> {
                        t.append(S).append(",");
                    }
                );
                batchGetPoiInfo(StringUtils.removeEnd(String.valueOf(t), ","));
            }
        );
    }

    public void batchGetPoiInfo(String codes) {
        SystemParam systemParam = new SystemParam(m.getMtappid(), m.getMtkey());
        PoiMGetRequest poiMGetRequest = new PoiMGetRequest(systemParam);
        // 上传单门店
        //poiCloseRequest.setApp_poi_code("666");
        poiMGetRequest.setApp_poi_codes(codes);
        SgOpenResponse sgOpenResponse;
        try {
            sgOpenResponse = poiMGetRequest.doRequest();
            this.saveOrUpdateBatch(
                    JSON.parseArray(JSON.parseObject(sgOpenResponse.getRequestResult()).getString("data"), MtPoi.class)
                );
        } catch (JSONException e) {
            e.getMessage();
            return;
        } catch (SgOpenException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
