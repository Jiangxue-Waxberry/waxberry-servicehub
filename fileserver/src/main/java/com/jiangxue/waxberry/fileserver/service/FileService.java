package com.jiangxue.waxberry.fileserver.service;

import com.jiangxue.waxberry.fileserver.entity.FileDesc;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

/**
 * 文件服务接口
 * 提供文件上传、下载、删除和信息查询等核心功能
 *
 * @author jiangxue
 * @version 1.0
 */
public interface FileService {

    /**
     * 上传文件
     *
     * @param file 要上传的文件
     * @param creator 文件创建者
     * @param client 客户端标识
     * @param securityLevel 安全级别
     * @param encrypt 是否加密
     * @param product 产品标识
     * @return 文件描述信息
     * @throws Exception 上传过程中可能发生的异常
     */
    FileDesc upload(MultipartFile file, String creator, String client, String securityLevel, boolean encrypt, String product) throws Exception;

    /**
     * 下载文件
     *
     * @param id 文件ID
     * @return 文件输入流
     * @throws Exception 下载过程中可能发生的异常
     */
    InputStream download(Long id) throws Exception;

    /**
     * 删除文件
     *
     * @param id 文件ID
     * @throws Exception 删除过程中可能发生的异常
     */
    void delete(Long id) throws Exception;

    /**
     * 获取文件信息
     *
     * @param id 文件ID
     * @return 文件描述信息
     */
    FileDesc getInfo(Long id);
}
