package cn.wbnull.helloflow.app.controller;

import cn.wbnull.helloflow.app.dto.project.ProjectCreateRequest;
import cn.wbnull.helloflow.app.dto.project.ProjectQueryRequest;
import cn.wbnull.helloflow.app.dto.project.ProjectUpdateRequest;
import cn.wbnull.helloflow.app.dto.project.ProjectVO;
import cn.wbnull.helloflow.app.service.HfProjectService;
import cn.wbnull.helloflow.common.model.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目控制器
 *
 * @author null
 * @date 2026-05-26
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "项目管理", description = "项目CRUD、项目成员管理")
public class ProjectController extends BaseController {

    private final HfProjectService hfProjectService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("userId", "isAdmin");
    }

    @GetMapping
    @Operation(summary = "项目列表")
    public Result<Page<ProjectVO>> listProjects(ProjectQueryRequest query) {
        query.setUserId(getCurrentUserId());
        query.setAdmin(isAdmin());
        return Result.success(hfProjectService.listProjects(query));
    }

    @PostMapping
    @Operation(summary = "创建项目")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<ProjectVO> createProject(@Valid @RequestBody ProjectCreateRequest request) {
        return Result.success(hfProjectService.createProject(request, getCurrentUserId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "项目详情")
    public Result<ProjectVO> getProject(@PathVariable Long id) {
        return Result.success(hfProjectService.getProject(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新项目")
    public Result<ProjectVO> updateProject(@PathVariable Long id, @RequestBody ProjectUpdateRequest request) {
        return Result.success(hfProjectService.updateProject(id, request));
    }

    @GetMapping("/{id}/members")
    @Operation(summary = "项目成员列表")
    public Result<List<ProjectVO.MemberVO>> listMembers(
            @PathVariable Long id,
            @RequestParam(required = false) String positionCode) {
        return Result.success(hfProjectService.listMembers(id, positionCode));
    }

    @PostMapping("/{id}/members")
    @Operation(summary = "添加项目成员")
    public Result<Void> addMember(@PathVariable Long id, @RequestParam Long userId) {
        hfProjectService.addMember(id, userId);
        return Result.success();
    }

    @DeleteMapping("/{id}/members/{userId}")
    @Operation(summary = "移除项目成员")
    public Result<Void> removeMember(@PathVariable Long id, @PathVariable Long userId) {
        hfProjectService.removeMember(id, userId);
        return Result.success();
    }
}