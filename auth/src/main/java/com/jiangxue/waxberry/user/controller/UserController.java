package com.jiangxue.waxberry.user.controller;

import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.auth.service.RedisService;
import com.jiangxue.waxberry.user.dto.UserDto;
import com.jiangxue.waxberry.user.dto.UserProfileDto;
import com.jiangxue.waxberry.user.dto.UserRegisterDto;
import com.jiangxue.waxberry.user.entity.User;
import com.jiangxue.waxberry.user.entity.UserProfile;
import com.jiangxue.waxberry.auth.service.AvatarService;
import com.jiangxue.waxberry.user.enums.YccNumEnum;
import com.jiangxue.waxberry.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@Tag(name = "用户管理", description = "用户相关接口，包括用户信息管理、头像管理等")
@RestController
@RequestMapping("/auth/users")
@Slf4j
public class UserController {

    private static final String ServerName = "auth:";

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Operation(summary = "获取当前用户信息",
            description = "获取当前登录用户的详细信息")
    @ApiResponse(responseCode = "200", description = "成功获取用户信息",
            content = @Content(schema = @Schema(implementation = User.class)))
    @GetMapping("/me")
    public ResponseEntity<ApiResult<UserRegisterDto>> getMyInfo() {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByUsername(authentication.getName());
        UserProfile userProfile = userService.getUserProfilesByUserId(currentUser.getId());
        BeanUtils.copyProperties(userProfile,userRegisterDto);
        BeanUtils.copyProperties(currentUser,userRegisterDto);
        userRegisterDto.setPassword("");
        userRegisterDto.setUserRole(currentUser.getUserRole().name());
        return ResponseEntity.ok(ApiResult.success(userRegisterDto));
    }

    @Operation(summary = "更新当前用户信息",
            description = "更新当前登录用户的个人信息")
    @ApiResponse(responseCode = "200", description = "成功更新用户信息",
            content = @Content(schema = @Schema(implementation = User.class)))
    @PutMapping("/me")
    public ResponseEntity<ApiResult<UserRegisterDto>> updateMyInfo(@RequestBody UserRegisterDto userRegisterDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserRegisterDto updatedUser = userService.updateUserByUsername(username, userRegisterDto);
        return ResponseEntity.ok(ApiResult.success(updatedUser));
    }

    @Operation(summary = "重置当前用户密码",
            description = "修改当前登录用户的密码")
    @Parameter(name = "newPassword", description = "新密码", required = true)
    @ApiResponse(responseCode = "200", description = "成功重置密码")
    @ApiResponse(responseCode = "400", description = "密码重置失败")
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResult<Void>> resetPassword(@RequestBody UserRegisterDto userRegisterDto) {
        Object captcha = redisService.get(userRegisterDto.getMobile());
        if (!ObjectUtils.isEmpty(captcha) && captcha.equals(userRegisterDto.getCode())) {
            userService.resetPasswordByUsername(userRegisterDto.getUsername(), userRegisterDto.getPassword());
        }
        return ResponseEntity.ok(ApiResult.success());
    }

    @Operation(summary = "重置当前用户密码",
            description = "修改当前登录用户的密码")
    @Parameter(name = "newPassword", description = "新密码", required = true)
    @ApiResponse(responseCode = "200", description = "成功重置密码")
    @ApiResponse(responseCode = "400", description = "密码重置失败")
    @PostMapping("/me/reset-password")
    public ResponseEntity<ApiResult<Void>> resetMyPassword(@RequestBody String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        userService.resetPasswordByUsername(username, newPassword);
        return ResponseEntity.ok(ApiResult.success());
    }

    @Operation(summary = "用户注册",
            description = "创建新用户账号")
    @ApiResponse(responseCode = "200", description = "注册成功",
            content = @Content(schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "400", description = "注册失败")
    @PostMapping("/register")
    public ApiResult register(@RequestBody @Valid UserRegisterDto registerDTO) {
        Object captcha = redisService.get(ServerName + "register:" + registerDTO.getMobile());
        if (!ObjectUtils.isEmpty(captcha) && captcha.equals(registerDTO.getCode())) {
            userService.checkUser(registerDTO);
            userService.registerUser(registerDTO);
            redisService.delete(ServerName + "register:" + registerDTO.getMobile());
            return ApiResult.success();
        } else {
            return ApiResult.error("验证码错误或已过期");
        }
    }

    @Operation(summary = "更新用户档案",
            description = "更新当前登录用户的档案信息")
    @ApiResponse(responseCode = "200", description = "成功更新用户档案",
            content = @Content(schema = @Schema(implementation = UserProfile.class)))
    @PutMapping("/me/profile")
    public ResponseEntity<ApiResult<UserProfile>> updateMyProfile(@RequestBody UserProfileDto profileDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String userId = userService.getUserByUsername(username).getId();
        UserProfile updatedProfile = userService.updateUserProfile(userId, profileDTO);
        return ResponseEntity.ok(ApiResult.success(updatedProfile));
    }
}
