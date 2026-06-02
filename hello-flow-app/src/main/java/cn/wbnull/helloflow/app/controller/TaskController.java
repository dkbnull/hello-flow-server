package cn.wbnull.helloflow.app.controller;

import cn.wbnull.helloflow.app.dto.comment.CommentCreateRequest;
import cn.wbnull.helloflow.app.dto.comment.CommentVO;
import cn.wbnull.helloflow.app.dto.task.*;
import cn.wbnull.helloflow.app.service.HfCommentService;
import cn.wbnull.helloflow.app.service.HfTaskHistoryService;
import cn.wbnull.helloflow.app.service.HfTaskService;
import cn.wbnull.helloflow.common.model.Result;
import cn.wbnull.helloflow.security.util.SecurityUtils;
import cn.wbnull.helloflow.data.condition.TaskCondition;
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
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "任务管理", description = "任务CRUD、状态流转、子任务、关联、评论、历史")
public class TaskController {

    private final HfTaskService hfTaskService;
    private final HfCommentService hfCommentService;
    private final HfTaskHistoryService hfTaskHistoryService;

    @GetMapping("/projects/{projectId}/tasks")
    @Operation(summary = "任务列表")
    public Result<Page<TaskVO>> listTasks(@PathVariable Long projectId, TaskQueryRequest query) {
        query.setProjectId(projectId);
        return Result.success(hfTaskService.listTasks(query));
    }

    @PostMapping("/projects/{projectId}/tasks")
    @Operation(summary = "创建任务")
    public Result<TaskVO> createTask(@PathVariable Long projectId,
                                     @Valid @RequestBody TaskCreateRequest request) {
        return Result.success(hfTaskService.createTask(projectId, request));
    }

    @GetMapping("/tasks/{id}")
    @Operation(summary = "任务详情")
    public Result<TaskVO> getTask(@PathVariable Long id) {
        return Result.success(hfTaskService.getTask(id));
    }

