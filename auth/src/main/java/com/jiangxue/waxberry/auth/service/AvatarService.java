package com.jiangxue.waxberry.auth.service;

import io.minio.*;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class AvatarService {
    
    private static final String USER_DATA_BUCKET = "user-data";
    

    @Value("http://localhost:9000")
    private String minioEndpoint;
    
    @Value("minioadmin")
    private String minioAccessKey;
    
    @Value("minioadmin")
    private String minioSecretKey;
     
    private MinioClient createMinioClient() {    
        return MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioAccessKey, minioSecretKey)
                .build();
    }
    
    
    public void uploadAvatar(String username, String extension, MultipartFile file) throws Exception {
        MinioClient minioClient = createMinioClient();
        
        // Check if bucket exists, create if it doesn't
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(USER_DATA_BUCKET)
                .build());
                
        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(USER_DATA_BUCKET)
                    .build());
        }
        
        // Delete existing avatar if any
        deleteExistingAvatar(minioClient, username);
        
        // Upload the new avatar
        String objectName = username + "/avatar." + extension;
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(USER_DATA_BUCKET)
                .object(objectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());
    }
    
    private void deleteExistingAvatar(MinioClient minioClient, String username) {
        try {
            // List all objects in user's directory to find the avatar file
            ListObjectsArgs listArgs = ListObjectsArgs.builder()
                    .bucket(USER_DATA_BUCKET)
                    .prefix(username + "/avatar.")
                    .recursive(false)
                    .build();
            
            Iterable<Result<Item>> results = minioClient.listObjects(listArgs);
            
            for (Result<Item> result : results) {
                String objectName = result.get().objectName();
                // Delete the existing avatar file
                minioClient.removeObject(RemoveObjectArgs.builder()
                        .bucket(USER_DATA_BUCKET)
                        .object(objectName)
                        .build());
                log.info("Deleted existing avatar for user: {}", username);
            }
        } catch (Exception e) {
            log.warn("Error deleting existing avatar for user: {}", username, e);
        }
    }

    public record AvatarData(byte[] content, String contentType) {}

    public AvatarData getAvatar(String username) throws Exception {
        MinioClient minioClient = createMinioClient();
        
        try {
            // List all objects in user's directory to find the avatar file
            ListObjectsArgs listArgs = ListObjectsArgs.builder()
                    .bucket(USER_DATA_BUCKET)
                    .prefix(username + "/avatar.")
                    .recursive(false)
                    .build();
            
            Iterable<Result<Item>> results = minioClient.listObjects(listArgs);
            String objectName = null;
            
            for (Result<Item> result : results) {
                objectName = result.get().objectName();
                break;
            }
            
            if (objectName == null) {
                throw new Exception("Avatar not found for user: " + username);
            }

            // Get object's metadata to retrieve content type
            StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(USER_DATA_BUCKET)
                    .object(objectName)
                    .build()
            );

            GetObjectResponse response = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(USER_DATA_BUCKET)
                    .object(objectName)
                    .build()
            );
            
            return new AvatarData(response.readAllBytes(), stat.contentType());
        } catch (Exception e) {
            log.error("Error getting avatar for user: " + username, e);
            throw e;
        }
    }
}
