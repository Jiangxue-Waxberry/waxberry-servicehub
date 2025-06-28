package com.jiangxue.waxberry.fileserver.controller;

import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.fileserver.entity.FileDesc;
import com.jiangxue.waxberry.fileserver.service.FileService;
import com.jiangxue.waxberry.fileserver.util.MimeTypeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 文件服务控制器
 * 提供文件上传、下载、删除和信息查询等功能的REST API接口
 *
 * @author jiangxue
 * @version 1.0
 */
@Slf4j
@Tag(name = "文件服务接口", description = "提供文件上传、下载、删除和信息查询等功能")
@Validated
@RestController
@RequestMapping("/fs/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

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
     */
    @Operation(summary = "上传文件", description = "上传文件并返回文件描述信息")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResult<FileDesc>> upload(
            @Parameter(description = "文件") @NotNull @RequestParam MultipartFile file,
            @Parameter(description = "创建者") @NotBlank @RequestParam String creator,
            @Parameter(description = "客户端") @NotBlank @RequestParam String client,
            @Parameter(description = "安全级别") @NotBlank @RequestParam String securityLevel,
            @Parameter(description = "是否加密") @RequestParam boolean encrypt,
            @Parameter(description = "产品标识") @NotBlank @RequestParam String product) {
        try {
            log.info("开始上传文件: {}, 创建者: {}, 客户端: {}", file.getOriginalFilename(), creator, client);
            FileDesc result = fileService.upload(file, creator, client, securityLevel, encrypt, product);
            log.info("文件上传成功: {}", result.getFilePath());
            return ResponseEntity.ok(ApiResult.success(result));
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResult.error("文件上传失败: " + e.getMessage()));
        }
    }

    /**
     * 下载文件
     *
     * @param id 文件ID
     * @return 文件资源
     */
    @Operation(summary = "下载文件", description = "根据文件ID下载文件")
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(
            @Parameter(description = "文件ID") @PathVariable Long id) {
        try {
            log.info("开始下载文件, ID: {}", id);
            FileDesc fileDesc = fileService.getInfo(id);
            if (fileDesc == null || fileDesc.getDeleteFlag()) {
                log.warn("文件不存在或已删除, ID: {}", id);
                return ResponseEntity.notFound().build();
            }

            InputStream is = fileService.download(id);
            if (is == null) {
                log.warn("文件下载失败, ID: {}", id);
                return ResponseEntity.notFound().build();
            }

            String fileName = URLEncoder.encode(fileDesc.getFileName(), StandardCharsets.UTF_8.toString());
            String contentType = MimeTypeUtil.getContentType(fileDesc.getSuffixName());

            log.info("文件下载成功: {}", fileDesc.getFileName());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileDesc.getFileSize()))
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                    .body(new InputStreamResource(is));

        } catch (Exception e) {
            log.error("文件下载失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 删除文件
     *
     * @param id 文件ID
     * @return 操作结果
     */
    @Operation(summary = "删除文件", description = "根据文件ID删除文件")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResult<Void>> delete(
            @Parameter(description = "文件ID") @PathVariable Long id) {
        try {
            log.info("开始删除文件, ID: {}", id);
            fileService.delete(id);
            log.info("文件删除成功, ID: {}", id);
            return ResponseEntity.ok(ApiResult.success());
        } catch (Exception e) {
            log.error("文件删除失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResult.error("文件删除失败: " + e.getMessage()));
        }
    }

    /**
     * 获取文件信息
     *
     * @param id 文件ID
     * @return 文件描述信息
     */
    @Operation(summary = "获取文件信息", description = "根据文件ID获取文件描述信息")
    @GetMapping("/getInfo/{id}")
    public ResponseEntity<ApiResult<FileDesc>> getInfo(
            @Parameter(description = "文件ID") @PathVariable Long id) {
        try {
            log.info("获取文件信息, ID: {}", id);
            FileDesc fileDesc = fileService.getInfo(id);
            if (fileDesc == null) {
                log.warn("文件不存在, ID: {}", id);
                return ResponseEntity.ok(ApiResult.error("文件不存在"));
            }
            return ResponseEntity.ok(ApiResult.success(fileDesc));
        } catch (Exception e) {
            log.error("获取文件信息失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResult.error("获取文件信息失败: " + e.getMessage()));
        }
    }
}