package com.jiangxue.waxberry.manager.commonChat.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiangxue.waxberry.manager.commonChat.constant.CommonChatchatConstant;
import com.jiangxue.waxberry.manager.commonChat.dto.CommonChatchatDto;
import com.jiangxue.waxberry.manager.commonChat.service.CommonChatchatService;
import com.jiangxue.waxberry.manager.agent.constant.WaxberryConstant;
import com.jiangxue.waxberry.manager.agent.entity.AgentSupplementaryInfo;
import com.jiangxue.waxberry.manager.agent.service.AgentSupplementaryInfoService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


@Service
public class CommonChatchatServiceImpl implements CommonChatchatService {

    @Autowired
    private AgentSupplementaryInfoService agentSupplementaryInfoService;

    @Override
    public JSONObject functionData(CommonChatchatDto commonChatchatDTO) {
        JSONObject data = new JSONObject();
        data.put(CommonChatchatConstant.ParamKey.URL, getUrl(commonChatchatDTO));
        data.put(CommonChatchatConstant.ParamKey.PARAMS,getParams(commonChatchatDTO));
        return data;
    }

    private String getUrl(CommonChatchatDto commonChatchatDTO) {
        String functionType = commonChatchatDTO.getFunctionType();
        if(CommonChatchatConstant.AiType.NEW_WAXBERRY_CHAT.equals(functionType)) {
            return System.getProperty("coreUrl").concat(CommonChatchatConstant.NEW_CHAT_COMPLETIONS_URL);
        }
        return "";
    }

    private JSONObject getParams(CommonChatchatDto commonChatchatDTO) {
        String functionType = commonChatchatDTO.getFunctionType();
        JSONObject data = commonChatchatDTO.getFunctionData();

        if(CommonChatchatConstant.AiType.NEW_WAXBERRY_CHAT.equals(functionType)) {
            if(String.valueOf(WaxberryConstant.WaberryType.WAXBERRY_AGENT).equals(data.getString("waxberryType"))){
                //工业智能体对话需要将角色指令进行拆分，然后将拆分后的数据和历史对话数据进行拼接
                JSONArray conversationMessage = data.getJSONArray("conversationMessage");
                String useTool = WaxberryConstant.AgentUseToolBox.UN_USE;
                String agentSystemPrompt = null;
                String impose = null;
                if(!ObjectUtils.isEmpty(data.getString("waxberryId"))){
                    AgentSupplementaryInfo agentSupplementaryInfo = agentSupplementaryInfoService.findByWaxberryId(data.getString("waxberryId"));
                    if(agentSupplementaryInfo != null) {
                        useTool = agentSupplementaryInfo.getUseToolBox();
                        agentSystemPrompt = agentSupplementaryInfo.getRoleInstruction();
                        impose = agentSupplementaryInfo.getImpose();
                    }
                }
                if(!StringUtils.isEmpty(agentSystemPrompt)){
                    // 转义特殊字符
                    agentSystemPrompt = StringEscapeUtils.escapeJson(agentSystemPrompt);
                }

                //user中添加限制条件
                if(conversationMessage != null && impose != null) {
                    String newQuery = conversationMessage.getJSONObject(conversationMessage.size()-1).getString("query")+ impose;
                    conversationMessage.getJSONObject(conversationMessage.size()-1).put("query", newQuery);
                }

                // 构建大模型问答的参数JSON对象
                return JSONObject.parseObject(String.format(CommonChatchatConstant.NEW_WAXBERRY_AGENT_QA_PARAM,
                        getWaxberryHistoryData(conversationMessage,data.getJSONArray("file"),data.getJSONArray("picture")),
                        data.getString("sessionId"),
                        data.getString("containerId"),
                        data.getString("waxberryType"),//0:工业APP 1:工业智能体 2:工业小模型 3:工业垂直领域大模型
                        data.getString("waxberryAppPort"),
                        useTool,//0:不使用工具（杨梅系统提示词不使用，只使用agentSystemPrompt提示词） 1:使用工具（将agentSystemPrompt和杨梅系统提示词组装）
                        agentSystemPrompt,data.getString("waxberryId")));
            }else{
                // 构建大模型问答的参数JSON对象
                return JSONObject.parseObject(String.format(CommonChatchatConstant.NEW_WAXBERRY_QA_PARAM,
                        getWaxberryHistoryData(data.getJSONArray("conversationMessage"),data.getJSONArray("file"),data.getJSONArray("picture")),
                        data.getString("containerId"),
                        data.getString("waxberryType"),//0:工业APP 1:工业智能体 2:工业小模型 3:工业垂直领域大模型
                        data.getString("waxberryAppPort"),WaxberryConstant.AgentUseToolBox.USE,null,data.getString("waxberryId")));
            }

        }
        return null;
    }


    private JSONArray getWaxberryHistoryData(JSONArray data,JSONArray file,JSONArray picture) {
        JSONArray history = new JSONArray();
        if(null != data && data.size() > 0){
            for(int i = 0; i< data.size(); i++){
                JSONObject conversationMessage = data.getJSONObject(i);
                String query = conversationMessage.getString("query");
                String reponse = conversationMessage.getString("reponse");
                if(!StringUtils.isEmpty(query)){
                    history.add(getContent(CommonChatchatConstant.HistoryRole.USER_ROLE,query));
                }
                if(!StringUtils.isEmpty(reponse)){
                    history.add(getContent(CommonChatchatConstant.HistoryRole.ASSISTANT_ROLE,reponse));
                }
            }
        }
        return history;
    }

    private JSONObject getContent(String role, String content) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("role",role);
        jsonObject.put("content",content);
        return jsonObject;
    }

}
