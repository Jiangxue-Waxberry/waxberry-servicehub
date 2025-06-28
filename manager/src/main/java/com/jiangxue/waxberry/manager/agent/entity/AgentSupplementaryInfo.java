package com.jiangxue.waxberry.manager.agent.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;



@Data
@Entity
@Table(name="mgr_agent_supplementaryinfo")
@Validated
public class AgentSupplementaryInfo implements Serializable {


	@Id
	@Column
	@GeneratedValue(generator = "custom-uuid")
	@GenericGenerator(name = "custom-uuid", strategy = "com.jiangxue.framework.common.util.CustomUUIDGenerator")
	private String id;

	@Column(name = "waxberry_id")
	private String waxberryId;

	//是否使用工具箱
	@Column(name = "use_tool_box")
	private String useToolBox;

	//限制
	@Column(name = "impose", columnDefinition = "TEXT")
	private String impose;

	//角色指令
	@Column(name = "role_instruction", columnDefinition = "LONGTEXT")
	private String roleInstruction;

	//开场白
	@Column(name = "prologue", columnDefinition = "TEXT")
	private String prologue;

	//问题一
	@Column(name = "recommended_question_one", columnDefinition = "TEXT")
	private String recommendedQuestionOne;

	//问题二
	@Column(name = "recommended_question_two", columnDefinition = "TEXT")
	private String recommendedQuestionTwo;

	//问题三
	@Column(name = "recommended_question_three", columnDefinition = "TEXT")
	private String recommendedQuestionThree;

	@Column(name = "creator_id")
	private String creatorId;

	@Column(name = "updater_id")
	private String updaterId;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "update_time")
	private Date updateTime;



}
