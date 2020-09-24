package com.zxg.pay.config;

import com.abc.pay.client.*;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 农业银行配置文件
 * 具体工厂-客运工厂，从数据库中读取配置
 * 生产两种产品：MerchantParaWeb和
 */

public class ABCConfig extends MerchantParaFactory {

//	private static String Addr = "/home/abc/";

    private static String Addr = "/home/abc/";

    /**
     * 商户端配置文件资源对象
     */
    private static MerchantPara paraWeb = null;

    /**
     * 初始旗标
     */
    private static boolean iIsInitialedWeb = false;

    public void refreshConfig() throws TrxException {
        iIsInitialedWeb = false;
    }

    public void init(MerchantPara para) {
        try {
            //##网上支付平台系统配置段 - 生产环境 - 请勿更改
            //#网上支付平台通讯方式（http / https）
            //公网
            para.setTrustPayConnectMethod("https");
            //专线
            para.setTrustPayConnectMethodLine("https");

            //#网上支付平台服务器名
            //公网
            para.setTrustPayServerName("pay.abchina.com");
            //专线
            para.setTrustPayServerNameLine("pay.abchina.com");

            //#网上支付平台交易端口
            //公网
            para.setTrustPayServerPort("443");
            //专线
            para.setTrustPayServerPortLine("443");

            //#网上支付平台交易网址
            para.setTrustPayTrxURL("/ebus/ReceiveMerchantTrxReqServlet");
            para.setTrustPayTrxIEURL("https://pay.abchina.com/ebus/ReceiveMerchantIERequestServlet");

            //#页面提交支付请求失败后的转向地址
            para.setMerchantErrorURL("http://127.0.0.1:8081/Merchant.html");

            //##网上支付平台系统配置段 - 生产环境 - 更改证书存放路径，使其和本地存放路径相匹配（绝对路径）
            //#网上支付平台证书

            File file = ResourceUtils.getFile("classpath:abc/TrustPay.cer");
            String path = file.getPath();
//			String path = Addr + "TrustPay.cer";
//			System.out.println("-------------------path:" + path);
            para.setTrustPayCertFileName(path);


            //#农行根证书文件
            File file2 = ResourceUtils.getFile("classpath:abc/abc.truststore");
            String path2 = file2.getPath();
//			String path2 = Addr + "abc.truststore";
//			System.out.println("-------------------path2:" + path2);
            para.setTrustStoreFileName(path2);

            //#农行根证书文件密码
            para.setTrustStorePassword("changeit");

            //设置商户编号。如果是多商户则在iMerchantIDList放置多条记录
            ArrayList<String> iMerchantIDList = new ArrayList<String>();
            iMerchantIDList.add("103881929993502");
            para.setMerchantIDList(iMerchantIDList);

            FileUtil util = new FileUtil();

            //设置商户证书。如果是多商户则在iMerchantCertNameList放置多条记录。注意：商户证书名称顺序要与商户编号顺序一致
            ArrayList<byte[]> iMerchantCertList = new ArrayList<byte[]>();

            File file3 = ResourceUtils.getFile("classpath:abc/sig.pfx");
            String path3 = file3.getPath();
//			String path3 = Addr + "sig.pfx";
//			System.out.println("-------------------path3:" + path3);
            iMerchantCertList.add(util.readFile(path3));
            para.setMerchantCertFileList(iMerchantCertList);

            //设置商户证书密码。如果是多商户则在iMerchantPasswordList放置多条记录。注意：密码顺序要与商户编号顺序一致
            ArrayList<String> iMerchantPasswordList = new ArrayList<String>();
            iMerchantPasswordList.add("11223344");// 商户私钥密码
            para.setMerchantCertPasswordList(iMerchantPasswordList);

            //#交易日志文件存放目录
            File logfile = ResourceUtils.getFile("classpath:abc/log/");
            String logfilePath = logfile.getPath();
//			String logfilePath = Addr+"log/";
//			System.out.println("-------------------logfilePath:" + logfilePath);
            para.setLogPath(logfilePath);

            //#证书储存媒体
            para.setMerchantKeyStoreType("0");

            //一般商户都选用文件证书
            if (para.getMerchantKeyStoreType().equals(MerchantPara.KEY_STORE_TYPE_FILE)) {
                CertHelper.bindMerchantCertificate(para, iMerchantCertList, iMerchantPasswordList);
            } else if (para.getMerchantKeyStoreType().equals(MerchantPara.KEY_STORE_TYPE_SIGN_SERVER)) {
            } else {
                throw new TrxException(TrxException.TRX_EXC_CODE_1001, TrxException.TRX_EXC_MSG_1001 + " - 证书储存媒体配置错误！");
            }

            //设定上网代理
            para.setProxyIP("");
            para.setProxyPort("");

            //设定连接超时时间
            para.setTrustPayServerTimeout("");

            //#Sign Server地址（当KeyStoreType=1时，必须设定）
            //para.setSignServerIP("");
            //#Sign Server端口（当KeyStoreType=1时，必须设定）
            //para.setSignServerPort("");
            //#Sign Server密码（当KeyStoreType=1时，选择设定）
            //para.setSignServerPassword("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //iIsInitialed = true;
        System.out.println("[Trustpay商户端API] - 初始 - 完成====================");
    }

    /**
     * MerchantParaFromDB类必须实现getMerchantPara方法，返回MerchantPara类型对象。
     * 从数据库中读取配置项。
     * 默认实现为web
     */
    public MerchantPara getMerchantPara() throws TrxException {
        if (!iIsInitialedWeb) {
            try {
                paraWeb = MerchantParaWeb.getUniqueInstance();
            } catch (TrxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            init(paraWeb);
            iIsInitialedWeb = true;
        }
        return paraWeb;
    }

    public byte[] readFile(String filePath) {
        //create file object
        File file = new File(filePath);
        byte fileContent[] = null;
        try {
            FileInputStream fin = new FileInputStream(file);
            fileContent = new byte[(int) file.length()];
            fin.read(fileContent);

        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the file " + ioe);
        }
        return fileContent;
    }
}