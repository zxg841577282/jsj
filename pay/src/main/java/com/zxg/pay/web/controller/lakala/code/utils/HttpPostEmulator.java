package com.zxg.pay.web.controller.lakala.code.utils;

import com.zxg.pay.web.req.lakala.code.FormFieldKeyValuePair;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;


public class HttpPostEmulator {
    // 每个post参数之间的分隔。随意设定，只要不会和其他的字符串重复即可。
    private static final String BOUNDARY = "----------HV2ymHFg03ehbqgZCaKO6jyH";

    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            X509Certificate[] x509Certificates = new X509Certificate[]{};
            return x509Certificates;
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType)
                throws java.security.cert.CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType)
                throws java.security.cert.CertificateException {
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    public static String sendHttpPostRequest(String serverUrl, ArrayList<FormFieldKeyValuePair> generalFormFields, MultipartFile multipartFile) throws Exception {
        // 向服务器发送post请求
        URL url = new URL(serverUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String protocol = url.getProtocol();
        if (protocol.equals("https")) {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new X509TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
            connection = (HttpsURLConnection) url.openConnection();
            ((HttpsURLConnection) connection).setSSLSocketFactory(sc.getSocketFactory());
            ((HttpsURLConnection) connection).setHostnameVerifier(new TrustAnyHostnameVerifier());
        }

        // 发送POST请求必须设置如下两行
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Charset", "GBK");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        connection.connect();

        // 头

        String boundary = BOUNDARY;

        // 传输内容

        StringBuffer contentBody = new StringBuffer("--" + BOUNDARY);

        // 尾

        String endBoundary = "\r\n--" + boundary + "--\r\n";

        OutputStream out = connection.getOutputStream();

        // 1. 处理文字形式的POST请求

        for (FormFieldKeyValuePair ffkvp : generalFormFields) {

            contentBody.append("\r\n")

                    .append("Content-Disposition: form-data; name=\"")

                    .append(ffkvp.getKey() + "\"")

                    .append("\r\n")

                    .append("\r\n")

                    .append(ffkvp.getValue())

                    .append("\r\n")

                    .append("--")

                    .append(boundary);

        }

        String boundaryMessage1 = contentBody.toString();

        out.write(boundaryMessage1.getBytes("GBK"));

        // 2. 处理文件上传

        contentBody = new StringBuffer();

        contentBody.append("\r\n")

                .append("Content-Disposition:form-data; name=\"")

//				.append(multipartFile.getContentType() + "\"; ") // form中field的名称

                .append("filename=\"")

                .append(multipartFile.getName() + "\"") // 上传文件的文件名，包括目录

                .append("\r\n")

                .append("Content-Type:image/jpeg")

                .append("\r\n\r\n");

        String boundaryMessage2 = contentBody.toString();

        out.write(boundaryMessage2.getBytes("GBK"));

        // 开始真正向服务器写文件

//		File file = new File(ufi.getFileName());

        DataInputStream dis = new DataInputStream(multipartFile.getInputStream());

        int bytes = 0;


        byte[] bufferOut = new byte[(int) multipartFile.getSize()];

        bytes = dis.read(bufferOut);

        out.write(bufferOut, 0, bytes);

        dis.close();

        contentBody.append("------------HV2ymHFg03ehbqgZCaKO6jyH");

        String boundaryMessage = contentBody.toString();

        out.write(boundaryMessage.getBytes("GBK"));

        // System.out.println(boundaryMessage);


//		for (UploadFileItem ufi : filesToBeUploaded) {
//
//
//
//		}

        out.write("------------HV2ymHFg03ehbqgZCaKO6jyH--\r\n".getBytes("GBK"));

        // 3. 3. 写结尾

        out.write(endBoundary.getBytes("GBK"));

        out.flush();

        out.close();

        // 4. 从服务器获得回答的内容

        String strLine = "";

        String strResponse = "";

        InputStream in = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        while ((strLine = reader.readLine()) != null) {

            strResponse += strLine + "\n";

        }
        return strResponse;

    }

}