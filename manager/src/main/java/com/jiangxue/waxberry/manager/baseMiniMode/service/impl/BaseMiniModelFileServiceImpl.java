package com.jiangxue.waxberry.manager.baseMiniMode.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.waxberry.manager.agent.dto.BaseMiniModelFileDTO;
import com.jiangxue.waxberry.manager.agent.service.AgentService;
import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelFile;
import com.jiangxue.waxberry.manager.baseMiniMode.repository.BaseMiniModelFileRepository;
import com.jiangxue.waxberry.manager.baseMiniMode.service.BaseMiniModelFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BaseMiniModelFileServiceImpl implements BaseMiniModelFileService {


    @Autowired
    private BaseMiniModelFileRepository baseMiniModelFileRepository;

    @Autowired
    private AgentService agentService;

    @Override
    public BaseMiniModelFile saveFile(JSONObject json) {
        String waxberryId = json.getString("waxberryId");
        String fileId = json.getString("fileId");

        //TODO 优先上传至waxberry/attachment
        try{
            agentService.uploadAgentFile(json);
        }catch(Exception e){
            e.printStackTrace();
        }

        BaseMiniModelFile file = new BaseMiniModelFile();
        file.setWaxberryId(waxberryId);
        file.setFileId(fileId);
        String userId = SecurityUtils.requireCurrentUserId();
        if(!ObjectUtils.isEmpty(userId)){
            file.setCreatorId(userId);
            file.setUpdaterId(userId);
        }
        file.setCreateTime(new Date());
        file.setUpdateTime(new Date());
        return baseMiniModelFileRepository.save(file);
    }

    @Override
    public List<BaseMiniModelFileDTO> findByWaxberryId(String waxberryId) {
        List<Object[]> results = baseMiniModelFileRepository.findByWaxberryId(waxberryId);
        return results.stream()
                .map(row -> new BaseMiniModelFileDTO(
                        (String) row[0],  // id
                        (String) row[1],  // waxberryId
                        (String) row[2],  // fileId
                        (String) row[3],  // creatorId
                        (String) row[4],  // updaterId
                        (Date) row[5],    // createTime
                        (Date) row[6],    // updateTime
                        (String) row[7],  // filename
                        (BigDecimal) row[8]     // filesize
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFile(JSONObject json) {
        String id = json.getString("id");
        baseMiniModelFileRepository.deleteById(id);
    }
}
