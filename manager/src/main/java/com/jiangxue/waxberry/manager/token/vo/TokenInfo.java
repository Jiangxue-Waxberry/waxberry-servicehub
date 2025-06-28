package com.jiangxue.waxberry.manager.token.vo;

import java.io.Serializable;


public class TokenInfo implements Serializable {

    private static final long serialVersionUID = -129573290072390174L;

    private String tokenId;
    private String userId;
    private String userName; // 用户名
    private String loginName; // 登录名
    private String password; // 密码
    private String securityId; // 密级ID
    private String securityName; // 密级名称
    private String securityValue; // 密级值
    private String departmentId; // 主要部门ID
    private String departmentName; // 主要部门名称
    private String ip; // IP地址
    private String generateTime; // 生成时间
    private String activeTime; // 活动时间
    private String timeLong;
    private String clientInfo; // 签名

    /**
     * debug模式下，固定token
     * @date     : 2019/9/11 12:22
     * @return   : com.lingy.eap.token.vo.TokenInfo
     * @author   : yangxh
     */
    public static TokenInfo debugToken(){
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setUserId("debug");
        tokenInfo.setUserName("调试模式");
        tokenInfo.setLoginName("debug");
        return tokenInfo;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityId() {
        return securityId;
    }

    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }

    public String getSecurityName() {
        return securityName;
    }

    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }

    public String getSecurityValue() {
        return securityValue;
    }

    public void setSecurityValue(String securityValue) {
        this.securityValue = securityValue;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(String generateTime) {
        this.generateTime = generateTime;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(String timeLong) {
        this.timeLong = timeLong;
    }
}
