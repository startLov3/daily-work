package com.common.tool.config.base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class ImageStrToBase64 {

    public static void main(String[] args) throws Exception {
        encodeImageToBase64(new File(""));
    }

    public static String encodeImageToBase64(File file) throws Exception {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("图片上传失败!");
        }
        //对字节数组Base64编码
        /*
        //带换行符
        BASE64Encoder encoder = new BASE64Encoder();
        String base64 = encoder.encode(data);
        */
        String base64 = Base64.getEncoder().encodeToString(data);
        return base64;//返回Base64编码过的字节数组字符串
    }
}
