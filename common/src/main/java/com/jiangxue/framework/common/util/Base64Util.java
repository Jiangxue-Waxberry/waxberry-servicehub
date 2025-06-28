package com.jiangxue.framework.common.util;

import java.util.Base64;

public class Base64Util {

    /**
     * 解码Base64编码的字符串。
     * 如果输入不是有效的Base64编码，则返回null或空数组。
     *
     * @param base64EncodedString Base64编码的字符串。
     * @return 解码后的字节数组，如果输入不合法，则返回null或空数组。
     */
    public static byte[] decode(String base64EncodedString) {
        try {
            return Base64.getDecoder().decode(base64EncodedString);
        } catch (IllegalArgumentException e) {
            System.err.println("提供的字符串不是有效的Base64编码: " + e.getMessage());
            // 返回null或根据需求返回空数组
            return null;
        }
    }

    public static void main(String[] args) {
        String base64EncodedString = "你的Base64编码的字符串";
        byte[] decodedBytes = Base64Util.decode(base64EncodedString);

        if (decodedBytes != null) {
            // 将字节转换为字符串（如果适用）
            String decodedString = new String(decodedBytes);
            System.out.println("解码后的字符串: " + decodedString);
        } else {
            System.out.println("解码失败");
        }
    }
}
