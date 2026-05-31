package cn.wbnull.helloflow.app.controller;

import cn.wbnull.helloflow.app.dto.user.*;
import cn.wbnull.helloflow.app.service.SysUserService;
import cn.wbnull.helloflow.common.model.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author null
 * @date 2026-05-26
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户CRUD、个人信息管理")
public class UserController extends BaseController {

    private final SysUserService sysUserService;

    @GetMapping
    @Operation(summary = "用户列表")
//    @PreAuthorize("hasRole('ADMIN')")
    public Result<Page<UserVO>> listUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long positionId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.success(sysUserService.listUsers(keyword, status, positionId, page, pageSize));
    }

    @PostMapping
    @Operation(summary = "创建用户")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<UserVO> createUser(@Valid @RequestBody UserCreateRequest request) {
        return Result.success(sysUserService.createUser(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<UserVO> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        return Result.success(sysUserService.updateUser(id, request));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "启用/禁用用户")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateUserStatus(@PathVariable Long id, @RequestBody UserStatusRequest request) {
        sysUserService.updateUserStatus(id, request);
        return Result.success();
    }

    @GetMapping("/me")
    @Operation(summary = "当前用户信息")
    public Result<UserVO> getCurrentUser() {
        return Result.success(sysUserService.getCurrentUser(getCurrentUserId()));
    }

    @PutMapping("/me")
    @Operation(summary = "更新个人信息")
    public Result<UserVO> updateProfile(@RequestBody UserProfileUpdateRequest request) {
        return Result.success(sysUserService.updateProfile(getCurrentUserId(), request));
    }
}