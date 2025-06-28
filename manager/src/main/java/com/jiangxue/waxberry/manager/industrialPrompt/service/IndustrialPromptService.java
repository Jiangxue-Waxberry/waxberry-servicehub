package com.jiangxue.waxberry.manager.industrialPrompt.service;




import com.jiangxue.waxberry.manager.industrialPrompt.entity.IndustrialPrompt;

import java.util.Map;

public interface IndustrialPromptService {

    IndustrialPrompt save(IndustrialPrompt prompt);

    IndustrialPrompt update(IndustrialPrompt prompt);

    IndustrialPrompt findById(String id);

    Map<String, Object> findAll(String title,String sort,String creatorId);

    void deleteById(String id);

}
