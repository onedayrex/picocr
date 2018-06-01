package com.git.onedayrex.picocr.http;

import java.io.IOException;
import java.util.Properties;

public class ConfigUtils {
    private static final Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(ConfigUtils.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperties(String key) {
        return properties.getProperty(key);
    }
}
