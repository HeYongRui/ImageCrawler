package com.heyongrui.crawler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by lambert on 2018/6/28.
 */

public class DownloadUtil {
    private DownloadCallBack downloadCallBack;
    private ExecutorService pool = null;

    public DownloadUtil(DownloadCallBack downloadCallBack) {
        this.downloadCallBack = downloadCallBack;
    }

    public void startDownloadList(Vector<ImageModel> downloadList, String keyword) {
        HttpURLConnection connection = null;
        //循环下载
        try {
            for (int i = 0; i < downloadList.size(); i++) {
                pool = Executors.newFixedThreadPool(20);

                ImageModel imageModel = downloadList.get(i);
                if (imageModel == null) continue;
                final String download_url = imageModel.getImage_url();
                final String filename = imageModel.getImage_name();
                int page = imageModel.getPage();
                int postion = imageModel.getPostion();

                Future<HttpURLConnection> future = pool.submit(new Callable<HttpURLConnection>() {
                    @Override
                    public HttpURLConnection call() throws Exception {
                        URL url;
                        url = new URL(download_url);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        //设置超时间为3秒
                        connection.setConnectTimeout(3 * 1000);
                        //防止屏蔽程序抓取而返回403错误
                        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                        return connection;
                    }
                });
                connection = future.get();
                if (connection == null) continue;
                int responseCode = connection.getResponseCode();
                System.out.println("正在下载第" + page + "页第" + postion + "个文件，地址：" + download_url + "响应码：" + connection.getResponseCode());
                if (responseCode != 200) continue;
                InputStream inputStream = connection.getInputStream();
                if (inputStream == null) continue;
                writeFile(inputStream, "d:\\ImageCrawler\\" + keyword + "\\", URLDecoder.decode(filename, "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != connection)
                connection.disconnect();
            if (null != pool)
                pool.shutdown();
            while (true) {
                if (pool.isTerminated()) {//所有子线程结束
                    if (downloadCallBack != null) {
                        downloadCallBack.allWorksDone();
                    }
                    break;
                }
            }
        }
    }

    public void stopDownload() {
        if (pool != null) {
            pool.shutdown();
        }
    }

    /**
     * 写入文件
     */
    public void writeFile(InputStream inputStream, String downloadDir, String filename) {
        try {
            //获取自己数组
            byte[] buffer = new byte[1024];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();

            byte[] getData = bos.toByteArray();

            //文件保存位置
            File saveDir = new File(downloadDir);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + filename);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface DownloadCallBack {
        void allWorksDone();
    }
}
