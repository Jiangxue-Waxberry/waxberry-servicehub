package com.jiangxue.waxberry.manager.attachment.service;



import com.alibaba.fastjson.JSONObject;

import java.util.Map;


public interface AttachmentService {

	Map<String, Object> getFileInfos(JSONObject json);


}
