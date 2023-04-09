package com.mundane.mail.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpUtils {
    /**
     * 获取请求参数,把请求参数转成Mapd
     *
     * @param request request对象
     * @return 参数集合map
     */
    public static Map<String, Object> getParameterMap(HttpServletRequest request) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, Object> returnMap = new HashMap<>();
            // 默认参数
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String value = "";
                String key = entry.getKey();
                Object valueObj = entry.getValue();
                log.info("key = {}, value = {}", key, valueObj);

                if (valueObj == null) {
                    value = "";
                } else if (!(valueObj instanceof String[])) {
                    value = valueObj.toString();
                } else {
                    String[] values = (String[]) valueObj;
                    for (String temp : values) {
                        value = temp + ",";
                    }
                    value = value.substring(0, value.length() - 1);
                }
                returnMap.put(key, value);
                //post等raw内容 content-type:application/json

            }

            // PostBody参数
            try {
                String body = IoUtil.read(request.getInputStream(), "utf-8");
                log.info("body = {}", body);
                if (StringUtils.isNotEmpty(body)) {
                    returnMap.putAll(JSONUtil.parseObj(body));
                }
            } catch (Exception e) {
                log.error("getParameterMap stream error", e);
            }

            return returnMap;
        } catch (Exception e) {
            log.error("getParameterMap error", e);
        }
        return new HashMap<>();
    }

}