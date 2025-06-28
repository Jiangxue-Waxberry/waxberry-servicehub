package com.jiangxue.waxberry.manager.baseMiniMode.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.waxberry.manager.agent.constant.SandboxesURLConstant;
import com.jiangxue.waxberry.manager.agent.entity.Agent;
import com.jiangxue.waxberry.manager.agent.service.AgentService;
import com.jiangxue.waxberry.manager.agent.util.AgentFileUploadUtil;
import com.jiangxue.waxberry.manager.baseMiniMode.entity.BaseMiniModelParamConfig;
import com.jiangxue.waxberry.manager.baseMiniMode.repository.BaseMiniModelParamConfigRepository;
import com.jiangxue.waxberry.manager.baseMiniMode.service.BaseMiniModelParamConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BaseMiniModelParamConfigServiceImpl implements BaseMiniModelParamConfigService {

    @Autowired
    private BaseMiniModelParamConfigRepository baseMiniModelParamConfigRepository;

    @Autowired
    private AgentService agentMarketService;

    @Override
    public List<BaseMiniModelParamConfig> saveAll(List<BaseMiniModelParamConfig> configs) {
        if (configs == null || configs.isEmpty()) {
            return Collections.emptyList();
        }

        //如果存在需要保存的数据，先基于杨梅id进行删除以前数据，在进行保存新数据
        String waxberryId = configs.get(0).getWaxberryId();
        List<BaseMiniModelParamConfig> waxberryList = baseMiniModelParamConfigRepository.findByWaxberryId(waxberryId);
        if (!CollectionUtils.isEmpty(waxberryList)) {
            //存在数据
            baseMiniModelParamConfigRepository.deleteInBatch(waxberryList);
        }
        String userId =SecurityUtils.requireCurrentUserId();
        for (BaseMiniModelParamConfig config : configs) {
            if (userId != null) {
                config.setCreatorId(userId);
                config.setUpdaterId(userId);
            }
            config.setCreateTime(new Date());
            config.setUpdateTime(new Date());
        }

        //TODO 删除config.yaml,上传最新得config.yaml
        updateBaseMiniModelParamConfigYaml(waxberryId,configs);

        return baseMiniModelParamConfigRepository.saveAll(configs);
    }

    @Override
    public List<BaseMiniModelParamConfig> findById(String waxberryId) {
        return baseMiniModelParamConfigRepository.findByWaxberryId(waxberryId);
    }

    @Override
    public List<BaseMiniModelParamConfig> findAll() {
        return baseMiniModelParamConfigRepository.findAll();
    }

    @Override
    public Page<BaseMiniModelParamConfig> findAll(Pageable pageable) {
        return baseMiniModelParamConfigRepository.findAll(pageable);
    }

    private void updateBaseMiniModelParamConfigYaml(String waxberryId,List<BaseMiniModelParamConfig> configs){
        try {
            String path = "/waxberry/chathistory/config.yaml";
            Optional<Agent> optionalMarket = agentMarketService.findById(waxberryId);
            if (optionalMarket.isPresent()) {
                Agent agent = optionalMarket.get();
                String delUrl = System.getProperty("sandBoxUrl") + "/sandboxes/" + agent.getVesselId() + "/files?path=" + path + "&recursive=true";
                URL url = new URL(delUrl);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("DELETE");
                httpConn.connect();

                // 获取响应码
                int responseCode = httpConn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    File file = generateYamlFile(configs, new File("config.yaml"));
                    JSONObject json = new JSONObject();
                    json.put("path", path);
                    AgentFileUploadUtil.uploadFile(
                            System.getProperty("sandBoxUrl").concat(String.format(SandboxesURLConstant.AGENT_UPLOAD_FILE_URL, agent.getVesselId())),
                            file,
                            json);
                    file.delete();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private File generateYamlFile(List<BaseMiniModelParamConfig> objects, File outputFile) throws Exception {
        try (FileWriter writer = new FileWriter(outputFile)) {
            String format = "%s:%s           #%s";
            for (BaseMiniModelParamConfig obj : objects) {
                String defaultValue = obj.getDefaultValue() != null ? obj.getDefaultValue() : "";
                String line = String.format(format,
                        obj.getName(),
                        defaultValue,
                        obj.getDescription());
                writer.write(line + "\n");
            }
        }
        return outputFile;
    }

}
