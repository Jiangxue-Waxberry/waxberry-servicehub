package com.jiangxue.waxberry.manager.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndustryResourcesDTO {

    private String resources;
    private String type;
    private String version;
    private String createDate;
    private String categoryId;
    private String modelId;
    private String url;
    private int currentPage;
    private int pageSize;
    private String id;
}