    @PutMapping("/tasks/{id}")
    @Operation(summary = "更新任务")
    public Result<TaskVO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskUpdateRequest request) {
        return Result.success(hfTaskService.updateTask(id, request));
    }

    @DeleteMapping("/tasks/{id}")
    @Operation(summary = "删除任务")
    public Result<Void> deleteTask(@PathVariable Long id) {
        hfTaskService.deleteTask(id);
        return Result.success();
    }

    @PostMapping("/tasks/{id}/assign")
    @Operation(summary = "分配任务")
    public Result<Void> assignTask(@PathVariable Long id, @Valid @RequestBody TaskAssignRequest request) {
        hfTaskService.assignTask(id, request);
        return Result.success();
    }

    @PostMapping("/tasks/{id}/start")
    @Operation(summary = "开始开发")
    public Result<Void> startTask(@PathVariable Long id) {
        hfTaskService.startTask(id);
        return Result.success();
    }

    @PostMapping("/tasks/{id}/complete-dev")
    @Operation(summary = "开发完成")
    public Result<Void> completeDev(@PathVariable Long id) {
        hfTaskService.completeDev(id);
        return Result.success();
    }

    @PostMapping("/tasks/{id}/review-pass")
    @Operation(summary = "评审通过")
    public Result<Void> reviewPass(@PathVariable Long id) {
        hfTaskService.reviewPass(id);
        return Result.success();
    }

    @PostMapping("/tasks/{id}/review-reject")
    @Operation(summary = "评审不通过")
    public Result<Void> reviewReject(@PathVariable Long id) {
        hfTaskService.reviewReject(id);
        return Result.success();
    }

    @PostMapping("/tasks/{id}/test-pass")
    @Operation(summary = "测试通过")
    public Result<Void> testPass(@PathVariable Long id) {
        hfTaskService.testPass(id);
        return Result.success();
    }

    @PostMapping("/tasks/{id}/test-reject")
    @Operation(summary = "测试不通过")
    public Result<Void> testReject(@PathVariable Long id) {
        hfTaskService.testReject(id);
        return Result.success();
    }

    @PostMapping("/tasks/{id}/reopen")
    @Operation(summary = "重新打开")
    public Result<Void> reopenTask(@PathVariable Long id) {
        hfTaskService.reopenTask(id);
        return Result.success();
    }

    @PostMapping("/tasks/{id}/close")
    @Operation(summary = "关闭任务")
    public Result<Void> closeTask(@PathVariable Long id) {
        hfTaskService.closeTask(id);
        return Result.success();
    }

    @PostMapping("/tasks/{id}/cancel")
    @Operation(summary = "取消任务")
    public Result<Void> cancelTask(@PathVariable Long id, @Valid @RequestBody TaskCancelRequest request) {
        hfTaskService.cancelTask(id, request);
        return Result.success();
    }

    @PostMapping("/tasks/{id}/delay")
    @Operation(summary = "标记延期")
    public Result<Void> delayTask(@PathVariable Long id, @Valid @RequestBody TaskDelayRequest request) {
        hfTaskService.delayTask(id, request);
        return Result.success();
    }

    @GetMapping("/tasks/{id}/subtasks")
    @Operation(summary = "子任务列表")
    public Result<List<TaskVO>> listSubTasks(@PathVariable Long id) {
        return Result.success(hfTaskService.listSubTasks(id));
    }

    @PostMapping("/tasks/{id}/subtasks")
    @Operation(summary = "创建子任务")
    public Result<TaskVO> createSubTask(@PathVariable Long id,
                                        @Valid @RequestBody TaskCreateRequest request) {
        return Result.success(hfTaskService.createSubTask(id, request));
    }

    @GetMapping("/tasks/{id}/relations")
    @Operation(summary = "任务关联列表")
    public Result<List<TaskVO.TaskRelationVO>> listRelations(@PathVariable Long id) {
        return Result.success(hfTaskService.listRelations(id));
    }

    @PostMapping("/tasks/{id}/relations")
    @Operation(summary = "添加任务关联")
    public Result<Void> addRelation(@PathVariable Long id, @Valid @RequestBody TaskRelationCreateRequest request) {
        hfTaskService.addRelation(id, request);
        return Result.success();
    }

    @DeleteMapping("/tasks/{taskId}/relations/{relationId}")
    @Operation(summary = "删除任务关联")
    public Result<Void> removeRelation(@PathVariable Long taskId, @PathVariable Long relationId) {
        hfTaskService.removeRelation(relationId);
        return Result.success();
    }

    @GetMapping("/tasks/mine")
    @Operation(summary = "我的任务")
    public Result<Page<TaskVO>> listMyTasks(TaskCondition condition) {
        condition.setAssigneeId(SecurityUtils.getCurrentUserId());
        return Result.success(hfTaskService.listMyTasks(condition));
    }

    @GetMapping("/tasks/reported")
    @Operation(summary = "我创建的任务")
    public Result<Page<TaskVO>> listReportedTasks(TaskCondition condition) {
        condition.setReporterId(SecurityUtils.getCurrentUserId());
        return Result.success(hfTaskService.listReportedTasks(condition));
    }

    @GetMapping("/tasks/related")
    @Operation(summary = "与我相关的任务")
    public Result<Page<TaskVO>> listRelatedTasks(TaskCondition condition) {
        condition.setAssigneeId(SecurityUtils.getCurrentUserId());
        return Result.success(hfTaskService.listRelatedTasks(condition));
    }

    @GetMapping("/tasks/{id}/comments")
    @Operation(summary = "评论列表")
    public Result<List<CommentVO>> listComments(@PathVariable Long id) {
        return Result.success(hfCommentService.listComments(id));
    }

    @PostMapping("/tasks/{id}/comments")
    @Operation(summary = "添加评论")
    public Result<CommentVO> addComment(@PathVariable Long id,
                                        @Valid @RequestBody CommentCreateRequest request) {
        return Result.success(hfCommentService.addComment(id, request));
    }

    @GetMapping("/tasks/{id}/activities")
    @Operation(summary = "操作历史列表")
    public Result<List<TaskHistoryVO>> listActivities(@PathVariable Long id) {
        return Result.success(hfTaskHistoryService.listHistories(id));
    }
}
