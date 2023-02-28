package com.mundane.mail.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.mundane.mail.pojo.Resp;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.util.*;

public class SpiderUtil {
    // 微信公众号文章域名
    private static final String HTTP_WX_DOMAIN = "http://mp.weixin.qq.com";
    private static final String HTTPS_WX_DOMAIN = "https://mp.weixin.qq.com";
    // 文章返回前端统一key常量
    private static final String KEY_TITLE = "title"; // 文章标题
    private static final String KEY_COVER_URL = "coverLink"; // 文章封面图链接
    private static final String KEY_REFER_NAME = "referName"; // 文章出处作者
    private static final String KEY_REFER_URL = "referLink"; // 文章出处链接
    private static final String KEY_TAGS = "tags"; // 文章内容
    private static final String KEY_NAME = "name"; // 标签名称
    private static final String KEY_TEXT = "text"; // 文本信息
    private static final String KEY_HREF = "href"; // a标签链接


    /**
     * 根据文章链接抓取文章内容
     *
     * @param url 文章链接
     * @return 文章内容
     */
    public static String getArticle(String url) {
        // 检测链接是否合法
        String msg = checkUrl(url);
        if (msg != null) {
            return msg;
        }
        // 请求与响应
        String resp = HttpTool.get(url, getWxHeaderMap());
        if (resp == null || resp.trim().length() == 0) {
            return "文章获取失败，请检查链接是否正确";
        }
        // 解析
        String articleResp = getWxArticleContent(resp);
        return articleResp;
    }

    /**
     * 测试主方法
     */
    public static void main(String args[]) {
        String url = "http://mp.weixin.qq.com/s?src=11&timestamp=1677229696&ver=4370&signature=YaS6okAGM0viJ6ZpLsMjqzwN-9OXIiMnnOOpWi37bYoK8N77zFC5Wdarp-dfYeA*8DiEjMD9Ahr7mzWSYslK5FXz5QREtFIAoWbyMaGCxNTD8gUrYOAAFXgaIS5mdvEp&new=1";
        String resp = getArticle(url);
        System.out.println(resp);
    }


    private static String getWxArticleContent(String resp) {
        try {
            List<String> returnList = new ArrayList<>();
            Document document = Jsoup.parse(resp);
            Element content = document.getElementsByClass("rich_media_area_primary_inner").get(0);
            Elements sections = content.select("div#js_content > section > section[data-role=outer]");
            for (int i = 0; i < sections.size(); i++) {
                Element resultSection = sections.get(i);
                for (int j = 0; j < resultSection.childNodeSize(); j++) {
                    Element child = resultSection.child(j);
                    String str = getStrFromChild(child);
                    if (StringUtils.isNotEmpty(str)) {
                        returnList.add(str);
                    }
                }
            }
            if (CollectionUtil.isEmpty(returnList)) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < returnList.size(); i++) {
                sb.append(returnList.get(i));
                if (i != returnList.size() - 1) {
                    sb.append("\n\n");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "文章解析失败";
        }
    }

    private static String getStrFromChild(Element element) {
        StringBuilder sb = new StringBuilder();
        for (Element child : element.children()) {
            sb.append(child.text());
        }
        String s = sb.toString();
        s = s.replaceAll("微信搜:每日资讯简报", "");
        return s;
    }


    /**
     * 检测文章链接是否合法
     */
    public static String checkUrl(String url) {
        if (url == null) {
            return "请输入文章链接";
        }
        if (!url.startsWith(HTTP_WX_DOMAIN) && !url.startsWith(HTTPS_WX_DOMAIN)) {
            return "请输入微信公众号文章链接";
        }
        return null;
    }

    /**
     * 微信公众号请求头设置
     */
    public static Map<String, String> getWxHeaderMap() {
        Map<String, String> map = new HashMap<>(new LinkedHashMap<>());
        map.put("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
        map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "zh-Hans-CN, zh-Hans; q=0.8, en-US; q=0.5, en; q=0.3");
        map.put("Host", "mp.weixin.qq.com");
        map.put("If-Modified-Since", "Sat, 04 Jan 2020 12:23:43 GMT");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
        return map;
    }

}
