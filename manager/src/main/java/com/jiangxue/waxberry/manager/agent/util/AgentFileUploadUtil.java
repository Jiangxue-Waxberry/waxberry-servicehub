package com.jiangxue.waxberry.manager.agent.util;


import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import jakarta.mail.internet.MimeUtility;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


@Slf4j
public class AgentFileUploadUtil {


    /**
     * 描述 : 上传代码文件至Agent
     * @param url
     * @param file
     * @param data
     * @return
     * @throws IOException
     */
    public static void uploadFile(String url, File file, JSONObject data) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);

            // 创建MultipartEntityBuilder并设置UTF-8编码
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(StandardCharsets.UTF_8);
            // 设置模式为BROWSER_COMPATIBLE
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            // 添加文件，使用ContentDisposition来正确处理文件名
            ContentType contentType = ContentType.create("application/octet-stream", StandardCharsets.UTF_8);
            String fileName = MimeUtility.encodeText(file.getName(), "UTF-8", "B");
            builder.addBinaryBody("file", file, contentType, fileName);

            // 添加文本字段
            builder.addTextBody("path", data.getString("path"), ContentType.create("text/plain", StandardCharsets.UTF_8));
//
//            // 添加文件
//            builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
//            // 添加文本字段
//            builder.addTextBody("path", data.getString("path"), ContentType.TEXT_PLAIN);

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // 检查响应状态
                if (response.getStatusLine().getStatusCode() != 200) {
                    HttpEntity entity = response.getEntity();
                    String responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                    log.error("上传文件失败，响应状态码: {}, 响应内容: {}", response.getStatusLine().getStatusCode(), responseBody);
                } else {
                    log.info("文件上传成功，响应状态码: {}", response.getStatusLine().getStatusCode());
                }
            }catch (IOException e) {
                // 打印异常信息
                log.error("上传代码文件发生异常: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * 描述 : 文件服务器下载文件
     * @param url
     * @return {@link InputStream}
     * @throws
     */
    public static InputStream downLoadByUrl(String url) {
        if (StringUtils.isEmpty(url)){
            return null;
        }
        InputStream is = null;
        try {
            is = new URL(url).openStream();
        } catch (MalformedURLException e) {
            log.error("下载文件发生异常",e);
        } catch (IOException e) {
            log.error("下载文件发生异常",e);
        }
        return is;
    }

    public static File stream2file (InputStream in,String tempFilePath) throws IOException {
        final File tempFile = new File(tempFilePath);
        FileUtils.copyInputStreamToFile(in,tempFile);
        return tempFile;
    }

}
