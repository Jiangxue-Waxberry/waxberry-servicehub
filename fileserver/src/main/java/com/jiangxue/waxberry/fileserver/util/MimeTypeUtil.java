package com.jiangxue.waxberry.fileserver.util;

import org.springframework.http.MediaType;
import java.util.Map;

/**
 * MIME 类型工具类
 * 提供文件类型和 MIME 类型的映射关系及处理方法
 *
 * @author jiangxue
 * @version 1.0
 */
public class MimeTypeUtil {

    /**
     * 文件类型与 MIME 类型的映射关系
     */
    private static final Map<String, String> CONTENT_TYPE_MAP = Map.of(
            "pdf", "application/pdf",
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg",
            "png", "image/png",
            "doc", "application/msword",
            "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "xls", "application/vnd.ms-excel",
            "xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "txt", "text/plain"
    );

    /**
     * 获取文件后缀对应的 MIME 类型
     *
     * @param suffix 文件后缀
     * @return MIME 类型
     */
    public static String getContentType(String suffix) {
        if (suffix == null) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return CONTENT_TYPE_MAP.getOrDefault(suffix.toLowerCase(), MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }

    /**
     * 检查文件类型是否支持
     *
     * @param suffix 文件后缀
     * @return 是否支持
     */
    public static boolean isSupportedType(String suffix) {
        if (suffix == null) {
            return false;
        }
        return CONTENT_TYPE_MAP.containsKey(suffix.toLowerCase());
    }

    /**
     * 获取所有支持的文件类型
     *
     * @return 支持的文件类型集合
     */
    public static Map<String, String> getSupportedTypes() {
        return Map.copyOf(CONTENT_TYPE_MAP);
    }
} 