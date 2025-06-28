package com.jiangxue.waxberry.manager.agent.constant;


public class WaxberryConstant {

    /**
     * 是否发布
     */
    public static class WaxberryStatus {
        public static final int UN_PUBLISH = 0;
        public static final int PUBLISH = 2;
    }

    /**
     * 杨梅类型
     */
    public static class WaberryType {
        public static final int WAXBERRY_APP = 0;
        public static final int WAXBERRY_AGENT = 1;
        public static final int WAXBERRY_SM = 2;
        public static final int WAXBERRY_LLM = 3;
    }

    /**
     * Agent是否使用工具
     */
    public static class AgentUseToolBox {
        public static final String UN_USE = "0";
        public static final String USE = "1";
    }

}
