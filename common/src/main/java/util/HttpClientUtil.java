package util;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.http.ssl.SSLContexts;
import org.springframework.util.ResourceUtils;
import other.ResultException;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/20
 * @Purpose:
 */

public class HttpClientUtil {

    public static final MediaType OKHTTP_JSON = MediaType.get("application/json; charset=utf-8");

    public static JSONObject doPost(String params,String url){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(params, OKHTTP_JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response execute = client.newCall(request).execute();

            System.out.println("返回信息：" + execute);

            String s = execute.body().toString();

            return JSONObject.parseObject(s);
        }catch (Exception e){
            e.getStackTrace();
            throw new ResultException("访问接口异常");
        }
    }

    public static JSONObject doGet(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            Response execute = client.newCall(request).execute();

            System.out.println("返回信息：" + execute);

            return JSONObject.parseObject(execute.body().toString());
        }catch (Exception e){
            e.getStackTrace();
            throw new ResultException("访问接口异常");
        }
    }


    public static String doLklPayPost(String params,String url){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(params, OKHTTP_JSON);
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .url(url)
                .post(body)
                .build();
        try {
            //访问
            Response execute = client.newCall(request).execute();

            if (execute.isSuccessful()){
                //解析信息并以string返回
                InputStream inputStream = execute.body().byteStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String str = null;
                StringBuilder buffer = new StringBuilder();
                while ((str = bufferedReader.readLine()) != null) {
                    buffer.append(str);
                }
                // 释放资源
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                return buffer.toString();
            }else {
                //访问失败则抛出异常
                throw new ResultException(execute.message());
            }
        }catch (Exception e){
            e.getStackTrace();
            throw new ResultException("拉卡拉POST请求异常");
        }
    }

    /**
     * 微信请求
     * @param params 参数字符串
     * @param url 访问的接口地址
     * @return
     */
    public static String doWxPayPost(String params,String url){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(params, OKHTTP_JSON);
        Request request = new Request.Builder()
                .addHeader("Content-Type", "text/plain; charset=utf-8")
                .addHeader("content", "text/plain; charset=utf-8")
                .url(url)
                .post(body)
                .build();
        try {
            //访问
            Response execute = client.newCall(request).execute();

            if (execute.isSuccessful()){
                //解析信息并以string返回
                InputStream inputStream = execute.body().byteStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String str = null;
                StringBuilder buffer = new StringBuilder();
                while ((str = bufferedReader.readLine()) != null) {
                    buffer.append(str);
                }
                // 释放资源
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                return buffer.toString();
            }else {
                //访问失败则抛出异常
                throw new ResultException(execute.message());
            }
        }catch (Exception e){
            e.getStackTrace();
            throw new ResultException("微信POST请求异常");
        }
    }


    /**
     * 微信请求(带证书)
     * @param requestParam 参数字符串
     * @param url 访问的接口地址
     * @param cretAddrs 证书地址
     * 参考地址: https://blog.csdn.net/qq_30729829/article/details/96456356
     * @return
     */
    public static String doWxPayPostCret(String requestParam,String url,String cretAddrs,String mchId){
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream inputStream = new FileInputStream(ResourceUtils.getFile(cretAddrs));
            try {
                keyStore.load(inputStream, mchId.toCharArray());
            } finally {
                inputStream.close();
            }
            SSLContext sslContext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, mchId.toCharArray())
                    .build();

            AssertUtils.isNull(sslContext,"获取证书文件失败");

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .addInterceptor(chain -> {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "text/plain; charset=utf-8")
                                .addHeader("content", "text/plain; charset=utf-8")
                                .build();
                        return chain.proceed(request);
                    })
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
            RequestBody requestBody = RequestBody.create(requestParam, MediaType.parse("text/plain;charset=utf-8"));
            Request request = new Request.Builder()
                    .post(requestBody)
                    .url(url)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new ResultException("response.message()");
            }
        }catch (Exception e){
            e.getStackTrace();
            throw new ResultException("");
        }
    }
}
