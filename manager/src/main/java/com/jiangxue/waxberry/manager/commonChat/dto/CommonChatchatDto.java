package com.jiangxue.waxberry.manager.commonChat.dto;


import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;

public class CommonChatchatDto implements Serializable {

    private String functionType;

    private JSONObject functionData;

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    public JSONObject getFunctionData() {
        return functionData;
    }

    public void setFunctionData(JSONObject functionData) {
        this.functionData = functionData;
    }
}
