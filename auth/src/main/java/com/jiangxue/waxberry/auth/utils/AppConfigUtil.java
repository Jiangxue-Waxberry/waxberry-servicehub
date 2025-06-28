package com.jiangxue.waxberry.auth.utils;

public class AppConfigUtil {

    private static String privateKeyPem;

    public static String getPrivateKeyPem() {
        return privateKeyPem;
    }

    public static void setPrivateKeyPem(String key) {
        privateKeyPem = key;
    }

}
