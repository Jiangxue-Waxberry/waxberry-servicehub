package com.jiangxue.waxberry.user.controller;


import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.user.dto.UserApprovalDto;
import com.jiangxue.waxberry.user.dto.UserDto;
import com.jiangxue.waxberry.user.dto.UserProcessDto;
import com.jiangxue.waxberry.user.dto.UserStatusDto;
import com.jiangxue.waxberry.user.entity.User;
import com.jiangxue.waxberry.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "用户")
@RestController
@RequestMapping("/auth/admin/users")
public class UserAdminController {

    @Autowired
    private UserService userService;


    @Operation(summary = "新增用户")
    @PostMapping("/createUser")
    public ResponseEntity<ApiResult<User>> createUser(@RequestBody @Valid UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setPassword(userDto.getPassword());
        user.setStatus(User.UserStatus.ENABLED);
        user.setUserRole(User.UserRole.ADMIN);
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(ApiResult.success(createdUser));
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResult<Void>> deleteUser(@PathVariable String userId) {
        User user = userService.getUser(userId);
        if ("ADMIN".equals(user.getUserRole().name())) {
            return ResponseEntity.status(HttpStatus.OK).body(ApiResult.error("不能删除超级管理员"));
        }
        userService.deleteUser(user);
        return ResponseEntity.ok(ApiResult.success());
    }

    @Operation(summary = "批量删除用户")
    @DeleteMapping("/bulk")
    public ResponseEntity<ApiResult<Void>> deleteUsers(@RequestBody List<String> userIds) {
        for (String userId : userIds) {
            User user = userService.getUser(userId);
            if ("ADMIN".equals(user.getUserRole().name())) {
                continue;
            }
            userService.deleteUser(user);
        }
        return ResponseEntity.ok(ApiResult.success());
    }


    @Operation(summary = "修改用户")
    @PutMapping
    public ResponseEntity<ApiResult<User>> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(ApiResult.success(updatedUser));
    }

    @Operation(summary = "根据Id查询用户")
    @GetMapping("/getUser/{userId}")
    public ResponseEntity<ApiResult<User>> getUser(@PathVariable String userId) {
        User user = userService.getUser(userId);
        if (user != null) {
            return ResponseEntity.ok(ApiResult.success(user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResult.error("User not found"));
        }
    }

    @GetMapping("/getAllUsersPaged")
    public ResponseEntity<ApiResult<Page<User>>> getAllUsersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) User.UserStatus status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userService.findAllUsersWithCriteria(username, role, mobile, pageable, status, startDate, endDate);
        return ResponseEntity.ok(ApiResult.success(userPage));
    }


    @Operation(summary = "通过用户名查询用户")
    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResult> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(ApiResult.success(user));
        } else {
            return ResponseEntity.ok(ApiResult.success());
        }
    }

    @Operation(summary = "重置密码")
    @PostMapping("/reset-password/{userId}")
    public ResponseEntity<ApiResult<User>> resetPassword(@PathVariable String userId, @RequestBody String newPassword) {
//        String decodedPasswd = new String(Base64Util.decode(newPassword));
        User updatedUser = userService.resetPassword(userId, newPassword);
        if (updatedUser != null) {
            return ResponseEntity.ok(ApiResult.success(updatedUser));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResult.error("User not found"));
        }
    }

    @Operation(summary = "批量重置密码")
    @PostMapping("/reset-password/bulk")
    public ResponseEntity<ApiResult> resetPasswords(@RequestBody List<String> userIds) {
        List<User> updatedUsers = new ArrayList<>();
        String defaultPassword = "123456";

        for (String userId : userIds) {
            User updatedUser = userService.resetPassword(userId, defaultPassword);
            if (updatedUser != null) {
                updatedUsers.add(updatedUser);
            }
        }

        if (!updatedUsers.isEmpty()) {
            return ResponseEntity.ok(ApiResult.success());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResult.error("Users not found"));
        }
    }

    @Operation(summary = "更新用户状态")
    @PutMapping("/{userId}/status")
    public ApiResult updateUserStatus(@PathVariable("userId") String userId, @RequestBody UserStatusDto userStatusDto) {
        userService.updateStatus(userId, userStatusDto.getStatus());
        return ApiResult.success();
    }

    @Operation(summary = "获取注册用户列表")
    @GetMapping("/findProcessList")
    public ApiResult<Page<UserApprovalDto>> findProcessList(
            @Parameter(description = "审核状态") @RequestParam(name = "name",required = false) String name,
            @Parameter(description = "审核状态") @RequestParam(name = "approvalStatus",required = false,defaultValue = "PROCESS,PASS,REFUSE") String approvalStatus,
            @Parameter(description = "用户类型") @RequestParam(name = "userType",required = false,defaultValue = "PERSONAL,ENTERPRISE,COLLEGE") String userType,
            @Parameter(description = "排序") @RequestParam(name = "sort",required = false,defaultValue = "0") Integer sort,
            @Parameter(description = "排序字段") @RequestParam(name = "sortField",required = false,defaultValue = "approvalCreateTime") String sortField,
            @Parameter(description = "页码") @RequestParam(name="pageNo",required = false,defaultValue = "1") int pageNo,
            @Parameter(description = "每页大小") @RequestParam(name="pageSize",required = false,defaultValue = "5") int pageSize)
            {
        return ApiResult.success(userService.findProcessList(name,userType,approvalStatus,sort,sortField,pageNo,pageSize));
    }

    @Operation(summary = "审核注册用户")
    @PutMapping("/{userId}/process")
    public ApiResult process(@PathVariable("userId") String userId, @RequestBody UserProcessDto userProcessDto) {
        userService.process(userId, userProcessDto);
        return ApiResult.success();
    }

    @Operation(summary = "获取注册用户详情")
    @GetMapping("/findProcessById")
    public ApiResult findProcessById(
            @Parameter(description = "审核状态") @RequestParam(name = "id") String id)
    {
        return ApiResult.success(userService.findProcessById(id));
    }

}


