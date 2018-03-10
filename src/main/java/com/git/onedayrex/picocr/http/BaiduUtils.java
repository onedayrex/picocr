package com.git.onedayrex.picocr.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by fuxiang.zhong on 2018/2/24.
 */
public class BaiduUtils {
    public static final Logger logger = Logger.getLogger(BaiduUtils.class);

    private static final String TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?" +
            "grant_type=client_credentials&" +
            "client_id=RPaCgM2uCHteDwiuYGjOuvO5&" +
            "client_secret=uoEs42ZOVcvoZGMi2TwKtvDqFeQwGkyA ";
    private static final String OCR_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";

    public static String getAuthToken() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request req = new Request.Builder()
                .url(TOKEN_URL)
                .build();
        Call call = okHttpClient.newCall(req);
        try {
            Response execute = call.execute();
            String result = execute.body().string();
            JSONObject jsonMap = JSON.parseObject(result);
            logger.info("response==>" + result);
            return jsonMap.getString("access_token");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String ocrrequest(BufferedImage bf) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("access_token", getAuthToken())
                .add("image",image2Base64(bf))
                .build();
        Request req = new Request.Builder()
                .url(OCR_URL)
                .post(body)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Call call = okHttpClient.newCall(req);
        try {
            Response response = call.execute();
            String result = response.body().string();
            JSONObject jsonMap = JSON.parseObject(result);
            JSONArray wordsResult = jsonMap.getJSONArray("words_result");
            StringBuilder sb = new StringBuilder();
            for (Object o : wordsResult) {
                JSONObject temp = (JSONObject) o;
                String words = temp.getString("words");
                sb.append(words + "\n");
            }
            logger.info("response==>" + result);
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

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
