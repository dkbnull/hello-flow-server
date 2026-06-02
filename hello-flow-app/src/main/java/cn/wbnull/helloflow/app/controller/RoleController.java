package cn.wbnull.helloflow.app.controller;

import cn.wbnull.helloflow.app.dto.role.RoleCreateRequest;
import cn.wbnull.helloflow.app.dto.role.RoleUpdateRequest;
import cn.wbnull.helloflow.app.service.SysRoleService;
import cn.wbnull.helloflow.common.model.Result;
import cn.wbnull.helloflow.data.entity.SysRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 *
 * @author null
 * @date 2026-05-26
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色CRUD")
public class RoleController {

    private final SysRoleService sysRoleService;

    @GetMapping
    @Operation(summary = "角色列表")
    public Result<List<SysRole>> listRoles() {
        return Result.success(sysRoleService.listRoles());
    }

    @PostMapping
    @Operation(summary = "创建角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<SysRole> createRole(@Valid @RequestBody RoleCreateRequest request) {
        return Result.success(sysRoleService.createRole(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<SysRole> updateRole(@PathVariable Long id, @Valid @RequestBody RoleUpdateRequest request) {
        return Result.success(sysRoleService.updateRole(id, request));
    }
}
