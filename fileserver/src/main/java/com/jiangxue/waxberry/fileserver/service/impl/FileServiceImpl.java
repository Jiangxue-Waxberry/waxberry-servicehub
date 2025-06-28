package com.jiangxue.waxberry.fileserver.service.impl;

import com.jiangxue.waxberry.fileserver.entity.FileDesc;
import com.jiangxue.waxberry.fileserver.repository.FileRepository;
import com.jiangxue.waxberry.fileserver.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.time.LocalDateTime;
import com.jiangxue.waxberry.fileserver.util.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import com.jiangxue.waxberry.fileserver.util.MinioPathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文件服务实现类
 * 实现文件上传、下载、删除和信息查询等核心功能
 *
 * @author jiangxue
 * @version 1.0
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    private final MinioUtil minioUtil;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository, MinioUtil minioUtil) {
        this.fileRepository = fileRepository;
        this.minioUtil = minioUtil;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileDesc upload(MultipartFile file, String creator, String client, String securityLevel, boolean encrypt, String product) throws Exception {
        log.info("开始处理文件上传: {}, 创建者: {}, 客户端: {}", file.getOriginalFilename(), creator, client);

        // 1. 生成存储路径
        LocalDate now = LocalDate.now();
        String objectName = MinioPathUtil.generateObjectName(product, now, file.getOriginalFilename());
        log.debug("生成的文件存储路径: {}", objectName);

        // 2. 计算文件MD5
        String md5 = DigestUtils.md5DigestAsHex(file.getInputStream());
        log.debug("文件MD5: {}", md5);

        // 3. 上传文件到MinIO
        minioUtil.uploadFile(objectName, file.getInputStream(), file.getSize(), file.getContentType());
        log.debug("文件已上传到MinIO");

        // 4. 保存文件信息到数据库
        FileDesc entity = new FileDesc();
        entity.setFileName(file.getOriginalFilename());
        entity.setSuffixName(FilenameUtils.getExtension(file.getOriginalFilename()));
        entity.setFileSize(file.getSize());
        entity.setFilePath(objectName);
        entity.setDeleteFlag(false);
        entity.setDownLoadCount(0);
        entity.setRowCreateTime(LocalDateTime.now());
        entity.setRowCreate(creator);
        entity.setRowCreateClient(client);
        entity.setSecurityLevel(securityLevel);
        entity.setMd5(md5);
        entity.setFileEncrypt(encrypt);

        FileDesc savedEntity = fileRepository.save(entity);
        log.info("文件信息已保存到数据库, ID: {}", savedEntity.getId());
        return savedEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InputStream download(Long id) throws Exception {
        log.info("开始处理文件下载, ID: {}", id);
        
        FileDesc entity = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文件不存在: " + id));
        
        // 更新下载次数
        entity.setDownLoadCount(entity.getDownLoadCount() + 1);
        fileRepository.save(entity);
        log.debug("文件下载次数已更新: {}", entity.getDownLoadCount());

        // 从MinIO下载文件
        InputStream inputStream = minioUtil.downloadFile(entity.getFilePath());
        if (inputStream == null) {
            throw new RuntimeException("文件下载失败: " + id);
        }
        
        log.info("文件下载成功: {}", entity.getFileName());
        return inputStream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) throws Exception {
        log.info("开始处理文件删除, ID: {}", id);
        
        FileDesc entity = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文件不存在: " + id));

        // 从MinIO删除文件
        minioUtil.deleteFile(entity.getFilePath());
        log.debug("文件已从MinIO删除");

        // 更新数据库记录
        entity.setDeleteFlag(true);
        fileRepository.save(entity);
        log.info("文件删除成功: {}", entity.getFileName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileDesc getInfo(Long id) {
        log.info("获取文件信息, ID: {}", id);
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文件不存在: " + id));
    }
}
