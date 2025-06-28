package com.jiangxue.waxberry.manager.agent.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentApprovalDTO  implements Serializable  {

    String id;

    String approvalCode;

    String username;

    String mobile;

    String userType;

    String agentName;

    Integer agentType;

    String agentDiscription;

    String agentClassification;

    Integer agentIsmodify;

    Date agentCreateTime;

    Date approvalCreateTime;

    Integer hour;

    String approvalStatus;

    String agentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Integer getAgentType() {
        return agentType;
    }

    public void setAgentType(Integer agentType) {
        this.agentType = agentType;
    }

    public String getAgentDiscription() {
        return agentDiscription;
    }

    public void setAgentDiscription(String agentDiscription) {
        this.agentDiscription = agentDiscription;
    }

    public String getAgentClassification() {
        return agentClassification;
    }

    public void setAgentClassification(String agentClassification) {
        this.agentClassification = agentClassification;
    }

    public Integer getAgentIsmodify() {
        return agentIsmodify;
    }

    public void setAgentIsmodify(Integer agentIsmodify) {
        this.agentIsmodify = agentIsmodify;
    }

    public Date getAgentCreateTime() {
        return agentCreateTime;
    }

    public void setAgentCreateTime(Date agentCreateTime) {
        this.agentCreateTime = agentCreateTime;
    }

    public Date getApprovalCreateTime() {
        return approvalCreateTime;
    }

    public void setApprovalCreateTime(Date approvalCreateTime) {
        this.approvalCreateTime = approvalCreateTime;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
