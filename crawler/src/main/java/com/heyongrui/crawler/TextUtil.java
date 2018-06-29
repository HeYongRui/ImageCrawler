package com.heyongrui.crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lambert on 2018/6/28.
 */

public class TextUtil {

    public static String getFileName(String url) {
        String suffixes = "avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|pdf|rar|zip|docx|doc";
        Pattern pat = Pattern.compile("[\\w]+[\\.](" + suffixes + ")");//正则判断
        Matcher mc = pat.matcher(url);//条件匹配
        while (mc.find()) {
            //截取文件名和后缀名作为文件名
            return mc.group();
        }
        return "";
    }

    public static boolean isNullOrEmpty(String str) {
//        if (str == null || "".equals(str) || "null".equals(str)) {
//            return true;
//        }
//        return false;
        return (str == null || str.trim().length() == 0);
    }
}
