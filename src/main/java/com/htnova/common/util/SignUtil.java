package com.htnova.common.util;
import cn.hutool.core.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class SignUtil {
    public static final String SIGN_FIELD = "sign";
    private static final String CHARSET = "UTF-8";

    private static final String SIGN_TYPE = "MD5";
    static Logger log = LoggerFactory.getLogger(SignUtil.class);

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。
     *
     * @param data Map类型数据
     * @param key  API密钥
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean check(Map<String, String> data, String key, String signField) {
        if (!data.containsKey(signField)) {
            return false;
        }
        String sign = data.get(signField);
        return sign(data, key, signField).equals(sign);
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key  API密钥
     * @return 签名
     */
    public static String sign(final Map<String, String> data, String key, String signField) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(signField)) {
                continue;
            }
            // 参数值为空，则不参与签名
            if (data.get(k).trim().length() > 0) {
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
            }
        }

        sb.append("signkey=").append(key);
        System.out.println("验签封装的串为:" + sb);
        String result = MD5(sb.toString()).toLowerCase();
        log.info("生成的签名为:\n" + result);
        return result;
    }

    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    private static String MD5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance(SIGN_TYPE);
            byte[] array = md.digest(data.getBytes(CHARSET));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

