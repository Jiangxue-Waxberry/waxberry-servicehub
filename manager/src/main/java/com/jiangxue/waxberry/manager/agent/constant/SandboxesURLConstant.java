package com.jiangxue.waxberry.manager.agent.constant;



public class SandboxesURLConstant {

    public SandboxesURLConstant() {
    }

    // 沙箱容器上传代码文件接口地址:容器id/upload
    public static String AGENT_UPLOAD_FILE_URL = "/sandboxes/%s/upload";

    // 后台chat服务删除会话接口
    public static String AGENT_DELETE_SESSION_URL = "/chat/session/delete/%s";



}
