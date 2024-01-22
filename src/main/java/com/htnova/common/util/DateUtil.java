package com.htnova.common.util;

import com.htnova.common.constant.GlobalConst;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateUtil {

    private DateUtil() {}

    public static String format(LocalDateTime date, String formatString) {
        return DateTimeFormatter.ofPattern(formatString).format(date);
    }

    public static LocalDateTime converter(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.of(GlobalConst.TIME_ZONE_ID));
    }

    public static Date converter(LocalDateTime date) {
        if (Objects.isNull(date)) {
            return null;
        }
        return Date.from(date.toInstant(ZoneOffset.of(GlobalConst.TIME_ZONE_ID)));
    }
    /**
     * 获取今天的时间范围
     * @return 返回长度为2的字符串集合，如：[2017-11-03 00:00:00, 2017-11-03 24:00:00]
     */
    public static List<String> getToday() {
        List<String> dataList = new ArrayList<>(2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DATE, 0);
        String today = dateFormat.format(calendar.getTime());
        dataList.add(today + " 00:00:00");
        dataList.add(today + " 24:00:00");
        return dataList;
    }
}
