package cn.wbnull.helloflow.app.controller;

import cn.wbnull.helloflow.app.dto.project.ProjectStatsVO;
import cn.wbnull.helloflow.app.dto.stats.BoardVO;
import cn.wbnull.helloflow.app.dto.stats.BurndownVO;
import cn.wbnull.helloflow.app.dto.stats.DefectStatsVO;
import cn.wbnull.helloflow.app.dto.stats.MemberWorkloadVO;
import cn.wbnull.helloflow.app.service.BoardStatsService;
import cn.wbnull.helloflow.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 看板与统计控制器
 *
 * @author null
 * @date 2026-05-26
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "看板与统计", description = "看板数据、项目统计、燃尽图")
public class BoardStatsController {

    private final BoardStatsService boardStatsService;

    @GetMapping("/projects/{projectId}/board")
    @Operation(summary = "项目看板数据")
    public Result<BoardVO> getProjectBoard(@PathVariable Long projectId) {
        return Result.success(boardStatsService.getProjectBoard(projectId));
    }

    @GetMapping("/sprints/{sprintId}/board")
    @Operation(summary = "Sprint看板数据")
    public Result<BoardVO> getSprintBoard(@PathVariable Long sprintId) {
        return Result.success(boardStatsService.getSprintBoard(sprintId));
    }

    @GetMapping("/projects/{projectId}/stats/overview")
    @Operation(summary = "项目概览统计")
    public Result<ProjectStatsVO> getProjectStats(@PathVariable Long projectId) {
        return Result.success(boardStatsService.getProjectStats(projectId));
    }

    @GetMapping("/projects/{projectId}/stats/burndown")
    @Operation(summary = "燃尽图数据")
    public Result<BurndownVO> getBurndown(@PathVariable Long projectId, @RequestParam Long sprintId) {
        return Result.success(boardStatsService.getBurndown(projectId, sprintId));
    }

    @GetMapping("/projects/{projectId}/stats/members")
    @Operation(summary = "成员工作量统计")
    public Result<List<MemberWorkloadVO>> getMemberWorkload(@PathVariable Long projectId) {
        return Result.success(boardStatsService.getMemberWorkload(projectId));
    }

    @GetMapping("/projects/{projectId}/stats/defects")
    @Operation(summary = "缺陷统计")
    public Result<DefectStatsVO> getDefectStats(@PathVariable Long projectId) {
        return Result.success(boardStatsService.getDefectStats(projectId));
    }
}
