package com.heyongrui.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Vector;

/**
 * Created by lambert on 2018/6/28.
 */

public class DataUtil {
    public Vector<ImageModel> getImgModelsData(String html, int page) throws Exception {
        //获取的数据，存放在集合中
        Vector<ImageModel> imageModels = new Vector<>();
        //采用Jsoup解析
        Document doc = Jsoup.parse(html);
        //获取html标签中的img的列表数据
        Elements elements = doc.select("li[class=image]");
        if (elements == null || elements.size() == 0) {
            elements = doc.select("ul[class=photos]").select("li[class=image]");
        }
        if (elements == null) return imageModels;
        int size = elements.size();
        for (int i = 0; i < size; i++) {
            Element ele = elements.get(i);
            Elements hrefEle = ele.select("a[href]");
            if (hrefEle == null || hrefEle.size() == 0) {
                System.out.println("第" + page + "页第" + (i + 1) + "个文件hrefEle为空");
                continue;
            }
            String img_detail_href = hrefEle.attr("href");
            if (TextUtil.isNullOrEmpty(img_detail_href)) {
                System.out.println("第" + page + "页第" + (i + 1) + "个文件img_detail_href为空");
                continue;
            }
            String img_detail_entity = HttpRequestUtil.requestGet(img_detail_href, page, (i + 1));
            if (TextUtil.isNullOrEmpty(img_detail_entity)) {
                System.out.println("第" + page + "页第" + (i + 1) + "个文件网页实体img_detail_entity为空");
                continue;
            }
            ImageModel imageModel = getModel(img_detail_entity);
            if (imageModel == null) {
                System.out.println("第" + page + "页第" + (i + 1) + "个文件模型imageModel为空");
                continue;
            }
            imageModel.setPage(page);
            imageModel.setPostion((i + 1));
            //将每一个对象的值，保存到List集合中
            imageModels.add(imageModel);
        }
        //返回数据
        return imageModels;
    }

    public boolean isLastPage(String html) {
        //采用Jsoup解析
        Document doc = Jsoup.parse(html);
        //获取Js内容，判断是否最后一页
        Elements jsEle = doc.getElementsByTag("script");
        for (Element element : jsEle) {
            String js_string = element.data().toString();
            if (js_string.contains("\"False\" == \"True\"")) {
                return false;
            } else if (js_string.contains("\"True\" == \"True\"")) {
                return true;
            }
        }
        return false;
    }

    public ImageModel getModel(String img_detail_html) throws Exception {
        if (TextUtil.isNullOrEmpty(img_detail_html)) return null;
        //采用Jsoup解析
        Document doc = Jsoup.parse(img_detail_html);
        //获取html标签中的内容
        String image_url = doc.select("div[class=img-col]").select("img[itemprop=url]").attr("src");
        if (TextUtil.isNullOrEmpty(image_url)) {
            image_url = doc.select("div[class=image-section__photo-wrap-width]").select("a[href]").attr("href");
        }
        if (TextUtil.isNullOrEmpty(image_url)) {
            image_url = doc.select("span[itemprop=image]").select("img").attr("src");
        }
        if (TextUtil.isNullOrEmpty(image_url)) {
            image_url = doc.select("div[id=download-image]").select("img").attr("src");
        }
        if (TextUtil.isNullOrEmpty(image_url)) return null;

        ImageModel imageModel = new ImageModel();
        String image_name = TextUtil.getFileName(image_url);
        imageModel.setImage_url(image_url);
        imageModel.setImage_name(image_name);
        return imageModel;
    }
}
