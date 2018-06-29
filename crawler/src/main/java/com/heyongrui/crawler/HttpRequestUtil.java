package com.heyongrui.crawler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lambert on 2018/6/28.
 */

public class HttpRequestUtil {
    public static String requestPost(String url, String query, String page, String last_id) {
        String content = "";
        HttpsURLConnection connection = null;
        try {
            URL u = new URL(url);
            connection = (HttpsURLConnection) u.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(50000);
            connection.setReadTimeout(50000);

            connection.setRequestProperty("Host", "librestock.com");
            connection.setRequestProperty("Referer", "https://librestock.com/photos/scenery/");
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.setRequestProperty("Origin", "https://librestock.com");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.3; Trident/7.0;rv:11.0)like Gecko");
            connection.setRequestProperty("Accept-Language", "zh-CN");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("X-CSRFToken", "0Xf4EfSJg03dOSx5NezCugrWmJV3lQjO");
            connection.setRequestProperty("Cookie", "__cfduid=d8e5b56c62b148b7450166e1c0b04dc641530080552;cookieconsent_status=dismiss;csrftoken=0Xf4EfSJg03dOSx5NezCugrWmJV3lQjO;_ga=GA1.2.1610434762.1516843038;_gid=GA1.2.1320775428.1530080429");

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            if (!TextUtil.isNullOrEmpty(query) && !TextUtil.isNullOrEmpty(page) && !TextUtil.isNullOrEmpty(last_id)) {
                DataOutputStream out = new DataOutputStream(connection
                        .getOutputStream());
                // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
                String query_string = "query=" + URLEncoder.encode(query, "UTF-8");
                String page_string = "page=" + URLEncoder.encode(page, "UTF-8");
                String last_id_string = "last_id=" + URLEncoder.encode(last_id, "UTF-8");
                String parms_string = query_string + "&" + page_string + "&" + last_id_string;
                out.writeBytes(parms_string);
                //流用完记得关
                out.flush();
                out.close();
            }
            connection.connect();

            int code = connection.getResponseCode();
            System.out.println("第" + page + "页POST网页解析连接响应吗：" + code);
            if (code == 200) {
                InputStream in = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(in, "utf-8");
                BufferedReader reader = new BufferedReader(isr);
                String line;
                while ((line = reader.readLine()) != null) {
                    content += line;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return content;
    }

    public static String requestGet(String url, int page, int postion) {
        String content = "";
        HttpURLConnection connection = null;
        try {
            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(50000);
            connection.setReadTimeout(50000);

            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.3; Trident/7.0;rv:11.0)like Gecko");
            connection.setRequestProperty("Accept-Language", "zh-CN");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");

            int code = connection.getResponseCode();
            System.out.println("第" + page + "页第" + postion + "个GET网页解析连接响应吗：" + code);
            if (code != 200) return "";
            InputStream in = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(in, "utf-8");
            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                content += line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return content;
    }
}
