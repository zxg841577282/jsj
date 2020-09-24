package com.zxg.pay.web.controller.lakala.paymax.other;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.file.IOUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zxg.pay.config.LklPaymaxConfig;
import util.RSAUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import other.ResultException;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 注：本DEMO或文档中所示的默认配置参数仅供参考，接入方须视各自系统及交易情况进行相应的调整。如因采用默认配置参数导致交易异常及造成相关损失的，我司不承担相关责任。
 */
public abstract class Paymax extends PaymaxBase {

    private static String HEADER_KEY_NONCE = "nonce";
    private static String HEADER_KEY_TIMESTAMP = "timestamp";
    private static String HEADER_KEY_AUTHORIZATION = "Authorization";
    private static String REQUEST_SUCCESS_FLAG = "reqSuccessFlag";
    private static String RESPONSE_CODE = "code";
    private static String RESPONSE_DATA = "data";
    private static int VALID_RESPONSE_TTL = 2 * 60 * 1000;//合法响应时间:2分钟内


    private static CloseableHttpClient httpsClient = null;

    static class AnyTrustStrategy implements TrustStrategy {

        @Override
        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            return true;
        }

    }

    static {
        try {
            RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
            ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
            registryBuilder.register("http", plainSF);

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            SSLContext sslContext =
                    SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, new AnyTrustStrategy()).build();
            LayeredConnectionSocketFactory sslSF =
                    new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            registryBuilder.register("https", sslSF);

            Registry<ConnectionSocketFactory> registry = registryBuilder.build();

            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
            /**
             * MaxtTotal是整个池子的大小；
             */
            connManager.setMaxTotal(500);
            /**
             * DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分（分配给同一个route(路由)最大的并发连接数）；
             * 比如：
             * MaxtTotal=400 DefaultMaxPerRoute=200
             * 只连接到http://baidu.com时，到这个主机的并发最多只有 200；而不是 400；
             * 连接到http://baidu.com 和 http://qq.com时，到每个主机的并发最多只有 200；即加起来是 400（不能超过 400）；
             * 所以起作用的设置是DefaultMaxPerRoute。
             *
             *
             * route：运行环境机器 到 目标机器的一条线路。
             * 举例来说，我们使用HttpClient的实现来分别请求 www.baidu.com 的资源和 www.bing.com 的资源那么他就会产生两个route。
             */
            connManager.setDefaultMaxPerRoute(200);

            httpsClient = HttpClientBuilder.create().setConnectionManager(connManager).build();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param url
     * @param jsonReqData
     * @param clazz
     * @param <T>
     * @throws IOException
     */
    protected static <T> T request(String url, String jsonReqData, Class<T> clazz) throws IOException {

        if (StringUtils.isBlank(LklPaymaxConfig.SECRET_KEY)) {
            throw new ResultException("Secret key can not be blank.Please set your Secret key in com.Paymax.config.SignConfig");
        }

        if (StringUtils.isBlank(LklPaymaxConfig.PRIVATE_KEY)) {
            throw new ResultException("RSAUtil Private key can not be blank.Please set your RSAUtil Private key  in com.Paymax.config.SignConfig");
        }

        if (StringUtils.isBlank(LklPaymaxConfig.PAYMAX_PUBLIC_KEY)) {
            throw new ResultException("Paymax Public key can not be blank.Please set your Paymax Public key in com.Paymax.config.SignConfig");
        }
        Map<String, String> result = null;
        if (StringUtils.isBlank(jsonReqData)) {
            result = buildGetRequest(url);
        } else {
            if (clazz.getSimpleName().equals("String")) {
                return (T) buildDownloadPostRequest(url, jsonReqData);
            } else {
                result = buildPostRequest(url, jsonReqData);
            }
        }

        return dealWithResult(result, clazz);

    }

    /**
     * 处理返回数据
     *
     * @param result
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> T dealWithResult(Map<String, String> result, Class<T> clazz) {
        int resultCode = Integer.valueOf(result.get(RESPONSE_CODE)).intValue();
        String resultData = result.get(RESPONSE_DATA);

        T t = JSON.parseObject(resultData, clazz);

        try {
            if (t == null) {
                t = clazz.newInstance();
            }
            Field f = clazz.getDeclaredField(REQUEST_SUCCESS_FLAG);
            f.setAccessible(true);
            f.set(t, resultCode < 400);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return t;

    }

    /**
     * POST请求
     *
     * @param url
     * @param jsonReqData
     */
    private static Map<String, String> buildPostRequest(String url, String jsonReqData) throws IOException {
        Map<String, String> result = null;

        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(
                new ByteArrayEntity(jsonReqData.getBytes(Charset.forName(LklPaymaxConfig.CHARSET)), ContentType.APPLICATION_JSON));

        setCustomHeaders(httpPost);

        String sign = signData(httpPost);

        httpPost.addHeader(LklPaymaxConfig.SIGN, sign);

        CloseableHttpResponse response = httpsClient.execute(httpPost);
        try {
            result = verifyData(response);
        } finally {
            response.close();
            httpPost.releaseConnection();
        }

        return result;

    }

    /**
     * 对账单下载POST请求
     *
     * @param url
     * @param jsonReqData
     */
    private static String buildDownloadPostRequest(String url, String jsonReqData) throws IOException {

        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(
                new ByteArrayEntity(jsonReqData.getBytes(Charset.forName(LklPaymaxConfig.CHARSET)), ContentType.APPLICATION_JSON));

        setCustomHeaders(httpPost);

        String sign = signData(httpPost);

        httpPost.addHeader(LklPaymaxConfig.SIGN, sign);

        CloseableHttpResponse response = httpsClient.execute(httpPost);

        try {
            return EntityUtils.toString(response.getEntity(), Charset.forName(LklPaymaxConfig.CHARSET));
        } finally {
            response.close();
            httpPost.releaseConnection();
        }
    }

    /**
     * 对返回结果进行验签。
     * 验签成功:返回数据
     * 验签失败:抛出异常
     *
     * @param response
     */
    private static Map<String, String> verifyData(CloseableHttpResponse response) throws IOException {
        Map<String, String> result = null;
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try {

                String resData = EntityUtils.toString(entity, Charset.forName(LklPaymaxConfig.CHARSET));
                int responseCode = response.getStatusLine().getStatusCode();
                if (Integer.valueOf(responseCode) < 400) {
                    String nonce = response.getFirstHeader(HEADER_KEY_NONCE) != null ? response.getFirstHeader(HEADER_KEY_NONCE).getValue() : "";
                    String timestamp = response.getFirstHeader(HEADER_KEY_TIMESTAMP) != null ? response.getFirstHeader(HEADER_KEY_TIMESTAMP).getValue() : "";
                    String secretKey = response.getFirstHeader(HEADER_KEY_AUTHORIZATION) != null ? response.getFirstHeader(HEADER_KEY_AUTHORIZATION).getValue() : "";

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    out.write(nonce.getBytes(LklPaymaxConfig.CHARSET));
                    out.write('\n');//header
                    out.write(timestamp.getBytes(LklPaymaxConfig.CHARSET));
                    out.write('\n');//header
                    out.write(secretKey.getBytes(LklPaymaxConfig.CHARSET));
                    out.write('\n');//header
                    byte[] data = resData.getBytes(LklPaymaxConfig.CHARSET);
                    out.write(data);//body
                    out.close();

                    String toVerifyData = out.toString(LklPaymaxConfig.CHARSET);

                    String sign = response.getFirstHeader(LklPaymaxConfig.SIGN) != null ? response.getFirstHeader(LklPaymaxConfig.SIGN).getValue() : "";

                    boolean flag = RSAUtil.verify(toVerifyData, sign, LklPaymaxConfig.PAYMAX_PUBLIC_KEY);
                    if (!flag) {
                        throw new ResultException("Invalid Response.[Response Data And Sign Verify Failure.]");
                    }

                    if (!LklPaymaxConfig.SECRET_KEY.equals(secretKey)) {
                        throw new ResultException("Invalid Response.[Secret Key Is Invalid.]");
                    }

                    if (Long.valueOf(timestamp) + VALID_RESPONSE_TTL < System.currentTimeMillis()) {
                        throw new ResultException("Invalid Response.[Response Time Is Invalid.]");
                    }
                }


                result = new HashMap<String, String>();

                result.put(RESPONSE_CODE, String.valueOf(responseCode));
                result.put(RESPONSE_DATA, resData);

            } finally {
                EntityUtils.consumeQuietly(entity);
            }

        }

        return result;
    }


    /**
     * GET请求
     *
     * @param url
     */
    public static Map<String, String> buildGetRequest(String url) throws IOException {
        Map<String, String> result = null;

        HttpGet httpGet = new HttpGet(url);

        setCustomHeaders(httpGet);

        String sign = signData(httpGet);

        httpGet.addHeader(LklPaymaxConfig.SIGN, sign);

        CloseableHttpResponse response = httpsClient.execute(httpGet);
        try {
            result = verifyData(response);
        } finally {
            response.close();
            httpGet.releaseConnection();
        }

        return result;
    }

    /**
     * 设置header
     *
     * @param request
     */
    private static void setCustomHeaders(HttpRequestBase request) {
        request.addHeader("Content-Type", "application/json; charset=" + LklPaymaxConfig.CHARSET);
        request.addHeader(HEADER_KEY_AUTHORIZATION, LklPaymaxConfig.SECRET_KEY);

        String timestamp = String.valueOf(System.currentTimeMillis());
        request.addHeader(HEADER_KEY_TIMESTAMP, timestamp);
        request.addHeader(HEADER_KEY_NONCE, UUID.randomUUID().toString().replaceAll("-", ""));

        String[] propertyNames = {"os.name", "os.version", "os.arch",
                "java.version", "java.vendor", "java.vm.version",
                "java.vm.vendor"};
        Map<String, String> propertyMap = new HashMap<String, String>();
        for (String propertyName : propertyNames) {
            propertyMap.put(propertyName, System.getProperty(propertyName));
        }
        propertyMap.put("lang", "Java");
        propertyMap.put("publisher", "Paymax");
        propertyMap.put("sdk-version", LklPaymaxConfig.SDK_VERSION);

        request.addHeader("X-Paymax-Client-User-Agent", JSON.toJSONString(propertyMap));

    }

    /**
     * 签名数据
     *
     * @param httpRequest
     * @return
     * @throws IOException
     */
    private static String signData(HttpRequestBase httpRequest) throws IOException {
        Request<HttpRequest> request = new HttpRequestWrapper(httpRequest);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(request.getMethod().toLowerCase().getBytes(LklPaymaxConfig.CHARSET));
        out.write('\n');//method
        String uri = request.getRequestUriPath();
        if (StringUtils.isBlank(uri)) {
            uri = "";
        }
        int _index = uri.indexOf("/v1/");
        if (_index != -1) {
            uri = uri.substring(_index);
        }
        out.write(uri.getBytes(LklPaymaxConfig.CHARSET));
        out.write('\n');//uri path
        out.write(request.getRequestQueryString().getBytes(LklPaymaxConfig.CHARSET));
        out.write('\n');//query string
        out.write(request.getHeaderValue(HEADER_KEY_NONCE).getBytes(LklPaymaxConfig.CHARSET));
        out.write('\n');//header
        out.write(request.getHeaderValue(HEADER_KEY_TIMESTAMP).getBytes(LklPaymaxConfig.CHARSET));
        out.write('\n');//header
        out.write(request.getHeaderValue(HEADER_KEY_AUTHORIZATION).getBytes(LklPaymaxConfig.CHARSET));
        out.write('\n');//header
        byte[] data = IOUtils.toByteArray(request.getRequestBody());
        out.write(data);//body
        out.close();
        String toSignString = out.toString(LklPaymaxConfig.CHARSET);
        return RSAUtil.sign(toSignString, LklPaymaxConfig.PRIVATE_KEY);
    }

}