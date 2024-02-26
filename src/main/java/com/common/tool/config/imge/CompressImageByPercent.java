package com.common.tool.config.imge;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

/*压缩图片*/
public class CompressImageByPercent {

    public static void main(String[] args) {
        //图片路径
        String localPicUrl = "";
        while (true) {
            File localPicFile = new File(localPicUrl);
            long len1 = localPicFile.length();
            double len2 = (double) len1 / 1024;
            if (len2 > 1024) {
                double ys_rate = 0.10;//压缩比例
                BufferedImage bufferedImage = compressImageByPercent(localPicFile, ys_rate);
                File destFile = saveAsImage(localPicUrl, bufferedImage);
                System.out.println("=====" + localPicUrl + "===" + readableFileSize(destFile.length()));
            } else {
                break;
            }
        }
    }

    public static BufferedImage compressImageByPercent(File file, double compressPercent) {
        BufferedImage img; //原图
        BufferedImage bufferedImage = null; //压缩后图
        int width, height;
        try {

            img = ImageIO.read(file);
            width = (int) (img.getWidth() * compressPercent);
            height = (int) (img.getHeight() * compressPercent);
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.getGraphics().drawImage(img, 0, 0, width, height, null);//绘制缩小图
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + "" + units[digitGroups];
    }

    public static File saveAsImage(String fullFilePath, BufferedImage bufferedImage) {
        File destFile = null;
        try {
            destFile = new File(fullFilePath);
            FileOutputStream out = new FileOutputStream(destFile); //输出到文件流
            ImageIO.write(bufferedImage, "png", out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destFile;
    }
}
