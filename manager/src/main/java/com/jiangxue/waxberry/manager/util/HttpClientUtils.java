package com.jiangxue.waxberry.manager.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.ObjectUtils;
import java.util.Map;


@Slf4j
public class HttpClientUtils {

    //浏览器请求标识
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36";

    private HttpClientUtils() {}

    /**
     * @param type          0 get 1 post 2 delete 3 put
     * @param url           请求地址
     * @param header        请求头信息
     * @param params        请求参数
     * @param isJson        true把请求数据转换成json .false将使用表单的方式请求
     * @param authorization 认证信息
     * @param cookie        添加cookie请求
     * @return
     */
    public static String sendHttp(int type, String url, Map<String, String> header, Object params, boolean isJson, String authorization, String cookie) {
        String response = null;
        try {
            HttpRequest httpRequest;
            //默认post请求
            switch (type) {
                case 0:
                    httpRequest = HttpRequest.get(url);
                    break;
                case 1:
                    httpRequest = HttpRequest.post(url);
                    break;
                case 2:
                    httpRequest = HttpRequest.delete(url);
                    break;
                case 3:
                    httpRequest = HttpRequest.put(url);
                    break;
                default:
                    httpRequest = HttpRequest.post(url);
                    break;
            }
            //常用的请求header信息
            httpRequest.header("Connection", "Keep-Alive")
                    .header("Accept", "*/*")
                    .header("User-Agent", HttpClientUtils.USER_AGENT);
            //添加header
            if (!ObjectUtils.isEmpty(header)) {
                httpRequest.addHeaders(header);
            }
            //添加认证
            if (!ObjectUtils.isEmpty(authorization)) {
                httpRequest.header("Authorization", authorization);
            }
            //请求参数
            if (!ObjectUtils.isEmpty(params) && !isJson) {
                httpRequest.form((Map<String, Object>) params);
            } else if (!ObjectUtils.isEmpty(params) && isJson) {
                httpRequest.body(JSON.toJSONString(params));
            }
            //设置cookie
            if (!ObjectUtils.isEmpty(cookie)) {
                httpRequest.cookie("tokenId=" + cookie);
            }

            //设置请求编码
            httpRequest.charset(CharsetUtil.CHARSET_UTF_8);

            HttpResponse httpResponse = httpRequest.execute();
            if (httpResponse.isOk()) {
                response = httpResponse.body();
            } else {
                log.info("请求地址:{} 请求参数:{} 请求Cookie:{} 请求失败:{}", url, JSON.toJSONString(params), cookie, httpResponse.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 发送post返回jsonobject对象
     *
     * @param url
     * @param params
     * @return
     */
    public static JSONObject sendPostJson(String url, Object params) {
        return HttpClientUtils.sendPostJson(url, params, null);
    }

    /**
     * 发送post请求
     *
     * @param url     地址
     * @param params  参数(将会已json格式请求)
     * @param tokenId 用户的tokenId信息
     * @return
     */
    public static JSONObject sendPostJson(String url, Object params, String tokenId) {
        return HttpClientUtils.strToJsonObject(HttpClientUtils.sendHttp(1, url, null, params, true, null, tokenId));
    }


    /**
     * 字符串转换成json对象
     *
     * @param responseData
     * @return
     */
    public static JSONObject strToJsonObject(String responseData) {
        JSONObject jsonObject = null;
        if (ObjectUtils.isEmpty(responseData)) {
            return jsonObject;
        }
        try {
            jsonObject = JSON.parseObject(responseData);
        } catch (Exception e) {
            log.info("转换json失败{} {}", responseData, e);
        }
        return jsonObject;
    }

    public static String executePost(String url) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            org.apache.http.HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());

            System.out.println("Status Code: " + statusCode);
            return responseBody;
        }
    }

    /**
     * get请求返回jsonobject
     *
     * @param url
     * @param tokenId
     * @return
     */
    public static JSONObject sendGet(String url, String tokenId) {
        return strToJsonObject(sendHttp(0, url, null, null, false, null, tokenId));
    }

    public static String getSandBoxFileData(String url) {
        return sendHttp(0, url, null, null, false, null, null);
    }


}
