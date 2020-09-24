package com.zxg.pay.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import other.ResultException;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * @Author: zhou_xg
 * @Date: 2020/3/19
 * @Purpose: 支付相关方法
 */

public class PayGeneralMethod {

    /**
     * 价格微信标准化
     * 乘以100
     *
     * @param total_fee
     * @return
     */
    public static String totalFeeWx(BigDecimal total_fee) {
        int i = total_fee.multiply(new BigDecimal(100)).intValue();
        return String.valueOf(i);
    }


    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();

    /**
     * 生成随机数字和字母
     */
    public static String getStringRandom() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    /**
     * 拼接参数并签名
     *
     * @param params 入参
     * @param KEY    商户密钥
     * @return
     */
    public static String getParamSign(Map<String, String> params, String KEY) {

        //建立一个有序的map
        Map<String, Object> map = new TreeMap<>(
                new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                }
        );

        //把参数塞入map
        Iterator<String> paramsIter = params.keySet().iterator();
        while (paramsIter.hasNext()) {
            String key = paramsIter.next();
            Object value = params.get(key);
            map.put(key, value);
        }

        //拼接参数
        Iterator<String> mapIter = map.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (mapIter.hasNext()) {
            String key = mapIter.next();
            if ("".equals(map.get(key)) || map.get(key) == null) {
                continue;
            }
            sb.append(key + "=" + map.get(key) + "&");
        }
        String sign = MD5(sb.toString() + "key=" + KEY).toUpperCase();
        return sign;
    }

    public static String MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }



    /*---------------------企业支付重要参数加密开始--------------------------*/

    /**
     * 获取加密结果
     *
     * @param data 加密内容
     * @return 加密后的字符串
     */
    public static String getDataWithPem(String data, String pemAddr) {
        //获取pubkey字符串内容
        String key = getPubKeyContentString(pemAddr);

        PublicKey publicKey = null;
        Cipher cipher = null;
        try {
            publicKey = getPublicKey(key);
            cipher = Cipher.getInstance("RSAUtil/ECB/OAEPWITHSHA-1ANDMGF1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] test = cipher.doFinal(data.getBytes("utf-8"));
            byte[] bytes = Base64.encodeBase64(test);
            String s = new String(bytes, "utf-8");
            System.out.println("加密结果【" + s);
            return s;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据公钥字符串获取公钥
     *
     * @param key 公钥字符串
     * @return 返回公钥
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new Base64().decode(key));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSAUtil");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 根据pem公钥路径获取公钥字符串
     *
     * @return 公钥字符串
     */
    public static String getPubKeyContentString(String pemAddr) {
        try {
            File file3 = ResourceUtils.getFile(pemAddr);
            //读取pem证书
            BufferedReader br = new BufferedReader(new FileReader(file3));

            StringBuffer publickey = new StringBuffer();
            String line;

            while (null != (line = br.readLine())) {
                if ((line.contains("BEGIN PUBLIC KEY") || line.contains("END PUBLIC KEY")))
                    continue;
                publickey.append(line);
            }
            return publickey.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*---------------------企业支付重要参数加密结束--------------------------*/
}
