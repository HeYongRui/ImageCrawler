package com.heyongrui.crawler;

import java.util.Scanner;
import java.util.Vector;

public class ImageCrawler {

    private static boolean is_last_page;
    private static int page = 1;
    private static DownloadUtil downloadUtil;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入下载内容的关键词: ");
        final String keyword = sc.nextLine();

        downloadUtil = new DownloadUtil(new DownloadUtil.DownloadCallBack() {
            @Override
            public void allWorksDone() {
                if (is_last_page) {
                    System.out.println("下载数据完毕");
                    downloadUtil.stopDownload();
                } else {
                    page++;
                    parseDownloadList(downloadUtil, keyword, page);
                }
            }
        });
        parseDownloadList(downloadUtil, keyword, page);
    }

    private static void parseDownloadList(DownloadUtil downloadUtil, String keyword, int page) {
        try {
            String url = "https://librestock.com/images/";
            String entity = HttpRequestUtil.requestPost(url, keyword, "" + page, "42508");
            DataUtil dataUtil = new DataUtil();
            is_last_page = dataUtil.isLastPage(entity);
            Vector<ImageModel> imageModels = dataUtil.getImgModelsData(entity, page);
            //开始批量下载任务
            downloadUtil.startDownloadList(imageModels, keyword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
