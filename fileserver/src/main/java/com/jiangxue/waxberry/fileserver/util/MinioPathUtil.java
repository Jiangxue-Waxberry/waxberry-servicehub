package com.jiangxue.waxberry.fileserver.util;

import org.springframework.util.ObjectUtils;
import java.time.LocalDate;

public class MinioPathUtil {

    private static final String DEFAULT_MINIO_PATH = "tmp";

    public static String generateObjectName(String product, LocalDate date, String fileName) {
        String year = String.valueOf(date.getYear());
        String monthDay = String.format("%02d%02d", date.getMonthValue(), date.getDayOfMonth());
        return String.format("%s/%s/%s/%s", ObjectUtils.isEmpty(product)?DEFAULT_MINIO_PATH : product, year, monthDay, fileName);
    }


}
