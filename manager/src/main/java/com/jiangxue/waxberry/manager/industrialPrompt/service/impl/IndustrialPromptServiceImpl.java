package com.jiangxue.waxberry.manager.industrialPrompt.service.impl;

import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.waxberry.manager.industrialPrompt.dto.IndustrialPromptDto;
import com.jiangxue.waxberry.manager.industrialPrompt.entity.IndustrialPrompt;
import com.jiangxue.waxberry.manager.industrialPrompt.repository.IndustrialPromptRepository;
import com.jiangxue.waxberry.manager.industrialPrompt.service.IndustrialPromptService;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class IndustrialPromptServiceImpl implements IndustrialPromptService {

    @Autowired
    private IndustrialPromptRepository industrialPromptRepository;

    @Override
    public IndustrialPrompt save(IndustrialPrompt prompt) {
        String userId = SecurityUtils.requireCurrentUserId();
        if(!ObjectUtils.isEmpty(userId)){
            prompt.setCreatorId(userId);
            prompt.setUpdaterId(userId);
        }
        prompt.setCreateTime(new Date());
        prompt.setUpdateTime(new Date());
        return industrialPromptRepository.save(prompt);
    }

    @Override
    public IndustrialPrompt update(IndustrialPrompt prompt) {
        IndustrialPrompt one = industrialPromptRepository.findById(prompt.getId()).orElse(null);
        if(!ObjectUtils.isEmpty(one)){
            if(!StringUtils.isEmpty(prompt.getTitle())){
                one.setTitle(prompt.getTitle());
            }
            if(!StringUtils.isEmpty(prompt.getContent())){
                one.setContent(prompt.getContent());
            }
            if(!StringUtils.isEmpty(prompt.getStatus())){
                one.setStatus(prompt.getStatus());
            }
        }
        String userId = SecurityUtils.requireCurrentUserId();
        if(!ObjectUtils.isEmpty(userId)){
            prompt.setUpdaterId(userId);
        }
        prompt.setUpdateTime(new Date());
        return industrialPromptRepository.save(one);
    }

    public IndustrialPrompt findById(String id) {
        Optional<IndustrialPrompt> optional = industrialPromptRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }else{
            return null;
        }
    }

    public Map<String, Object> findAll(String title, String sort,String creatorId ) {
        // 确定排序方向
        boolean ascending = "asc".equalsIgnoreCase(sort);
        //判断title不为空才执行模糊搜索
        if (!StringUtils.isEmpty(title)) {
            List<IndustrialPromptDto> filteredPrompts = industrialPromptRepository.searchByTitleOrContent(title,creatorId);
            return getPinyinSort(filteredPrompts, ascending);
        }else {
            List<IndustrialPromptDto> filteredPrompts = industrialPromptRepository.findByCreatorId(creatorId);
            return getPinyinSort(filteredPrompts, ascending);
        }

    }

    private Map<String, Object> getPinyinSort(List<IndustrialPromptDto> filteredPrompts, boolean ascending) {
        // 设置拼音格式
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        // 根据首字母拼音排序
        filteredPrompts.sort((p1, p2) -> {
            String py1 = getFirstCharPinyin(p1.getTitle(), format);
            String py2 = getFirstCharPinyin(p2.getTitle(), format);
            int result = py1.compareTo(py2);
            return ascending ? result : py2.compareTo(py1);
        });

        // 为每个提示添加创建者信息
        List<Map<String, Object>> promptsWithCreator = filteredPrompts.stream()
                .map(prompt -> {
                    Map<String, Object> promptMap = new HashMap<>();
                    promptMap.put("id", prompt.getId());
                    promptMap.put("title", prompt.getTitle());
                    promptMap.put("content", prompt.getContent());
                    promptMap.put("createTime", prompt.getCreateTime());
                    promptMap.put("updateTime", prompt.getUpdateTime());
                    promptMap.put("updaterId",prompt.getUpdaterId());
                    promptMap.put("status", prompt.getStatus());
                    // 添加创建者信息
                    promptMap.put("creatorId", prompt.getCreatorId());
                    promptMap.put("creatorName", prompt.getCreatorName());
                    return promptMap;
                }).collect(Collectors.toList());

        // 提取排序后的标题
        List<String> sortedTitles = filteredPrompts.stream()
                .map(IndustrialPromptDto::getTitle)
                .collect(Collectors.toList());

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("titles", sortedTitles);
        result.put("prompts", promptsWithCreator);
        result.put("count", sortedTitles.size());
        return result;
    }

    private String getFirstCharPinyin(String text, HanyuPinyinOutputFormat format) {
        if (text == null || text.isEmpty()) return "#";
        char firstChar = text.charAt(0);
        try {
            if (Character.toString(firstChar).matches("[\\u4E00-\\u9FA5]")) {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(firstChar, format);
                return (pinyinArray != null && pinyinArray.length > 0)
                        ? pinyinArray[0].substring(0, 1).toUpperCase()
                        : "#";
            } else {
                return Character.toString(firstChar).toUpperCase();
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
            return "#";
        }
    }





    public void deleteById(String id) {
        industrialPromptRepository.deleteById(id);
    }

}
