package com.jiangxue.waxberry.fileserver.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "fs_file_desc")
public class FileDesc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String suffixName;
    private Long fileSize;
    private String filePath;
    private Boolean deleteFlag;
    private Integer downLoadCount;
    private LocalDateTime rowCreateTime;
    private String rowCreate;
    private String rowCreateClient;
    private LocalDateTime rowEndTime;
    private String securityLevel;
    private String md5;
    private Boolean fileEncrypt;

}
