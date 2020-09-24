package com.zxg.pay.web.controller.lakala.code.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @Author: zhou_xg
 * @Date: 2019/11/29 16:30
 * @Purpose:
 */

public class HttpClientUtil {

    public static String doPost(String url, String xml) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "text/html;charset=GBK");

            //解决中文乱码问题
            StringEntity stringEntity = new StringEntity(xml, "GBK");
            stringEntity.setContentEncoding("GBK");

            httpPost.setEntity(stringEntity);

            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(final HttpResponse response)
                        throws ClientProtocolException, IOException {//
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {

                        HttpEntity entity = response.getEntity();


                        return entity != null ? EntityUtils.toString(entity, "gbk") : null;
                    } else {
                        throw new ClientProtocolException(
                                "Unexpected response status: " + status);
                    }
                }
            };

            String responseBody = httpclient.execute(httpPost, responseHandler);


            System.out.println("----------------------------------------");
            System.out.println(responseBody);

            return responseBody;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static String doPostByPic(String url, String xml) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "multipart/form-data");

            //解决中文乱码问题
            StringEntity stringEntity = new StringEntity(xml, "GBK");
            stringEntity.setContentEncoding("GBK");

            httpPost.setEntity(stringEntity);

            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(final HttpResponse response)
                        throws ClientProtocolException, IOException {//
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {

                        HttpEntity entity = response.getEntity();


                        return entity != null ? EntityUtils.toString(entity, "GBK") : null;
                    } else {
                        throw new ClientProtocolException(
                                "Unexpected response status: " + status);
                    }
                }
            };

            String responseBody = httpclient.execute(httpPost, responseHandler);


            System.out.println("----------------------------------------");
            System.out.println(responseBody);

            return responseBody;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
