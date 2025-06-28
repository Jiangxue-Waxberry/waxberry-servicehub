package com.jiangxue.waxberry.manager.agent.service.impl;


import com.jiangxue.framework.common.enums.TemplateEnum;
import com.jiangxue.framework.common.exception.BizException;
import com.jiangxue.framework.common.util.SmsUtils;
import com.jiangxue.waxberry.manager.agent.dto.AgentApprovalDTO;
import com.jiangxue.waxberry.manager.agent.entity.Agent;
import com.jiangxue.waxberry.manager.agent.entity.AgentApproval;
import com.jiangxue.waxberry.manager.agent.repository.AgentApprovalRepository;
import com.jiangxue.waxberry.manager.agent.repository.AgentRepository;
import com.jiangxue.waxberry.manager.agent.service.AgentApprovalService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AgentApprovalServiceImpl implements AgentApprovalService {

    @Value("${manage.ALIBABA_CLOUD_ACCESS_KEY_SECRET}")
    private String accessKeySecret;

    @Value("${manage.ALIBABA_CLOUD_ACCESS_KEY_ID}")
    private String accessKeyId;

    @Autowired
    private AgentApprovalRepository approvalRepository;

    @Autowired
    private AgentRepository agentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<AgentApprovalDTO> findAgentApprovalList(String status, int pageNo, int pageSize,Integer sort,String sortField,String userType,String agentType,String name){
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("status",status);
        queryParams.put("userType",userType);
        queryParams.put("agentType",agentType);
        StringBuilder dataSQL = new StringBuilder("select ml.id,ml.approval_code,au.loginname,au.mobile,au.user_role as userType,ml.agent_name,ma.type,ma.discription, ");
        dataSQL.append(" CONCAT(mf1.name, '>', mf2.name) AS classificationName,ma.ismodify,ma.create_time as agentCreateTime,ml.create_time approvalCreateTime,");
        dataSQL.append(" CASE WHEN ml.status = 'PROCESS' THEN TIMESTAMPDIFF(HOUR, ml.create_time, NOW()) ELSE 0 END as hour,ml.status,ma.id as agentId ");
        dataSQL.append(" from mgr_agent_approval ml ");
        dataSQL.append("  left join auth_users au on ml.creator = au.id ");
        dataSQL.append("  left join mgr_agent_agent ma on ml.agent_Id = ma.id ");
        dataSQL.append("  left join mgr_agent_classification mf1 on mf1.id = SUBSTRING_INDEX(ma.group_id, '-', 1) ");
        dataSQL.append("  left join mgr_agent_classification mf2 on mf2.id = SUBSTRING_INDEX(ma.group_id, '-', -1) ");
        dataSQL.append("  where ml.status in(:status) and au.user_role in (:userType) and ma.type in (:agentType)");
        if(!ObjectUtils.isEmpty(name)){
            dataSQL.append("  and ml.agent_name like :name ");
        }
        dataSQL.append(" order by ").append(sortField).append(" ").append(sort==0?"asc":"desc");
        Query query = entityManager.createNativeQuery(dataSQL.toString());
        if(!ObjectUtils.isEmpty(name)){
            query.setParameter("name","%" + name + "%");
        }

        for (Map.Entry<String, String> param : queryParams.entrySet()) {
            query.setParameter(param.getKey(), Arrays.stream(param.getValue().split(","))
                    .collect(Collectors.toList()));
        }

        // 计算总记录数
        Long totalCount = countTotal(dataSQL.toString(),name,queryParams);
        

        // 应用分页
        query.setFirstResult((pageNo - 1) * pageSize);
        query.setMaxResults(pageSize);

        // 执行查询
        List<Object[]> results = query.getResultList();

        // 转换结果为DTO列表
        List<AgentApprovalDTO> dtoList = convertToDTOList(results);

        // 创建分页对象
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    @Override
    public AgentApproval addAgentApproval(AgentApproval approval) {
        return approvalRepository.save(approval);
    }


    @Override
    public void updateAgentApproval(String id, String status, String approvalLanguage) {
        Map<String, String> templateParam = new HashMap<>();
        for (String s : id.split(",")) {
            Optional<AgentApproval> optional = approvalRepository.findById(s);
            if(optional.isPresent()){
                AgentApproval approval = optional.get();
                Optional<Agent> angetOptional =agentRepository.findById(approval.getAgentId());
                Integer agentStatus = 0;

                if(status.equals("PASS")){
                    approval.setStatus(AgentApproval.Status.PASS);
                    agentStatus = 2;
                    templateParam.put("user_nick",approval.getAgentName());
                    sendSms(approval.getMobile(), TemplateEnum.NATTOPASS.getValue(),templateParam);
                }else if(status.equals("REFUSE")){
                    approval.setStatus(AgentApproval.Status.REFUSE);
                    templateParam.put("user_nick",approval.getAgentName());
                    templateParam.put("cause",approvalLanguage);
                    sendSms(approval.getMobile(), TemplateEnum.NATTOREFUSE.getValue(),templateParam);
                }
                if(angetOptional.isPresent()){
                    angetOptional.get().setStatus(agentStatus);
                    agentRepository.save(angetOptional.get());
                }
                approvalRepository.save(approval);
            }else{
                throw new BizException("方法错误");
            }
        }
    }

    private Long countTotal(String sql,String name,Map<String,String> queryParams) {
        String countSql = "SELECT COUNT(1) FROM (" + sql + ") as subquery";

        Query countQuery = entityManager.createNativeQuery(countSql);
        if(!ObjectUtils.isEmpty(name)){
            countQuery.setParameter("name","%" + name + "%");
        }
        for (Map.Entry<String, String> param : queryParams.entrySet()) {
            countQuery.setParameter(param.getKey(), Arrays.stream(param.getValue().split(","))
                    .collect(Collectors.toList()));
        }

        return ((Number) countQuery.getSingleResult()).longValue();
    }

    private List<AgentApprovalDTO> convertToDTOList(List<Object[]> results) {
        List<AgentApprovalDTO> dtoList = new ArrayList<>();

        for (Object[] row : results) {
            AgentApprovalDTO dto = new AgentApprovalDTO();
            dto.setId(convertToString(row[0]));
            dto.setApprovalCode(convertToString(row[1]));
            dto.setUsername(convertToString(row[2]));
            dto.setMobile(convertToString(row[3]));
            dto.setUserType(convertToString(row[4]));
            dto.setAgentName(convertToString(row[5]));
            dto.setAgentType(Integer.valueOf(row[6].toString()));
            dto.setAgentDiscription(convertToString(row[7]));
            dto.setAgentClassification(convertToString(row[8]));
            dto.setAgentIsmodify(row[9]!= null ? Integer.valueOf(row[9].toString()): null);
            Timestamp ts1 = (Timestamp) row[10];
            dto.setAgentCreateTime(new Date(ts1.getTime()));
            Timestamp ts2 = (Timestamp) row[11];
            dto.setApprovalCreateTime(new Date(ts2.getTime()));
            dto.setHour(Integer.valueOf(convertToString(row[12])));
            dto.setApprovalStatus(convertToString(row[13]));
            dto.setAgentId(convertToString(row[14]));
            dtoList.add(dto);
        }
        return dtoList;
    }

    private String convertToString(Object value) {
        return value != null ? value.toString() : null;
    }

    public void sendSms(String mobile, String templateType, Map<String, String> templateParam) {
        SmsUtils.init(accessKeyId,accessKeySecret);
        SmsUtils.sendTemplateMessage(mobile,templateType,templateParam);
    }
}
