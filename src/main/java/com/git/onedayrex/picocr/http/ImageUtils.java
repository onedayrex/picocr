package com.git.onedayrex.picocr.http;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static String image2Base64(BufferedImage bf) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] data = null;
        //读取图片字节数组
        try
        {
            ImageIO.write(bf, "png", out);
            data = out.toByteArray();
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }
}
