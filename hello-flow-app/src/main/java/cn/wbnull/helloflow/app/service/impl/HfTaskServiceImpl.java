package cn.wbnull.helloflow.app.service.impl;

import cn.wbnull.helloflow.app.dto.task.*;
import cn.wbnull.helloflow.app.service.HfNotificationService;
import cn.wbnull.helloflow.app.service.HfTaskHistoryService;
import cn.wbnull.helloflow.app.service.HfTaskService;
import cn.wbnull.helloflow.common.exception.BusinessException;
import cn.wbnull.helloflow.common.model.ResultCode;
import cn.wbnull.helloflow.common.util.BeanCopyUtils;
import cn.wbnull.helloflow.data.condition.TaskCondition;
import cn.wbnull.helloflow.data.entity.*;
import cn.wbnull.helloflow.data.enums.NotificationTypeEnum;
import cn.wbnull.helloflow.data.enums.TaskActionEnum;
import cn.wbnull.helloflow.data.enums.TaskStatusEnum;
import cn.wbnull.helloflow.data.repository.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务服务实现
 *
 * @author null
 * @date 2026-05-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HfTaskServiceImpl implements HfTaskService {

    private final HfTaskRepository hfTaskRepository;
    private final HfTaskRelationRepository hfTaskRelationRepository;
    private final HfProjectRepository hfProjectRepository;
    private final HfProjectMemberRepository hfProjectMemberRepository;
    private final HfPositionRepository hfPositionRepository;
    private final SysUserRepository sysUserRepository;
    private final HfTaskHistoryService taskHistoryService;
    private final HfNotificationService notificationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskVO createTask(Long projectId, TaskCreateRequest request, Long userId) {
        HfProject project = hfProjectRepository.selectById(projectId);
        if (project == null) {
            throw new BusinessException(ResultCode.PROJECT_NOT_FOUND);
        }
        if (project.getStatus() == 0) {
            throw new BusinessException(ResultCode.PROJECT_ARCHIVED);
        }
        HfTask task = new HfTask();
        task.setProjectId(projectId);
        Integer maxSeq = hfTaskRepository.selectMaxSeqByProjectId(projectId);
        int nextSeq = (maxSeq != null ? maxSeq : 0) + 1;
        task.setTaskSeq(nextSeq);
        task.setTaskCode(project.getCode() + "-" + String.format("%04d", nextSeq));
        task.setSprintId(request.getSprintId());
        task.setParentId(request.getParentId());
        task.setType(request.getType() != null ? request.getType() : 1);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatusEnum.TODO.getCode());
        task.setPriority(request.getPriority() != null ? request.getPriority() : 3);
        task.setReporterId(userId);
        task.setCreatedBy(userId);
        task.setUpdatedBy(userId);
        task.setIsDelayed(0);
        if (request.getAssigneeId() != null) {
            task.setAssigneeId(request.getAssigneeId());
        } else {
            // 未指定开发者，自动指派给项目开发主责
            task.setAssigneeId(project.getDevLeadId());
        }
        // 指派开发者
        if (request.getDeveloperId() != null) {
            task.setDeveloperId(request.getDeveloperId());
        }
        // 指派测试人员
        if (request.getTesterId() != null) {
            task.setTesterId(request.getTesterId());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(LocalDate.parse(request.getDueDate()));
        }
        hfTaskRepository.insert(task);
        // 记录创建历史
        taskHistoryService.recordHistory(TaskHistoryCommand.builder()
                .taskId(task.getId()).userId(userId).action(TaskActionEnum.CREATE).build());
        // 如果指定了开发者，发送通知
        if (task.getAssigneeId() != null) {
            notificationService.sendNotification(task.getAssigneeId(), "新任务分配",
                    "任务「" + task.getTitle() + "」已分配给您", NotificationTypeEnum.TASK_ASSIGN.getCode(), task.getId());
        }
        // 如果有关联任务，创建关联
        if (request.getRelatedTaskId() != null && request.getRelationType() != null) {
            HfTaskRelation relation = new HfTaskRelation();
            relation.setTaskId(task.getId());
            relation.setRelatedTaskId(request.getRelatedTaskId());
            relation.setRelationType(request.getRelationType());
            hfTaskRelationRepository.insert(relation);
        }
        log.info("创建任务：{}", task.getTitle());
        return toTaskVO(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskVO updateTask(Long id, TaskUpdateRequest request, Long userId) {
        HfTask task = getTaskOrThrow(id);
        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getType() != null) {
            task.setType(request.getType());
        }
        if (request.getPriority() != null) {
            Integer oldPriority = task.getPriority();
            task.setPriority(request.getPriority());
            taskHistoryService.recordHistory(TaskHistoryCommand.builder()
                    .taskId(id).userId(userId).action(TaskActionEnum.PRIORITY_CHANGE)
                    .field("priority").oldValue(String.valueOf(oldPriority)).newValue(String.valueOf(request.getPriority())).build());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(LocalDate.parse(request.getDueDate()));
        }
        if (request.getPlanStartDate() != null) {
            task.setPlanStartDate(LocalDate.parse(request.getPlanStartDate()));
        }
        if (request.getPlanEndDate() != null) {
            task.setPlanEndDate(LocalDate.parse(request.getPlanEndDate()));
        }
        if (request.getSprintId() != null) {
            task.setSprintId(request.getSprintId());
        }
        task.setUpdatedBy(userId);
        hfTaskRepository.updateById(task);
        taskHistoryService.recordHistory(TaskHistoryCommand.builder()
                .taskId(id).userId(userId).action(TaskActionEnum.UPDATE).build());
        return toTaskVO(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTask(Long id, Long userId) {
        HfTask task = getTaskOrThrow(id);
        if (!task.getCreatedBy().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        hfTaskRepository.deleteById(id);
        log.info("删除任务：id={}, operatorId={}", id, userId);
    }

    @Override
    public TaskVO getTask(Long id) {
        HfTask task = getTaskOrThrow(id);
        TaskVO vo = toTaskVO(task);
        // 加载子任务
        List<HfTask> subTasks = hfTaskRepository.selectByParentId(id);
        vo.setSubTasks(subTasks.stream().map(this::toTaskVO).collect(Collectors.toList()));
        // 加载关联
        vo.setRelations(listRelations(id));
        return vo;
    }

    @Override
    public Page<TaskVO> listTasks(TaskQueryRequest query) {
        TaskCondition condition = new TaskCondition();
        BeanCopyUtils.copyNonNullProperties(query, condition);
        Page<HfTask> pageResult = hfTaskRepository.selectPageByCondition(
                new Page<>(query.getPage(), query.getPageSize()), condition);
        Page<TaskVO> voPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        voPage.setRecords(pageResult.getRecords().stream().map(this::toTaskVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignTask(Long id, TaskAssignRequest request, Long operatorId) {
        HfTask task = getTaskOrThrow(id);
        Long oldAssignee = task.getAssigneeId();
        task.setAssigneeId(request.getAssigneeId());
        task.setDeveloperId(request.getAssigneeId());
        hfTaskRepository.updateById(task);
        taskHistoryService.recordHistory(TaskHistoryCommand.builder()
                .taskId(id).userId(operatorId).action(TaskActionEnum.ASSIGN)
                .field("assigneeId").oldValue(oldAssignee != null ? oldAssignee.toString() : null)
                .newValue(request.getAssigneeId() != null ? request.getAssigneeId().toString() : null).build());
        if (request.getAssigneeId() != null) {
            notificationService.sendNotification(request.getAssigneeId(), "任务分配",
                    "任务「" + task.getTitle() + "」已分配给您", NotificationTypeEnum.TASK_ASSIGN.getCode(), task.getId());
        }
        log.info("分配任务：taskId={}, assigneeId={}", id, request.getAssigneeId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startTask(Long id, Long userId) {
        HfTask task = getTaskOrThrow(id);
        if (task.getStatus() != TaskStatusEnum.TODO.getCode()) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        Integer oldStatus = task.getStatus();
        task.setStatus(TaskStatusEnum.IN_PROGRESS.getCode());
        task.setActualStartDate(LocalDate.now());
        hfTaskRepository.updateById(task);
        taskHistoryService.recordHistory(TaskHistoryCommand.builder()
                .taskId(id).userId(userId).action(TaskActionEnum.STATUS_CHANGE)
                .field("status").oldValue(String.valueOf(oldStatus)).newValue(String.valueOf(TaskStatusEnum.IN_PROGRESS.getCode())).build());
        log.info("开始开发：taskId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeDev(Long id, Long userId) {
        HfTask task = getTaskOrThrow(id);
        if (task.getStatus() != TaskStatusEnum.IN_PROGRESS.getCode()) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        Integer oldStatus = task.getStatus();
        task.setStatus(TaskStatusEnum.IN_REVIEW.getCode());
        hfTaskRepository.updateById(task);
        taskHistoryService.recordHistory(TaskHistoryCommand.builder()
                .taskId(id).userId(userId).action(TaskActionEnum.STATUS_CHANGE)
                .field("status").oldValue(String.valueOf(oldStatus)).newValue(String.valueOf(TaskStatusEnum.IN_REVIEW.getCode())).build());
        // 通知开发主责
        HfProject project = hfProjectRepository.selectById(task.getProjectId());
        if (project != null && project.getDevLeadId() != null) {
            notificationService.sendNotification(project.getDevLeadId(), "任务待评审",
                    "任务「" + task.getTitle() + "」开发完成，等待评审",
                    NotificationTypeEnum.STATUS_CHANGE.getCode(), task.getId());
        }
        log.info("开发完成：taskId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reviewPass(Long id, Long userId) {
        HfTask task = getTaskOrThrow(id);
        if (task.getStatus() != TaskStatusEnum.IN_REVIEW.getCode()) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        validateReviewPermission(task, userId);
        Integer oldStatus = task.getStatus();
        task.setStatus(TaskStatusEnum.IN_TEST.getCode());
        hfTaskRepository.updateById(task);
        taskHistoryService.recordHistory(TaskHistoryCommand.builder()
                .taskId(id).userId(userId).action(TaskActionEnum.STATUS_CHANGE)
                .field("status").oldValue(String.valueOf(oldStatus)).newValue(String.valueOf(TaskStatusEnum.IN_TEST.getCode())).build());
        // 自动指派测试人员：优先使用任务已指定的测试工程师，否则指派项目测试主责
        Long testerId = task.getTesterId();
        HfProject project = hfProjectRepository.selectById(task.getProjectId());
        if (testerId == null && project != null) {
            testerId = project.getTestLeadId();
        }
        if (testerId != null) {
            task.setAssigneeId(testerId);
            task.setTesterId(testerId);
            hfTaskRepository.updateById(task);
            notificationService.sendNotification(testerId, "任务待测试",
                    "任务「" + task.getTitle() + "」评审通过，等待测试",
                    NotificationTypeEnum.STATUS_CHANGE.getCode(), task.getId());
        }
        log.info("评审通过：taskId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reviewReject(Long id, Long userId) {
        HfTask task = getTaskOrThrow(id);
        if (task.getStatus() != TaskStatusEnum.IN_REVIEW.getCode()) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        validateReviewPermission(task, userId);
        Integer oldStatus = task.getStatus();
        task.setStatus(TaskStatusEnum.IN_PROGRESS.getCode());
        task.setAssigneeId(task.getDeveloperId());
        hfTaskRepository.updateById(task);
        taskHistoryService.recordHistory(TaskHistoryCommand.builder()
                .taskId(id).userId(userId).action(TaskActionEnum.STATUS_CHANGE)
                .field("status").oldValue(String.valueOf(oldStatus)).newValue(String.valueOf(TaskStatusEnum.IN_PROGRESS.getCode())).build());
        // 通知开发工程师
        if (task.getDeveloperId() != null) {
            notificationService.sendNotification(task.getDeveloperId(), "任务评审不通过",
                    "任务「" + task.getTitle() + "」评审不通过，请修改",
                    NotificationTypeEnum.STATUS_CHANGE.getCode(), task.getId());
        }
        log.info("评审不通过：taskId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testPass(Long id, Long userId) {
        HfTask task = getTaskOrThrow(id);
        if (task.getStatus() != TaskStatusEnum.IN_TEST.getCode()) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        Integer oldStatus = task.getStatus();
        task.setStatus(TaskStatusEnum.DONE.getCode());
        task.setActualEndDate(LocalDate.now());
        hfTaskRepository.updateById(task);
        taskHistoryService.recordHistory(TaskHistoryCommand.builder()
                .taskId(id).userId(userId).action(TaskActionEnum.STATUS_CHANGE)
                .field("status").oldValue(String.valueOf(oldStatus)).newValue(String.valueOf(TaskStatusEnum.DONE.getCode())).build());
        // 检查父任务状态
        checkParentTaskStatus(task.getParentId());
        // 通知创建者
        if (task.getReporterId() != null) {
            notificationService.sendNotification(task.getReporterId(), "任务已完成",
                    "任务「" + task.getTitle() + "」测试通过，已完成",
                    NotificationTypeEnum.STATUS_CHANGE.getCode(), task.getId());
        }
        log.info("测试通过：taskId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testReject(Long id, Long userId) {
        HfTask task = getTaskOrThrow(id);
        if (task.getStatus() != TaskStatusEnum.IN_TEST.getCode()) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        Integer oldStatus = task.getStatus();
        task.setStatus(TaskStatusEnum.IN_PROGRESS.getCode());
        task.setAssigneeId(task.getDeveloperId());
        hfTaskRepository.updateById(task);
        taskHistoryService.recordHistory(TaskHistoryCommand.builder()
                .taskId(id).userId(userId).action(TaskActionEnum.TEST_REJECT)
                .field("status").oldValue(String.valueOf(oldStatus)).newValue(String.valueOf(TaskStatusEnum.IN_PROGRESS.getCode())).build());
        if (task.getDeveloperId() != null) {
            notificationService.sendNotification(task.getDeveloperId(), "任务测试不通过",
                    "任务「" + task.getTitle() + "」测试不通过，请修改",
                    NotificationTypeEnum.STATUS_CHANGE.getCode(), task.getId());
        }
        log.info("测试不通过：taskId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reopenTask(Long id, Long userId) {
        HfTask task = getTaskOrThrow(id);
        if (task.getStatus() != TaskStatusEnum.DONE.getCode() && task.getStatus() != TaskStatusEnum.CLOSED.getCode()) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        Integer oldStatus = task.getStatus();
        task.setStatus(TaskStatusEnum.TODO.getCode());
        task.setAssigneeId(task.getDeveloperId());
        task.setActualEndDate(null);
        task.setCloseDate(null);
        hfTaskRepository.updateById(task);
        taskHistoryService.recordHistory(TaskHistoryCommand.builder()
                .taskId(id).userId(userId).action(TaskActionEnum.REOPEN)
                .field("status").oldValue(String.valueOf(oldStatus)).newValue(String.valueOf(TaskStatusEnum.TODO.getCode())).build());
        if (task.getDeveloperId() != null) {
            notificationService.sendNotification(task.getDeveloperId(), "任务重新打开",
                    "任务「" + task.getTitle() + "」已重新打开",
                    NotificationTypeEnum.REOPEN.getCode(), task.getId());
        }
        log.info("重新打开任务：taskId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeTask(Long id, Long userId) {
        HfTask task = getTaskOrThrow(id);
        if (task.getStatus() != TaskStatusEnum.DONE.getCode()) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        Integer oldStatus = task.getStatus();
        task.setStatus(TaskStatusEnum.CLOSED.getCode());
        task.setCloseDate(java.time.LocalDateTime.now());
        hfTaskRepository.updateById(task);
        taskHistoryService.recordHistory(TaskHistoryCommand.builder()
                .taskId(id).userId(userId).action(TaskActionEnum.CLOSE)
                .field("status").oldValue(String.valueOf(oldStatus)).newValue(String.valueOf(TaskStatusEnum.CLOSED.getCode())).build());
        log.info("关闭任务：taskId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelTask(Long id, TaskCancelRequest request, Long userId) {
        HfTask task = getTaskOrThrow(id);
        if (task.getStatus() != TaskStatusEnum.TODO.getCode() && task.getStatus() != TaskStatusEnum.IN_PROGRESS.getCode()) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        Integer oldStatus = task.getStatus();
        task.setStatus(TaskStatusEnum.CANCELLED.getCode());
        task.setCancelReason(request.getCancelReason());
        hfTaskRepository.updateById(task);
        taskHistoryService.recordHistory(TaskHistoryCommand.builder()
                .taskId(id).userId(userId).action(TaskActionEnum.CANCEL)
                .field("status").oldValue(String.valueOf(oldStatus)).newValue(String.valueOf(TaskStatusEnum.CANCELLED.getCode())).build());
        log.info("取消任务：taskId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delayTask(Long id, TaskDelayRequest request, Long userId) {
        HfTask task = getTaskOrThrow(id);
        if (task.getStatus() != TaskStatusEnum.IN_PROGRESS.getCode()) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        task.setIsDelayed(1);
        task.setDelayReason(request.getDelayReason());
        hfTaskRepository.updateById(task);
        taskHistoryService.recordHistory(TaskHistoryCommand.builder()
                .taskId(id).userId(userId).action(TaskActionEnum.DELAY)
                .field("isDelayed").oldValue("0").newValue("1").build());
        log.info("标记延期：taskId={}", id);
    }

    @Override
    public List<TaskVO> listSubTasks(Long parentId) {
        List<HfTask> subTasks = hfTaskRepository.selectByParentId(parentId);
        return subTasks.stream().map(this::toTaskVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskVO createSubTask(Long parentId, TaskCreateRequest request, Long userId) {
        HfTask parent = hfTaskRepository.selectById(parentId);
        if (parent == null) {
            throw new BusinessException(ResultCode.TASK_NOT_FOUND);
        }
        request.setParentId(parentId);
        return createTask(parent.getProjectId(), request, userId);
    }

    @Override
    public List<TaskVO.TaskRelationVO> listRelations(Long taskId) {
        List<HfTaskRelation> relations = hfTaskRelationRepository.selectByTaskId(taskId);
        return relations.stream().map(r -> {
            TaskVO.TaskRelationVO vo = new TaskVO.TaskRelationVO();
            BeanCopyUtils.copyNonNullProperties(r, vo);
            HfTask relatedTask = hfTaskRepository.selectById(r.getRelatedTaskId());
            if (relatedTask != null) {
                vo.setRelatedTaskTitle(relatedTask.getTitle());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRelation(Long taskId, TaskRelationCreateRequest request) {
        HfTaskRelation existing = hfTaskRelationRepository.selectByTaskIdAndRelatedTaskIdAndType(
                taskId, request.getRelatedTaskId(), request.getRelationType());
        if (existing != null) {
            throw new BusinessException(ResultCode.TASK_RELATION_EXISTS);
        }
        HfTaskRelation relation = new HfTaskRelation();
        relation.setTaskId(taskId);
        relation.setRelatedTaskId(request.getRelatedTaskId());
        relation.setRelationType(request.getRelationType());
        hfTaskRelationRepository.insert(relation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRelation(Long relationId) {
        hfTaskRelationRepository.deleteById(relationId);
    }

    @Override
    public Page<TaskVO> listMyTasks(TaskCondition condition) {
        Page<HfTask> pageResult = hfTaskRepository.selectPageByAssigneeId(
                new Page<>(condition.getPage(), condition.getPageSize()), condition);
        Page<TaskVO> voPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        voPage.setRecords(pageResult.getRecords().stream().map(this::toTaskVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public Page<TaskVO> listReportedTasks(TaskCondition condition) {
        Page<HfTask> pageResult = hfTaskRepository.selectPageByReporterId(
                new Page<>(condition.getPage(), condition.getPageSize()), condition);
        Page<TaskVO> voPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        voPage.setRecords(pageResult.getRecords().stream().map(this::toTaskVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public Page<TaskVO> listRelatedTasks(TaskCondition condition) {
        Page<HfTask> pageResult = hfTaskRepository.selectPageByRelatedUserId(
                new Page<>(condition.getPage(), condition.getPageSize()), condition);
        Page<TaskVO> voPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        voPage.setRecords(pageResult.getRecords().stream().map(this::toTaskVO).collect(Collectors.toList()));
        return voPage;
    }

    /**
     * 检查父任务状态，子任务全部完成后自动更新父任务状态
     */
    private void checkParentTaskStatus(Long parentId) {
        if (parentId == null) {
            return;
        }
        HfTask parent = hfTaskRepository.selectById(parentId);
        if (parent == null) {
            return;
        }
        List<HfTask> subTasks = hfTaskRepository.selectByParentId(parentId);
        boolean allDone = subTasks.stream().allMatch(
                t -> t.getStatus() == TaskStatusEnum.DONE.getCode()
                        || t.getStatus() == TaskStatusEnum.CLOSED.getCode()
                        || t.getStatus() == TaskStatusEnum.CANCELLED.getCode());
        if (allDone && !subTasks.isEmpty()) {
            if (parent.getStatus() == TaskStatusEnum.TODO.getCode()) {
                parent.setStatus(TaskStatusEnum.DONE.getCode());
                hfTaskRepository.updateById(parent);
            } else if (parent.getStatus() == TaskStatusEnum.IN_PROGRESS.getCode()) {
                parent.setStatus(TaskStatusEnum.IN_REVIEW.getCode());
                hfTaskRepository.updateById(parent);
            }
        }
    }

    private TaskVO toTaskVO(HfTask task) {
        TaskVO vo = new TaskVO();
        BeanCopyUtils.copyNonNullProperties(task, vo);
        // 填充用户名
        if (task.getAssigneeId() != null) {
            SysUser assignee = sysUserRepository.selectById(task.getAssigneeId());
            if (assignee != null) {
                vo.setAssigneeName(assignee.getNickname() != null ? assignee.getNickname() : assignee.getUsername());
            }
        }
        if (task.getReporterId() != null) {
            SysUser reporter = sysUserRepository.selectById(task.getReporterId());
            if (reporter != null) {
                vo.setReporterName(reporter.getNickname() != null ? reporter.getNickname() : reporter.getUsername());
            }
        }
        if (task.getDeveloperId() != null) {
            SysUser developer = sysUserRepository.selectById(task.getDeveloperId());
            if (developer != null) {
                vo.setDeveloperName(developer.getNickname() != null ? developer.getNickname() : developer.getUsername());
            }
        }
        if (task.getTesterId() != null) {
            SysUser tester = sysUserRepository.selectById(task.getTesterId());
            if (tester != null) {
                vo.setTesterName(tester.getNickname() != null ? tester.getNickname() : tester.getUsername());
            }
        }
        return vo;
    }

    private HfTask getTaskOrThrow(Long id) {
        HfTask task = hfTaskRepository.selectById(id);
        if (task == null) {
            throw new BusinessException(ResultCode.TASK_NOT_FOUND);
        }
        return task;
    }

    private void validateReviewPermission(HfTask task, Long userId) {
        SysUser user = sysUserRepository.selectById(userId);
        if (user == null || user.getPositionId() == null) {
            return;
        }
        HfPosition position = hfPositionRepository.selectById(user.getPositionId());
        if (position == null) {
            return;
        }
        // 项目经理不允许评审
        if ("PM".equals(position.getCode())) {
            throw new BusinessException(ResultCode.PM_CANNOT_REVIEW);
        }
        // 开发工程师评审自己的任务：仅当项目中只有自己一个开发工程师时允许
        if ("DEV".equals(position.getCode()) && task.getDeveloperId() != null && task.getDeveloperId().equals(userId)) {
            HfProject project = hfProjectRepository.selectById(task.getProjectId());
            if (project == null) {
                throw new BusinessException(ResultCode.CANNOT_REVIEW_OWN_TASK);
            }
            long devCount = hfProjectMemberRepository.selectByProjectId(project.getId()).stream()
                    .filter(m -> {
                        SysUser member = sysUserRepository.selectById(m.getUserId());
                        if (member == null || member.getPositionId() == null) {
                            return false;
                        }
                        HfPosition memberPosition = hfPositionRepository.selectById(member.getPositionId());
                        return memberPosition != null && "DEV".equals(memberPosition.getCode());
                    }).count();
            if (devCount > 1) {
                throw new BusinessException(ResultCode.CANNOT_REVIEW_OWN_TASK);
            }
        }
    }
}
