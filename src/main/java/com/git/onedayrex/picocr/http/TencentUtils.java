package com.git.onedayrex.picocr.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class TencentUtils {
    private static final Logger logger = Logger.getLogger(TencentUtils.class);
    public static final String APP_KEY = "1106943114";
    public static final String APP_SECRET = "nBPokl8LqOwvycy4";
    private static final String TENCENT_URL = "https://api.ai.qq.com/fcgi-bin/ocr/ocr_generalocr";

    public static final String sign(Map<String, String> param) {
        List<Map.Entry<String, String>> infos = new ArrayList<>(param.entrySet());
        Collections.sort(infos, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> info : infos) {
            sb.append(info.getKey());
            sb.append("=");
            try {
                sb.append(URLEncoder.encode(info.getValue(),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append("&");
        }
        sb.append("app_key=");
        sb.append(APP_SECRET);
        logger.info("sign sort param==>" + sb);
        String result = DigestUtils.md5Hex(sb.toString()).toUpperCase();
        logger.info("sign MD5==>" + result);
        return result;
    }

    public static final String ocrString(BufferedImage bf) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        Map<String, String> param = new HashMap<>();
        param.put("app_id", APP_KEY);
        param.put("time_stamp", String.valueOf(System.currentTimeMillis()/1000));
        param.put("nonce_str", UUID.randomUUID().toString().replace("-",""));
        param.put("image", ImageUtils.image2Base64(bf));
        String sign = sign(param);
        param.put("sign", sign);
        for (Map.Entry<String, String> stringObjectEntry : param.entrySet()) {
            builder.add(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        FormBody body = builder.build();
        Request req = new Request.Builder()
                .url(TENCENT_URL)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(req);
        StringBuilder sb = new StringBuilder();
        try {
            Response response = call.execute();
            String result = response.body().string();
            logger.info("tencent ocr ==>" + result);
            JSONObject jsonObject = JSON.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray item_list = data.getJSONArray("item_list");
            if (item_list.size() > 0) {
                for (Object o : item_list) {
                    JSONObject item = (JSONObject) o;
                    JSONArray words = item.getJSONArray("words");
                    if (words.size() > 0) {
                        for (Object word : words) {
                            String character = ((JSONObject) word).getString("character");
                            sb.append(character);
                        }
                        sb.append("\n");
                    }
                }
                logger.info("====>" + sb.toString());
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return sb.toString();
    }
}
