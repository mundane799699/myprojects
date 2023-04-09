package com.mundane.mail.service;

import com.mundane.mail.pojo.Picture;
import com.mundane.mail.utils.HttpTool;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PicCrawlerService {

    public List<Picture> getPicList() {
        String url = "https://www.ikanins.com/week/";
        String html = HttpTool.get(url, getHeaderMap());
        Document doc = Jsoup.parse(html);
        Element element = doc.selectFirst("#masonry");
        Element article = element.selectFirst("article");
        Element aLink = article.selectFirst("a.entry-thumbnail");
        // https://www.ikanins.com/week-20230305/
        String href = aLink.attr("href");
        String title = article.selectFirst("h2.entry-title a").text();
        return getContentFromHref(href, title);
    }

    public String getTitle() {
        String url = "https://www.ikanins.com/week/";
        String html = HttpTool.get(url, getHeaderMap());
        Document doc = Jsoup.parse(html);
        Element element = doc.selectFirst("#masonry");
        Element article = element.selectFirst("article");
        String title = article.selectFirst("h2.entry-title a").text();
        if (title == null) {
            return null;
        }
        return title.trim();
    }

    private List<Picture> getContentFromHref(String href, String title) {
        String html = HttpTool.get(href, getHeaderMap());
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("div.content article div.entry-content div.entry.themeform p > img");
        List<Picture> picList = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setId((long) (i + 1));
            String url = element.attr("src");
            if (StringUtils.isEmpty(url)) {
                continue;
            }
            picture.setUrl(url);
            if (i == 0) {
                picture.setStatus(1);
            }
            picList.add(picture);
        }
        return picList;
    }

    private static Map<String, String> getHeaderMap() {
        Map<String, String> map = new HashMap<>(new LinkedHashMap<>());
        map.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36");
        return map;
    }
}
