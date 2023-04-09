package com.mundane.mail.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.mundane.mail.config.WeatherConfig;
import com.mundane.mail.pojo.Weather;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@EnableConfigurationProperties(WeatherConfig.class)
public class WeatherService {

    @Autowired
    private WeatherConfig weatherConfig;

    public Weather queryWeather(String cityName) {
        Map<String, Object> params = new HashMap<>();//组合参数
        params.put("city", cityName);
        params.put("key", weatherConfig.getApiKey());
        String queryParams = urlencode(params);

        String responseJsonStr = doGet(weatherConfig.getApiUrl(), queryParams);
        try {
            JSONObject response = new JSONObject(responseJsonStr);
            int error_code = response.getInt("error_code");
            if (error_code == 0) {
                log.info("调用接口成功");

                JSONObject result = response.getJSONObject("result");
                JSONArray future = result.getJSONArray("future");
                String todayDate = DateUtil.formatDate(new Date());
                for (int i = 0; i < future.size(); i++) {
                    JSONObject jsonObj = future.getJSONObject(i);
                    String date = jsonObj.getStr("date");
                    if (!todayDate.equals(date)) {
                        continue;
                    }
                    String weatherStr = jsonObj.getStr("weather");
                    String temperature = jsonObj.getStr("temperature");
                    String[] temperatureArr = temperature.split("/");
                    Integer minTemperature = ReUtil.getFirstNumber(temperatureArr[0]);
                    Integer maxTemperature = ReUtil.getFirstNumber(temperatureArr[1]);

                    Weather weather = new Weather();
                    weather.setDate(date);
                    weather.setWeather(weatherStr);
                    weather.setMinTemperature(minTemperature);
                    weather.setMaxTemperature(maxTemperature);
                    return weather;
                }
                return null;
            } else {
                log.info("调用接口失败：" + response.getStr("reason"));
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get方式的http请求
     *
     * @param httpUrl 请求地址
     * @return 返回结果
     */
    private String doGet(String httpUrl, String queryParams) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(new StringBuffer(httpUrl).append("?").append(queryParams).toString());
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(5000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(6000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                // 封装输入流，并指定字符集
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                // 存放数据
                StringBuilder sbf = new StringBuilder();
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append(System.getProperty("line.separator"));
                }
                result = sbf.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();// 关闭远程连接
            }
        }
        return result;
    }

    /**
     * 将map型转为请求参数型
     *
     * @param data
     * @return
     */
    private String urlencode(Map<String, ?> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String result = sb.toString();
        result = result.substring(0, result.lastIndexOf("&"));
        return result;
    }
}
