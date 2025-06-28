package com.jiangxue.waxberry.manager.commonChat.constant;


public class CommonChatchatConstant {

    /**
     *
     * 新chat接口
     *
     */
    public static final String NEW_CHAT_COMPLETIONS_URL = "/chat/session/stream";

    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE_NO = 0;

    /**
     * 默认页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 最近会话默认数量
     */
    public static final int CONVERSATION_MAX_NUM = 5;

    /**
     * 对话请求前缀
     */
    public static String CHATCHAT_PREFIX_URL = "/chatchat/";

    /**
     * 参数
     */
    public static class ParamKey {
        public static final String URL = "url";
        public static final String PARAMS = "params";
    }


    /**
     * 历史会话角色
     */
    public static class HistoryRole {
        public static final String USER_ROLE = "user";
        public static final String ASSISTANT_ROLE = "assistant";
    }

    /**
     * 会话类型
     */
    public static class ChatType {
        //智能问答
        public static final String PROFESSIONAL_QA = "0";
        //专业翻译
        public static final String INTELLIGENT_TRANSLATION = "1";
        //文档总结
        public static final String DOCUMENT_SUMMARY = "2";
        //AI写作
        public static final String AI_WRITING = "3";
        //个人文档
        public static final String PERSONAL_DOCUMENT = "4";
        //大模型问答
        public static final String LARGE_MODEL_QA = "5";
        //编程助手
        public static final String PROGRAMMING_ASSISTANT = "6";
        //文档问答
        public static final String DOCUMENT_QA = "7";
        //杨梅模块
        public static final String WAXBERRY_AGENT = "8";
    }

    /**
     * AI类型
     */
    public static class AiType {
        //知识库对话
        public static final String KB_CHAT = "0";
        //文件对话
        public static final String FILE_CHAT = "1";
        //openai对话
        public static final String COMPLETIONS_CHAT = "2";
        //杨梅模块对话
        public static final String WAXBERRY_CHAT = "3";
        //杨梅模块对话 新chat
        public static final String NEW_WAXBERRY_CHAT = "4";
    }


    /**
     *  杨梅问答参数 新chat
     */
    public static String NEW_WAXBERRY_QA_PARAM = "{\n" +
            "  \"messages\": %s,\n" +
            "  \"response_format\": {\n" +
            "    \"type\": \"text\"\n" +
            "  },\n" +
            "  \"stream\": true,\n" +
            "  \"temperature\": 0,\n" +
            "  \"metadata\": {\n" +
            "    \"containerId\": \"%s\",\n" +
            "    \"waxberryType\": \"%s\",\n" +
            "    \"waxberryAppPort\": \"%s\",\n" +
            "    \"useTool\": \"%s\",\n" +
            "    \"agentSystemPrompt\": \"%s\",\n" +
            "    \"waxberryID\": \"%s\"\n" +
            "  }\n" +
            "}";

    /**
     *  杨梅问答参数 新chat
     */
    public static String NEW_WAXBERRY_AGENT_QA_PARAM = "{\n" +
            "  \"messages\": %s,\n" +
            "  \"response_format\": {\n" +
            "    \"type\": \"text\"\n" +
            "  },\n" +
            "  \"stream\": true,\n" +
            "  \"temperature\": 0,\n" +
            "  \"sessionId\": \"%s\",\n" +
            "  \"metadata\": {\n" +
            "    \"containerId\": \"%s\",\n" +
            "    \"waxberryType\": \"%s\",\n" +
            "    \"waxberryAppPort\": \"%s\",\n" +
            "    \"useTool\": \"%s\",\n" +
            "    \"agentSystemPrompt\": \"%s\",\n" +
            "    \"waxberryID\": \"%s\"\n" +
            "  }\n" +
            "}";

}
