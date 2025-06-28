package com.jiangxue.waxberry.user.service;

import com.jiangxue.framework.common.enums.TemplateEnum;
import com.jiangxue.framework.common.exception.BizException;
import com.jiangxue.framework.common.util.CopyProperties;
import com.jiangxue.framework.common.util.SmsUtils;
import com.jiangxue.waxberry.auth.service.RedisService;
import com.jiangxue.waxberry.auth.utils.RSAUtil;
import com.jiangxue.waxberry.user.dto.UserApprovalDto;
import com.jiangxue.waxberry.user.dto.UserProcessDto;
import com.jiangxue.waxberry.user.dto.UserProfileDto;
import com.jiangxue.waxberry.user.dto.UserRegisterDto;
import com.jiangxue.waxberry.user.entity.User;
import com.jiangxue.waxberry.user.entity.UserProcess;
import com.jiangxue.waxberry.user.entity.UserProfile;
import com.jiangxue.waxberry.user.enums.YccNumEnum;
import com.jiangxue.waxberry.user.exception.UserNotFoundException;
import com.jiangxue.waxberry.user.repository.UserProcessRepository;
import com.jiangxue.waxberry.user.repository.UserProfileRepository;
import com.jiangxue.waxberry.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.security.PrivateKey;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserProcessRepository userProcessRepository;

    @Autowired
    private RedisService redisService;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${auth.ALIBABA_CLOUD_ACCESS_KEY_SECRET}")
    private String accessKeySecret;

    @Value("${auth.ALIBABA_CLOUD_ACCESS_KEY_ID}")
    private String accessKeyId;

    @Value("${auth.privateKey}")
    private String PRIVATE_KEY_PEM;


    // 新增用户
    @Transactional
    public User createUser(User user) {
        userRepository.findByUsername(user.getUsername()).ifPresent(existingUser -> {
            throw new BizException("User already exists with username: " + existingUser.getUsername());
        });
        userRepository.findByMobile(user.getMobile()).ifPresent(existingUser -> {
            throw new BizException("User already exists with username: " + existingUser.getUsername());
        });
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        return userRepository.save(user);
    }

    // 删除用户
    public void deleteUser(User user) {
        userRepository.deleteById(user.getId());
        userProcessRepository.deleteByMobile(user.getMobile());
    }

    // 修改用户
    public User updateUser(User user) {
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setMobile(user.getMobile());
            existingUser.setLoginname(user.getLoginname());  // Add this line
            UserProcess userProcess = userProcessRepository.findByMobile(existingUser.getMobile()).get();
            userProcess.setMobile(user.getMobile());
            return userRepository.save(existingUser);
        } else {
            throw new UserNotFoundException("User not found with id: " + user.getId());
        }
    }

    @Transactional
    public void updateStatus(String id, User.UserStatus status) {
        userRepository.findById(id).ifPresent(user -> {
            user.setStatus(status);
            userRepository.save(user);
        });
    }

    // 查询用户
    public User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    // 重置密码
    public User resetPassword(String userId, String newPassword) {
        User user = getUser(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    // 获取用户信息通过用户名
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // 获取用户信息通过用户名
    public UserProfile getUserProfilesByUserId(String userId) {
        return userProfileRepository.findByUserId(userId).orElse(null);
    }

    // 修改用户信息通过用户名
    @Transactional
    public UserRegisterDto updateUserByUsername(String username, UserRegisterDto userRegisterDto) {
        UserRegisterDto newUserRegisterDto = new UserRegisterDto();
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserProfile existingProfile = userProfileRepository.findByUserId(existingUser.getId())
                .orElseThrow(() -> new UserNotFoundException("userProfile not found"));
        Optional<UserProcess> optionalUserProcess = userProcessRepository.findByMobile(existingUser.getMobile());
        if (optionalUserProcess.isPresent()) {
            if (!ObjectUtils.isEmpty(userRegisterDto.getMobile())) {
                UserProcess userProcess = optionalUserProcess.get();
                userProcess.setMobile(userRegisterDto.getMobile());
                userProcessRepository.save(userProcess);
            }
        }
        userRegisterDto.setCreateNum(existingUser.getCreateNum());
        CopyProperties.copyNonNullProperties(userRegisterDto,existingUser);
        if (existingProfile != null) {
            CopyProperties.copyNonNullProperties(userRegisterDto,existingProfile);
            UserProfile updateUserProfile = userProfileRepository.save(existingProfile);
            BeanUtils.copyProperties(updateUserProfile,newUserRegisterDto);
        }
        User updateUser = userRepository.save(existingUser);
        BeanUtils.copyProperties(updateUser,newUserRegisterDto);
        newUserRegisterDto.setPassword("");
        return newUserRegisterDto;
    }

    @Transactional
    // 修改密码通过用户名
    public void resetPasswordByUsername(String username, String newPassword) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<User> findAllUsersWithCriteria(String username, String role, String mobile, Pageable pageable, User.UserStatus status, LocalDate startDate, LocalDate endDate) {
        Specification<User> spec = UserSpecification.compositeSpec(username, role, mobile, status, startDate, endDate);
        return userRepository.findAll(spec, pageable);
    }

    @Transactional
    public User registerUser(@Valid UserRegisterDto registerDTO) {
        User user = userRepository.findByMobile(registerDTO.getMobile()).orElse(new User());
        user.setUsername(registerDTO.getMobile());
        user.setMobile(registerDTO.getMobile());
        user.setStatus(User.UserStatus.DISABLED);
        user.setUserRole(User.UserRole.valueOf(registerDTO.getUserRole()));
        if (User.UserRole.ENTERPRISE.name().equals(registerDTO.getUserRole())) {
            user.setLoginname(registerDTO.getCompanyAdmin());
        } else {
            user.setLoginname(registerDTO.getLoginname());
        }
        user.setCreateNum(YccNumEnum.getValueByName(registerDTO.getUserRole()));
        user.setEmail(registerDTO.getEmail());
        String password = registerDTO.getPassword();
        try {
            PrivateKey privateKey = RSAUtil.getPrivateKey(PRIVATE_KEY_PEM);
            password = RSAUtil.decrypt(password, privateKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(false);
        UserProcess userProcess = new UserProcess();
        userProcess.setMobile(user.getMobile());
        userProcess.setApprovalNumber(redisService.generateCode("auth","RA"));
        userProcess.setStatus(UserProcess.ProcessStatus.PROCESS);
        userProcessRepository.save(userProcess);
        User newUser = userRepository.save(user);
        registerUserProfiles(newUser.getId(),registerDTO);
        return newUser;
    }

    @Transactional
    public UserProfile updateUserProfile(String userId, UserProfileDto profileDTO) {
        UserProfile existingProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("userProfile not found"));

        //existingProfile.setGender(UserProfile.Gender.valueOf(profileDTO.getGender()));
        existingProfile.setSchool(profileDTO.getSchool());
        existingProfile.setCollege(profileDTO.getCollege());
        existingProfile.setUscc(profileDTO.getUscc());
        existingProfile.setMajor(profileDTO.getMajor());
        existingProfile.setWorkNum(profileDTO.getWorkNum());
        existingProfile.setAvatarUrl(profileDTO.getAvatarUrl());
        existingProfile.setCompanyName(profileDTO.getCompanyName());
        existingProfile.setCompanyAdmin(profileDTO.getCompanyAdmin());
        existingProfile.setIndustry(profileDTO.getIndustry());
        existingProfile.setTitle(profileDTO.getTitle());
        existingProfile.setBio(profileDTO.getBio());
        
        return userProfileRepository.save(existingProfile);
    }

    public UserProfile getUserProfile(String userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User profile not found for user: " + userId));
    }

    public String exchangeRedemption() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User profile not found for user: " + authentication.getName()));
        user.setCreateNum(user.getCreateNum() + 1);
        userRepository.save(user);
        return user.getId();
    }


    public UserRegisterDto findProcessById(String id){
        Optional<UserProcess> optional = userProcessRepository.findById(id);
        if(optional.isPresent()){
            UserProcess userProcess = optional.get();
            UserRegisterDto userRegisterDto = new UserRegisterDto();
            BeanUtils.copyProperties(userProcess,userRegisterDto);
            Optional<User> optionalUser = userRepository.findByMobile(userProcess.getMobile());
            User user = optionalUser.get();
            BeanUtils.copyProperties(user,userRegisterDto);
            Optional<UserProfile> optionalUserProfile = userProfileRepository.findByUserId(user.getId());
            UserProfile userProfile = optionalUserProfile.get();
            BeanUtils.copyProperties(userProfile,userRegisterDto);
            userRegisterDto.setUserRole(user.getUserRole().name());
            return userRegisterDto;
        }
        throw new BizException("方法异常");
    }


    public Page<UserApprovalDto> findProcessList(String name,String userType,String approvalStatus,Integer sort,String sortField,int pageNo,int pageSize) {
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("userType",userType);
        queryParams.put("status",approvalStatus);
        StringBuilder dataSQL = new StringBuilder("select aup.id,aup.approval_number,au.loginname,aup.mobile,");
        dataSQL.append(" au.user_role,aup.created_at as approvalCreateTime,aup.status as approvalStatus,");
        dataSQL.append(" CASE WHEN aup.status = 'PROCESS' THEN TIMESTAMPDIFF(HOUR, aup.created_at, NOW()) ELSE 0 END as hour,au.id as userId ");
        dataSQL.append(" from auth_user_process aup ");
        dataSQL.append(" left join auth_users au on au.mobile = aup.mobile");
        dataSQL.append(" where aup.status in(:status) and au.user_role in (:userType)");
        if(!ObjectUtils.isEmpty(name)){
            dataSQL.append("  and au.loginname like :name ");
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
        List<UserApprovalDto> dtoList = convertToDTOList(results);

        // 创建分页对象
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return new PageImpl<>(dtoList, pageable, totalCount);
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

    private List<UserApprovalDto> convertToDTOList(List<Object[]> results) {
        List<UserApprovalDto> dtoList = new ArrayList<>();

        for (Object[] row : results) {
            UserApprovalDto dto = new UserApprovalDto();
            dto.setId(convertToString(row[0]));
            dto.setApprovalCode(convertToString(row[1]));
            dto.setUsername(convertToString(row[2]));
            dto.setMobile(convertToString(row[3]));
            dto.setUserType(convertToString(row[4]));
            Timestamp ts = (Timestamp) row[5];
            dto.setApprovalCreateTime(new Date(ts.getTime()));
            dto.setApprovalStatus(convertToString(row[6]));
            dto.setHour(Integer.valueOf(convertToString(row[7])));
            dto.setUserId(convertToString(row[8]));
            dtoList.add(dto);
        }
        return dtoList;
    }

    private String convertToString(Object value) {
        return value != null ? value.toString() : null;
    }


    public void process(String userId, UserProcessDto userProcessDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User profile not found for user: " + userId));
        UserProcess userProcess = userProcessRepository.findByMobile(user.getMobile()).get();
        Map<String,String> templateParam = new HashMap<>();
        String templateType;
        if (!UserProcess.ProcessStatus.PROCESS.name().equals(userProcessDto.getStatus().name())) {
            if (UserProcess.ProcessStatus.PASS.name().equals(userProcessDto.getStatus().name())) {
                templateType = TemplateEnum.PASS.getValue();
                userProcess.setStatus(UserProcess.ProcessStatus.PASS);
                user.setStatus(User.UserStatus.ENABLED);
                user.setEnabled(true);
                userRepository.save(user);
            } else {
                userProcess.setStatus(UserProcess.ProcessStatus.REFUSE);
                templateParam.put("message","拒绝通过");
                templateType = TemplateEnum.REFUSE.getValue();
            }
            userProcess.setApprovalLanguage(userProcessDto.getApprovalLanguage());
            userProcess.setAdminUser(authentication.getName());
            userProcessRepository.save(userProcess);
            SmsUtils.init(accessKeyId,accessKeySecret);
            SmsUtils.sendTemplateMessage(user.getMobile(),templateType,templateParam);
        } else {
            throw new BizException("该用户已审核!");
        }
    }


    public void registerUserProfiles(String userId, UserRegisterDto registerDTO) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElse(new UserProfile());
        userProfile.setUserId(userId);
        userProfile.setSchool(registerDTO.getSchool());
        userProfile.setCollege(registerDTO.getCollege());
        userProfile.setUscc(registerDTO.getUscc());
        userProfile.setMajor(registerDTO.getMajor());
        userProfile.setWorkNum(registerDTO.getWorkNum());
        userProfile.setAvatarUrl(registerDTO.getAvatarUrl());
        userProfile.setCompanyName(registerDTO.getCompanyName());
        userProfile.setCompanyAdmin(registerDTO.getCompanyAdmin());
        userProfile.setIndustry(registerDTO.getIndustry());
        userProfile.setTitle(registerDTO.getTitle());
        userProfile.setBio(registerDTO.getBio());
        userProfileRepository.save(userProfile);
    }

    public void checkUser(@Valid UserRegisterDto registerDTO) {
        Optional<UserProcess> optionalUserProcess = userProcessRepository.findByMobile(registerDTO.getMobile());
        if (optionalUserProcess.isPresent()) {
            UserProcess userProcess = optionalUserProcess.get();
            if (!UserProcess.ProcessStatus.REFUSE.name().equals(userProcess.getStatus().name())) {
                if (UserProcess.ProcessStatus.PASS.name().equals(userProcess.getStatus().name())) {
                    throw new BizException("该用户已注册!");
                } else if (UserProcess.ProcessStatus.PROCESS.name().equals(userProcess.getStatus().name())) {
                    throw new BizException("用户正在审核中，请耐心等待!");
                }
            } else {
                User user = userRepository.findByMobile(registerDTO.getMobile()).get();
                boolean isEmailExists = userRepository.findCheckUserEmail(registerDTO.getMobile(),registerDTO.getEmail()) > 0;
                if (isEmailExists && !registerDTO.getEmail().equals(user.getEmail())) {
                    throw new BizException("该邮箱已注册!");
                }
                boolean isLoginnameExists = userRepository.findCheckUserLoginName(registerDTO.getMobile(),registerDTO.getLoginname()) > 0;
                if (isLoginnameExists && !registerDTO.getLoginname().equals(user.getLoginname())) {
                    throw new BizException("用户名已存在!");
                }

            }
        } else {
            checkUserInformation(registerDTO);
        }
    }

    public void checkUserInformation(UserRegisterDto registerDTO) {
        boolean isEmailExists = userRepository.findCheckUserEmail(registerDTO.getMobile(),registerDTO.getEmail()) > 0;
        if (isEmailExists) {
            throw new BizException("该邮箱已注册!");
        }
        boolean isLoginnameExists = userRepository.findCheckUserLoginName(registerDTO.getMobile(),registerDTO.getLoginname()) > 0;
        if (isLoginnameExists) {
            throw new BizException("用户名已存在!");
        }
    }
}

