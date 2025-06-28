package com.jiangxue.waxberry.manager.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseMiniModelFileDTO {

    private String id;
    private String waxberryId;
    private String fileId;
    private String creatorId;
    private String updaterId;
    private Date createTime;
    private Date updateTime;
    private String filename;
    private BigDecimal filesize;  // 改为 BigDecimal filesize;

}
