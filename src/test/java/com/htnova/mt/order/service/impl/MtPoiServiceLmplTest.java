package com.htnova.mt.order.service.impl;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson.JSON;
import com.htnova.common.meituan.MeiTuanUtil;
import com.htnova.common.util.HttpRequestUtil;
import com.htnova.common.util.SignUtil;
import com.htnova.mt.order.service.MtPoiService;
import com.htnova.system.tool.service.QuartzJobService;
import com.htnova.system.tool.service.QuartzLogService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class MtPoiServiceLmplTest {

    @Autowired
    MtPoiService mtPoiService;

    @Autowired
    MeiTuanUtil meiTuanUtil;

    @Autowired
    MeEleServiceImpl meEleService;



    @Test
    public void contextLoads() throws Exception {


        //log.info("{}", JSON.toJSONString(meiTuanUtil.getPoiInfo("1430_2701469")) );
        //log.info("{}", JSON.toJSONString(meiTuanUtil.orderCancel("4400848994226429994",2015,"")));
        //mtPoiService.poiGetIds();

        //meEleService.getEleOrder("4093990062890562094");

    }
}
