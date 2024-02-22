package com.htnova.mt.order.service.impl;

import com.htnova.common.meituan.MeiTuanUtil;
import com.htnova.mt.order.service.MtPoiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        String business_time = "07:00-12:00,12:30-23:00;06:00-22:00;07:00-12:00;07:00-12:00";
        log.info("EleStatus:{}",meEleService.GetShopInfo("32267804205").getData());
        //log.info("{}", JSON.toJSONString(meiTuanUtil.getPoiInfo("1430_2701469")) );
        //log.info("{}", JSON.toJSONString(meiTuanUtil.orderCancel("4400848994226429994",2015,"")));
        //mtPoiService.poiGetIds();

        //meEleService.getEleOrder("4093990062890562094");

    }
}
