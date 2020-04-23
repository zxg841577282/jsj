package com.zxg.pay.web.controller.lakala.code.utils;

import org.springframework.web.multipart.MultipartFile;
import other.ResultException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * @Author: zhou_xg
 * @Date: 2019/12/2 10:20
 * @Purpose: 附件上传图片转二进制
 */

public class PicUtil {

//    public static void main(String[] args) {
//        picToBinary("http://zb.chinazzw.com:8080/uploads/zzw/1574910054745.jpg");
//    }

    public static byte[] picToBinary(String url) {
        BufferedImage bi;
        try {
            bi = ImageIO.read(new URL(url));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] fileToBinary(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            System.out.println("图片二进制：" + bytes.toString());
            return bytes;
        } catch (IOException e) {
//            e.printStackTrace();
            throw new ResultException("图片转换异常");
        }
    }

}
