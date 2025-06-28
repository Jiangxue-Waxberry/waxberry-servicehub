package com.jiangxue.waxberry.manager.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentRunCountDTO {

    private String agentId;
    private Long runCount;
}
