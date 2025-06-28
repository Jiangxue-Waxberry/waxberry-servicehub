package com.jiangxue.waxberry.manager.attachment.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.exception.BizException;
import com.jiangxue.waxberry.manager.attachment.service.AttachmentService;
import com.jiangxue.waxberry.manager.util.HttpClientUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;


@Slf4j
@Service
public class AttachmentServiceImpl implements AttachmentService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Map<String, Object> getFileInfos(JSONObject json) {

        if(Objects.isNull(json)){
            throw new BizException("参数不能为空");
        }

        if(Objects.isNull(json.getString("fileIds"))){
            throw new BizException("文件id不能为空");
        }

        String fileIds = json.getString("fileIds");
        String[] ids = fileIds.split(",");

        // 使用 EntityManager 执行原生 SQL 查询
        String sql = "SELECT * FROM FILE_FILEDESC WHERE PID IN (" + String.join(",", Collections.nCopies(ids.length, "?")) + ")";
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            Query query = entityManager.createNativeQuery(sql);
            for (int i = 0; i < ids.length; i++) {
                query.setParameter(i + 1, ids[i]);
            }

            List<Object[]> results = query.getResultList();
            for (Object[] row : results) {
                Map<String, Object> fileMap = new HashMap<>();
                // 假设 FILE_FILEDESC 表有 id 和 name 字段
                fileMap.put("id", row[0]);
                fileMap.put("fileName", row[1]);
                fileMap.put("suffixName", row[2]);
                fileMap.put("fileSize", row[3]);
                // 添加其他字段
                resultList.add(fileMap);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 处理异常
        }

        return Collections.singletonMap("fileInfos", resultList);
    }
}
