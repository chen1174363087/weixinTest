package com.chenxin.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@RestController
public class CreateMenu {
    @RequestMapping(value = "/createMenu", method = RequestMethod.GET)
    public String createMenu() {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=  ";
        URL url = null;
        HttpsURLConnection conn = null;
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            url = new URL(requestUrl);
            try {
                conn = (HttpsURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);

                // conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                conn.setRequestProperty("Charset", "UTF-8");
                jsonObject.put("type", "click");
                jsonObject.put("name", "今日歌曲");
                jsonObject.put("key", "V1001_TODAY_MUSIC");
                jsonArray.add(jsonObject);
                jsonObject.clear();

                jsonObject.put("name", "菜单");

                jsonObject1.put("type", "view");
                jsonObject1.put("name", "搜索");
                jsonObject1.put("url", "http://www.soso.com/");

                jsonObject2.put("type", "click");
                jsonObject2.put("key", "V1001_GOOD");
                JSONArray array = new JSONArray();
                array.add(jsonObject1);
                array.add(jsonObject2);

                jsonObject.put("sub_button", array.toJSONString());

                jsonArray.add(jsonObject);

                // 设置请求方式（GET/POST）
                try {
                    conn.setRequestMethod("POST");
                    conn.connect();
                    //建立输入流，向指向的URL传入参数
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(jsonArray.toJSONString());
                    dos.flush();
                    dos.close();

                    // 从输入流读取返回内容
                    InputStream inputStream = conn.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String str = null;
                    StringBuffer buffer = new StringBuffer();
                    while ((str = bufferedReader.readLine()) != null) {
                        buffer.append(str);
                    }

                    jsonObject = JSONObject.parseObject(buffer.toString());

                    // 释放资源
                    bufferedReader.close();
                    inputStreamReader.close();
                    inputStream.close();
                    inputStream = null;
                    conn.disconnect();

                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return jsonObject.toJSONString();
    }
}
