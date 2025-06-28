package com.jiangxue.waxberry.manager.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentRankDTO {

	private String agentId;
	private String agentName;
	private String agentLabel;
	private String description;
	private String coverFileId;
	private Long count;

}