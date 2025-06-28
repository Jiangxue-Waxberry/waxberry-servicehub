package com.jiangxue.waxberry.fileserver.util;

import com.jiangxue.waxberry.fileserver.config.MinioProperties;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioUtil {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    /**
     * 上传文件到指定 bucket
     */
    public void uploadFile(String objectName, InputStream stream, long size, String contentType) {
        try {
            ensureBucketExists(minioProperties.getBucket());
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectName)
                            .stream(stream, size, -1)
                            .contentType(StringUtils.hasText(contentType) ? contentType : "application/octet-stream")
                            .build()
            );
        } catch (Exception e) {
            log.error("MinIO上传文件失败: {}", e.getMessage(), e);
            throw new RuntimeException("MinIO上传文件失败", e);
        }
    }

    /**
     * 下载文件
     */
    public InputStream downloadFile(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("MinIO下载文件失败: {}", e.getMessage(), e);
            throw new RuntimeException("MinIO下载文件失败", e);
        }
    }

    /**
     * 删除文件
     */
    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("MinIO删除文件失败: {}", e.getMessage(), e);
            throw new RuntimeException("MinIO删除文件失败", e);
        }
    }

    /**
     * 确保 bucket 存在，不存在则创建
     */
    private void ensureBucketExists(String bucketName) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }
}
