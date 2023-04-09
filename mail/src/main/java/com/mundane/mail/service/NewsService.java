package com.mundane.mail.service;

import com.mundane.mail.utils.HttpTool;
import com.mundane.mail.utils.SpiderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
public class NewsService {



    public String getNews() {
        //公众号名称
        String wechatName = "每日资讯简报";
        String url = "https://weixin.sogou.com/weixin?type=1&s_from=input&query={query}&ie=utf8&_sug_=n&_sug_type_=";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("query", wechatName);//公众号名称
        URI uri = new UriTemplate(url).expand(paramMap);
        url = uri.toString();

        //获取webchatList
        String snuid = getSnuid();
        String webchatListResp = HttpTool.get(url, getSoGouHeaderMap(snuid));
        //解析最新文章的跳转链接 link?url=dn9a_-gY295K0Rci_xozVXfdMkSQTLW6cwJThYulHEtVjXrGTiVgSzeeEp_hLeB2yqXURLyfUtLrJXD1_0k9nVqXa8Fplpd9zzyQJTTG4l0_9qb01isMvlZ64lZu95gQIMueGIVv5d1dqVpT_pMk4offWRUgcoizEACiaxkcwhvTWNDOS5z7xv1sl3spDge3bn9dTu5NB8apP4DxpTPU2JJXAvEOlBkhNcfvnKsLq2y_fex_FvLObSeMu8iyJiWt4mSINg6uUw7I3OTRHYU2og..&amp;type=1&amp;query=36%E6%B0%AA&amp;token=67498C9B4AEFECA0CACC0A62E51D7A8ACA9C78CD60B61980
        String sougouLink = parseArticleLink(webchatListResp, wechatName);
        return downloadWechatArticle(sougouLink);
    }

    private String downloadWechatArticle(String sougouLink) {
        String snuid = getSnuid();
        String linkResp = HttpTool.get(sougouLink, getSoGouHeaderMap(snuid));
        // 得到https://mp.weixin.qq.com/s?src=11&timestamp=1622537971&ver=3103&signature=-I-7L0hCDOh1LfBwKNDSpjZ1sUYizUM0P8Rbn5XJddf21B3mRfF*BCmJF9AdrZ0T3PjQkZgQ0rS*5tk0NAN*BvKliGSVCAhgDP5Y2ScozkF8tGp07aecT-9yqxUEBlrU&new=1
        String weChatArticleUrl = getWechatArticleUrl(linkResp);
        // 解析微信文章内容
        String articleResp = SpiderUtil.getArticle(weChatArticleUrl);
        return articleResp;

    }

    private String getWechatArticleUrl(String resp) {
        if (StringUtils.isBlank(resp)) {
            return null;
        }

        int startIndex = resp.indexOf("http://mp.w");
        int endIndex = resp.indexOf("&new=1");
        if (startIndex == -1 || endIndex == -1) {
            return null;
        }

        String url = resp.substring(startIndex, endIndex + 6);
        url = url.replaceAll("';        url \\+\\= '", "");
        return url;
    }

    private String parseArticleLink(String html, String wechatName) {
        String articleLink = null;
        try {
            Document doc = Jsoup.parse(html);
            //拿到ul 列表数据
            Elements liList = doc.select("ul.news-list2 li");

            Element liElt = null;
            while (liList.size() > 0) {
                liElt = liList.first();
                Element wechatElt = liElt.selectFirst("p.tit");
                //找到与查询公众号wechatName名称相同的一项，拿到最新文章的跳转链接
                if (wechatElt != null && wechatName.equals(wechatElt.text())) {
                    Element aElt = liElt.selectFirst("dl a[uigs]");
                    if (aElt != null) {
                        articleLink = aElt.attr("href");
                        break;
                    }
                }
                liList = liList.next();
            }
        } catch (Exception e) {
            log.info("解析微信公众号最新文章跳转链接出错，error: {}" + e.getMessage());
        }

        if (articleLink != null && articleLink.startsWith("/link")) {
            articleLink = "https://weixin.sogou.com" + articleLink;
        }
        return articleLink;
    }

    /**
     * 搜狗请求头设置
     */
    private static Map<String, String> getSoGouHeaderMap(String snuid) {
        Map<String, String> map = new HashMap<>(new LinkedHashMap<>());
        map.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36");
        map.put("Cookie", "SNUID=" + snuid + ";");
        return map;
    }

    private String getSnuid() {
        CloseableHttpClient httpClient = null;
        CookieStore cookieStore = null;
        String url = "https://www.sogou.com/web?query=333&_asf=www.sogou.com&_ast=1488955851&w=01019900&p=40040100&ie=utf8&from=index-nologin";
        int timeout = 30000;
        String snuid = null;
        try {
            cookieStore = new BasicCookieStore();
            HttpClientContext context = HttpClientContext.create();
            context.setCookieStore(cookieStore);
            RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
            httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).setDefaultCookieStore(cookieStore).build();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build());
            httpGet.setHeader("Cookie",
                    "ABTEST=0|1488956269|v17;IPLOC=CN3301;SUID=E9DA81B7290B940A0000000058BFAB6D;PHPSESSID=rfrcqafv5v74hbgpt98ah20vf3;SUIR=1488956269");
            httpClient.execute(httpGet);
            for (Cookie c : cookieStore.getCookies()) {
                if (c.getName().equals("SNUID")) {
                    snuid = c.getValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return snuid;
    }
}
