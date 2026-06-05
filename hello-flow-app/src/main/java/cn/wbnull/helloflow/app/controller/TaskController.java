package cn.wbnull.helloflow.app.controller;

import cn.wbnull.helloflow.app.dto.comment.CommentCreateRequest;
import cn.wbnull.helloflow.app.dto.comment.CommentVO;
import cn.wbnull.helloflow.app.dto.task.*;
import cn.wbnull.helloflow.app.service.HfCommentService;
import cn.wbnull.helloflow.app.service.HfTaskHistoryService;
import cn.wbnull.helloflow.app.service.HfTaskService;
import cn.wbnull.helloflow.common.model.Result;
import cn.wbnull.helloflow.data.condition.TaskCondition;
import cn.wbnull.helloflow.security.util.SecurityUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务控制器
 *
 * @author null
 * @date 2026-05-26
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "任务管理", description = "任务CRUD、状态流转、子任务、关联、评论、历史")
public class TaskController {

    private final HfTaskService hfTaskService;
    private final HfCommentService hfCommentService;
    private final HfTaskHistoryService hfTaskHistoryService;

    @GetMapping
    @Operation(summary = "任务列表")
    public Result<Page<TaskVO>> listTasks(TaskCondition condition) {
        return Result.success(hfTaskService.listTasks(condition));
    }

    @PostMapping
    @Operation(summary = "创建任务")
    public Result<TaskVO> createTask(@Valid @RequestBody TaskCreateRequest request) {
        return Result.success(hfTaskService.createTask(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "任务详情")
    public Result<TaskVO> getTask(@PathVariable Long id) {
        return Result.success(hfTaskService.getTask(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新任务")
    public Result<TaskVO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskUpdateRequest request) {
        return Result.success(hfTaskService.updateTask(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除任务")
    public Result<Void> deleteTask(@PathVariable Long id) {
        hfTaskService.deleteTask(id);
        return Result.success();
    }

    @PostMapping("/{id}/assign")
    @Operation(summary = "分配任务")
    public Result<Void> assignTask(@PathVariable Long id, @Valid @RequestBody TaskAssignRequest request) {
        hfTaskService.assignTask(id, request);
        return Result.success();
    }

    @PostMapping("/{id}/transition")
    @Operation(summary = "任务状态流转")
    public Result<Void> transitionTask(@PathVariable Long id, @Valid @RequestBody TaskTransitionRequest request) {
        hfTaskService.transitionTask(id, request);
        return Result.success();
    }

    @PostMapping("/{id}/delay")
    @Operation(summary = "标记延期")
    public Result<Void> delayTask(@PathVariable Long id, @Valid @RequestBody TaskDelayRequest request) {
        hfTaskService.delayTask(id, request);
        return Result.success();
    }

    @GetMapping("/{id}/subtasks")
    @Operation(summary = "子任务列表")
    public Result<List<TaskVO>> listSubTasks(@PathVariable Long id) {
        return Result.success(hfTaskService.listSubTasks(id));
    }

    @PostMapping("/{id}/subtasks")
    @Operation(summary = "创建子任务")
    public Result<TaskVO> createSubTask(@PathVariable Long id,
                                        @Valid @RequestBody TaskCreateRequest request) {
        return Result.success(hfTaskService.createSubTask(id, request));
    }

    @GetMapping("/{id}/relations")
    @Operation(summary = "任务关联列表")
    public Result<List<TaskVO.TaskRelationVO>> listRelations(@PathVariable Long id) {
        return Result.success(hfTaskService.listRelations(id));
    }

    @PostMapping("/{id}/relations")
    @Operation(summary = "添加任务关联")
    public Result<Void> addRelation(@PathVariable Long id, @Valid @RequestBody TaskRelationCreateRequest request) {
        hfTaskService.addRelation(id, request);
        return Result.success();
    }

    @DeleteMapping("/{taskId}/relations/{relationId}")
    @Operation(summary = "删除任务关联")
    public Result<Void> removeRelation(@PathVariable Long taskId, @PathVariable Long relationId) {
        hfTaskService.removeRelation(relationId);
        return Result.success();
    }

    @GetMapping("/mine")
    @Operation(summary = "我负责的任务")
    public Result<Page<TaskVO>> listMyTasks(TaskCondition condition) {
        condition.setAssigneeId(SecurityUtils.getCurrentUserId());
        return Result.success(hfTaskService.listMyTasks(condition));
    }

    @GetMapping("/pending-review")
    @Operation(summary = "待我审查的任务")
    public Result<Page<TaskVO>> listPendingReviewTasks(TaskCondition condition) {
        condition.setAssigneeId(SecurityUtils.getCurrentUserId());
        return Result.success(hfTaskService.listPendingReviewTasks(condition));
    }

    @GetMapping("/{id}/comments")
    @Operation(summary = "评论列表")
    public Result<List<CommentVO>> listComments(@PathVariable Long id) {
        return Result.success(hfCommentService.listComments(id));
    }

    @PostMapping("/{id}/comments")
    @Operation(summary = "添加评论")
    public Result<CommentVO> addComment(@PathVariable Long id,
                                        @Valid @RequestBody CommentCreateRequest request) {
        return Result.success(hfCommentService.addComment(id, request));
    }

    @GetMapping("/{id}/activities")
    @Operation(summary = "操作历史列表")
    public Result<List<TaskHistoryVO>> listActivities(@PathVariable Long id) {
        return Result.success(hfTaskHistoryService.listHistories(id));
    }
}
