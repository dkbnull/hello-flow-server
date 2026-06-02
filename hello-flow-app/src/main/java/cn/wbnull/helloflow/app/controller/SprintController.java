package cn.wbnull.helloflow.app.controller;

import cn.wbnull.helloflow.app.dto.sprint.SprintCreateRequest;
import cn.wbnull.helloflow.app.dto.sprint.SprintUpdateRequest;
import cn.wbnull.helloflow.app.dto.sprint.SprintVO;
import cn.wbnull.helloflow.app.service.HfSprintService;
import cn.wbnull.helloflow.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 迭代控制器
 *
 * @author null
 * @date 2026-05-26
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Sprint管理", description = "Sprint CRUD、开始/完成Sprint")
public class SprintController {

    private final HfSprintService hfSprintService;

    @GetMapping("/projects/{projectId}/sprints")
    @Operation(summary = "Sprint列表")
    public Result<List<SprintVO>> listSprints(@PathVariable Long projectId) {
        return Result.success(hfSprintService.listSprints(projectId));
    }

    @PostMapping("/projects/{projectId}/sprints")
    @Operation(summary = "创建Sprint")
    public Result<SprintVO> createSprint(@PathVariable Long projectId,
                                         @Valid @RequestBody SprintCreateRequest request) {
        return Result.success(hfSprintService.createSprint(projectId, request));
    }

    @PutMapping("/sprints/{id}")
    @Operation(summary = "更新Sprint")
    public Result<SprintVO> updateSprint(@PathVariable Long id, @Valid @RequestBody SprintUpdateRequest request) {
        return Result.success(hfSprintService.updateSprint(id, request));
    }

    @PutMapping("/sprints/{id}/start")
    @Operation(summary = "开始Sprint")
    public Result<Void> startSprint(@PathVariable Long id) {
        hfSprintService.startSprint(id);
        return Result.success();
    }

    @PutMapping("/sprints/{id}/complete")
    @Operation(summary = "完成Sprint")
    public Result<Void> completeSprint(@PathVariable Long id) {
        hfSprintService.completeSprint(id);
        return Result.success();
    }
}