package com.jiangxue.waxberry.manager.redemptionCode.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


public class GenerateCodeUtils {

    // 最大尝试次数，防止无限循环
    public static final int MAX_ATTEMPTS = 1000;
    public static Set<String> generatedCodes = new HashSet<>(); // 用于存储已生成的码

    // 计数器，用于增强唯一性
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    /**
     * 生成10位安全编码（使用SHA-256哈希）
     */
    public static String generateCode() {
        try {
            // 1. 生成基础UUID
            String uuid = UUID.randomUUID().toString();

            // 2. 添加计数器增强唯一性
            String input = uuid + "-" + COUNTER.incrementAndGet();

            // 3. 计算SHA-256哈希
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

            // 4. 将字节数组转换为长整型（取前8个字节）
            long num = 0;
            for (int i = 0; i < 8; i++) {
                num = (num << 8) | (hashBytes[i] & 0xFF);
            }

            // 5. 转换为正数并取模，确保10位数字
            long positiveNum = Math.abs(num);
            return String.format("%010d", positiveNum % 10_000_000_000L);

        } catch (NoSuchAlgorithmException e) {
            // 处理不可能发生的异常
            throw new RuntimeException("SHA-256算法不可用", e);
        }
    }

    /**
     * 生成带校验位的安全编码
     * 第10位为Luhn算法校验位
     */
    public static String generateCodeWithChecksum() {
        String baseCode = generateCode().substring(0, 9);
        int checkDigit = calculateLuhnCheckDigit(baseCode);
        return baseCode + checkDigit;
    }

    /**
     * 使用Luhn算法计算校验位
     */
    private static int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        return (10 - (sum % 10)) % 10;
    }

    /**
     * 验证带校验位的编码
     */
    public static boolean validateCode(String code) {
        if (code == null || code.length() != 10) {
            return false;
        }

        String number = code.substring(0, 9);
        int checkDigit = Integer.parseInt(code.substring(9));

        return calculateLuhnCheckDigit(number) == checkDigit;
    }
}
