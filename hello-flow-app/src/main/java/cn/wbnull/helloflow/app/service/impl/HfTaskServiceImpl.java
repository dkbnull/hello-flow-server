package cn.wbnull.helloflow.app.service.impl;

import cn.wbnull.helloflow.app.dto.mapstruct.TaskMapper;
import cn.wbnull.helloflow.app.dto.task.*;
import cn.wbnull.helloflow.app.service.HfNotificationService;
import cn.wbnull.helloflow.app.service.HfProjectService;
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
import cn.wbnull.helloflow.data.util.PageUtils;
import cn.wbnull.helloflow.security.util.SecurityUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final HfProjectService hfProjectService;
    private final TaskMapper taskMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskVO createTask(TaskCreateRequest request) {
        Long projectId = request.getProjectId();
        Long userId = SecurityUtils.getCurrentUserId();
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
            task.setDueDate(request.getDueDate());
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
    public TaskVO updateTask(Long id, TaskUpdateRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        HfTask task = getTaskOrThrow(id);
        hfProjectService.validateNotArchived(task.getProjectId());
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
            task.setDueDate(request.getDueDate());
        }
        if (request.getPlanStartDate() != null) {
            task.setPlanStartDate(request.getPlanStartDate());
        }
        if (request.getPlanEndDate() != null) {
            task.setPlanEndDate(request.getPlanEndDate());
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
    public void deleteTask(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        HfTask task = getTaskOrThrow(id);
        hfProjectService.validateNotArchived(task.getProjectId());
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
    public Page<TaskVO> listTasks(TaskCondition condition) {
        Page<HfTask> pageResult = hfTaskRepository.selectPageByCondition(
                new Page<>(condition.getPage(), condition.getPageSize()), condition);
        return PageUtils.convertPage(pageResult, this::toTaskVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignTask(Long id, TaskAssignRequest request) {
        Long operatorId = SecurityUtils.getCurrentUserId();
        HfTask task = getTaskOrThrow(id);
        hfProjectService.validateNotArchived(task.getProjectId());
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
    public void transitionTask(Long id, TaskTransitionRequest request) {
        TaskStatusEnum targetStatus = TaskStatusEnum.fromCode(request.getTargetStatus());
        switch (targetStatus) {
            case IN_PROGRESS:
                handleStartOrReject(id, request);
                break;
            case IN_REVIEW:
                handleCompleteDev(id);
                break;
            case IN_TEST:
                handleReviewPass(id);
                break;
            case DONE:
                handleTestPass(id);
                break;
            case TODO:
                handleReopen(id);
                break;
            case CLOSED:
                handleClose(id);
                break;
            case CANCELLED:
                handleCancel(id, request);
                break;
            default:
                throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
    }

    private void handleStartOrReject(Long id, TaskTransitionRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        HfTask task = getTaskOrThrow(id);
        hfProjectService.validateNotArchived(task.getProjectId());
        // 从"未开始"→"进行中"为开始开发
        if (TaskStatusEnum.TODO.matches(task.getStatus())) {
            Integer oldStatus = task.getStatus();
            task.setStatus(TaskStatusEnum.IN_PROGRESS.getCode());
            task.setActualStartDate(LocalDate.now());
            hfTaskRepository.updateById(task);
            recordStatusHistory(id, userId, oldStatus, TaskStatusEnum.IN_PROGRESS, TaskActionEnum.STATUS_CHANGE);
            log.info("开始开发：taskId={}", id);
            return;
        }
        // 从"待审查"→"进行中"为审查不通过
        if (TaskStatusEnum.IN_REVIEW.matches(task.getStatus())) {
            validateReviewPermission(task, userId);
            Integer oldStatus = task.getStatus();
            task.setStatus(TaskStatusEnum.IN_PROGRESS.getCode());
            task.setAssigneeId(task.getDeveloperId());
            hfTaskRepository.updateById(task);
            recordStatusHistory(id, userId, oldStatus, TaskStatusEnum.IN_PROGRESS, TaskActionEnum.STATUS_CHANGE);
            if (task.getDeveloperId() != null) {
                notificationService.sendNotification(task.getDeveloperId(), "任务审查不通过",
                        "任务「" + task.getTitle() + "」审查不通过，请修改",
                        NotificationTypeEnum.STATUS_CHANGE.getCode(), task.getId());
            }
            log.info("审查不通过：taskId={}", id);
            return;
        }
        // 从"待测试"→"进行中"为测试不通过
        if (TaskStatusEnum.IN_TEST.matches(task.getStatus())) {
            Integer oldStatus = task.getStatus();
            task.setStatus(TaskStatusEnum.IN_PROGRESS.getCode());
            task.setAssigneeId(task.getDeveloperId());
            hfTaskRepository.updateById(task);
            recordStatusHistory(id, userId, oldStatus, TaskStatusEnum.IN_PROGRESS, TaskActionEnum.TEST_REJECT);
            if (task.getDeveloperId() != null) {
                notificationService.sendNotification(task.getDeveloperId(), "任务测试不通过",
                        "任务「" + task.getTitle() + "」测试不通过，请修改",
                        NotificationTypeEnum.STATUS_CHANGE.getCode(), task.getId());
            }
            log.info("测试不通过：taskId={}", id);
            return;
        }
        throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
    }

    private void handleCompleteDev(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        HfTask task = getTaskOrThrow(id);
        hfProjectService.validateNotArchived(task.getProjectId());
        if (!TaskStatusEnum.IN_PROGRESS.matches(task.getStatus())) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        Integer oldStatus = task.getStatus();
        task.setStatus(TaskStatusEnum.IN_REVIEW.getCode());
        // 进入待审查时，将负责人设置为项目开发主责（审查人）
        HfProject project = hfProjectRepository.selectById(task.getProjectId());
        if (project != null && project.getDevLeadId() != null) {
            task.setAssigneeId(project.getDevLeadId());
        }
        hfTaskRepository.updateById(task);
        recordStatusHistory(id, userId, oldStatus, TaskStatusEnum.IN_REVIEW, TaskActionEnum.STATUS_CHANGE);
        if (project != null && project.getDevLeadId() != null) {
            notificationService.sendNotification(project.getDevLeadId(), "任务待审查",
                    "任务「" + task.getTitle() + "」开发完成，等待审查",
                    NotificationTypeEnum.STATUS_CHANGE.getCode(), task.getId());
        }
        log.info("开发完成：taskId={}", id);
    }

    private void handleReviewPass(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        HfTask task = getTaskOrThrow(id);
        hfProjectService.validateNotArchived(task.getProjectId());
        if (!TaskStatusEnum.IN_REVIEW.matches(task.getStatus())) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        validateReviewPermission(task, userId);
        Integer oldStatus = task.getStatus();
        task.setStatus(TaskStatusEnum.IN_TEST.getCode());
        hfTaskRepository.updateById(task);
        recordStatusHistory(id, userId, oldStatus, TaskStatusEnum.IN_TEST, TaskActionEnum.STATUS_CHANGE);
        // 自动指派测试人员
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
                    "任务「" + task.getTitle() + "」审查通过，等待测试",
                    NotificationTypeEnum.STATUS_CHANGE.getCode(), task.getId());
        }
        log.info("审查通过：taskId={}", id);
    }

    private void handleTestPass(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        HfTask task = getTaskOrThrow(id);
        hfProjectService.validateNotArchived(task.getProjectId());
        if (!TaskStatusEnum.IN_TEST.matches(task.getStatus())) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        Integer oldStatus = task.getStatus();
        task.setStatus(TaskStatusEnum.DONE.getCode());
        task.setActualEndDate(LocalDate.now());
        hfTaskRepository.updateById(task);
        recordStatusHistory(id, userId, oldStatus, TaskStatusEnum.DONE, TaskActionEnum.STATUS_CHANGE);
        checkParentTaskStatus(task.getParentId());
        if (task.getReporterId() != null) {
            notificationService.sendNotification(task.getReporterId(), "任务已完成",
                    "任务「" + task.getTitle() + "」测试通过，已完成",
                    NotificationTypeEnum.STATUS_CHANGE.getCode(), task.getId());
        }
        log.info("测试通过：taskId={}", id);
    }

    private void handleReopen(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        HfTask task = getTaskOrThrow(id);
        hfProjectService.validateNotArchived(task.getProjectId());
        if (!TaskStatusEnum.DONE.matches(task.getStatus()) && !TaskStatusEnum.CLOSED.matches(task.getStatus())) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        Integer oldStatus = task.getStatus();
        task.setStatus(TaskStatusEnum.TODO.getCode());
        task.setAssigneeId(task.getDeveloperId());
        task.setActualEndDate(null);
        task.setCloseDate(null);
        hfTaskRepository.updateById(task);
        recordStatusHistory(id, userId, oldStatus, TaskStatusEnum.TODO, TaskActionEnum.REOPEN);
        if (task.getDeveloperId() != null) {
            notificationService.sendNotification(task.getDeveloperId(), "任务重新打开",
                    "任务「" + task.getTitle() + "」已重新打开",
                    NotificationTypeEnum.REOPEN.getCode(), task.getId());
        }
        log.info("重新打开任务：taskId={}", id);
    }

    private void handleClose(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        HfTask task = getTaskOrThrow(id);
        hfProjectService.validateNotArchived(task.getProjectId());
        if (!TaskStatusEnum.DONE.matches(task.getStatus())) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        Integer oldStatus = task.getStatus();
        task.setStatus(TaskStatusEnum.CLOSED.getCode());
        task.setCloseDate(java.time.LocalDateTime.now());
        hfTaskRepository.updateById(task);
        recordStatusHistory(id, userId, oldStatus, TaskStatusEnum.CLOSED, TaskActionEnum.CLOSE);
        log.info("关闭任务：taskId={}", id);
    }

    private void handleCancel(Long id, TaskTransitionRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        HfTask task = getTaskOrThrow(id);
        hfProjectService.validateNotArchived(task.getProjectId());
        if (!TaskStatusEnum.TODO.matches(task.getStatus()) && !TaskStatusEnum.IN_PROGRESS.matches(task.getStatus())) {
            throw new BusinessException(ResultCode.TASK_STATUS_TRANSITION_INVALID);
        }
        Integer oldStatus = task.getStatus();
        task.setStatus(TaskStatusEnum.CANCELLED.getCode());
        task.setCancelReason(request.getCancelReason());
        hfTaskRepository.updateById(task);
        recordStatusHistory(id, userId, oldStatus, TaskStatusEnum.CANCELLED, TaskActionEnum.CANCEL);
        log.info("取消任务：taskId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delayTask(Long id, TaskDelayRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        HfTask task = getTaskOrThrow(id);
        hfProjectService.validateNotArchived(task.getProjectId());
        if (!TaskStatusEnum.IN_PROGRESS.matches(task.getStatus())) {
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
    public TaskVO createSubTask(Long parentId, TaskCreateRequest request) {
        HfTask parent = hfTaskRepository.selectById(parentId);
        if (parent == null) {
            throw new BusinessException(ResultCode.TASK_NOT_FOUND);
        }
        request.setParentId(parentId);
        request.setProjectId(parent.getProjectId());
        return createTask(request);
    }

    @Override
    public List<TaskVO.TaskRelationVO> listRelations(Long taskId) {
        List<TaskVO.TaskRelationVO> result = new ArrayList<>();

        // 正向查询：当前任务作为 task_id
        List<HfTaskRelation> forwardRelations = hfTaskRelationRepository.selectByTaskId(taskId);
        for (HfTaskRelation r : forwardRelations) {
            TaskVO.TaskRelationVO vo = new TaskVO.TaskRelationVO();
            BeanCopyUtils.copyNonNullProperties(r, vo);
            fillRelatedTaskInfo(vo, r.getRelatedTaskId());
            result.add(vo);
        }

        // 反向查询：当前任务作为 related_task_id
        List<HfTaskRelation> reverseRelations = hfTaskRelationRepository.selectByRelatedTaskId(taskId);
        for (HfTaskRelation r : reverseRelations) {
            TaskVO.TaskRelationVO vo = new TaskVO.TaskRelationVO();
            vo.setId(r.getId());
            vo.setRelatedTaskId(r.getTaskId());
            vo.setRelationType(r.getRelationType());
            fillRelatedTaskInfo(vo, r.getTaskId());
            result.add(vo);
        }

        return result;
    }

    private void fillRelatedTaskInfo(TaskVO.TaskRelationVO vo, Long relatedTaskId) {
        HfTask relatedTask = hfTaskRepository.selectById(relatedTaskId);
        if (relatedTask != null) {
            vo.setRelatedTaskCode(relatedTask.getTaskCode());
            vo.setRelatedTaskTitle(relatedTask.getTitle());
            vo.setRelatedTaskType(relatedTask.getType());
            vo.setRelatedTaskStatus(relatedTask.getStatus());
            vo.setRelatedTaskDeveloperId(relatedTask.getDeveloperId());
            if (relatedTask.getDeveloperId() != null) {
                vo.setRelatedTaskDeveloperName(sysUserRepository.getDisplayName(relatedTask.getDeveloperId()));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRelation(Long taskId, TaskRelationCreateRequest request) {
        validateNotArchivedByTaskId(taskId);
        // 检查正向关联是否已存在（A→B）
        HfTaskRelation existing = hfTaskRelationRepository.selectByTaskIdAndRelatedTaskIdAndType(
                taskId, request.getRelatedTaskId(), request.getRelationType());
        if (existing != null) {
            throw new BusinessException(ResultCode.TASK_RELATION_EXISTS);
        }
        // 检查反向关联是否已存在（B→A），双向互关联只需一条记录
        HfTaskRelation reverseExisting = hfTaskRelationRepository.selectByRelatedTaskIdAndTaskIdAndType(
                request.getRelatedTaskId(), taskId, request.getRelationType());
        if (reverseExisting != null) {
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
        Page<HfTask> pageResult = hfTaskRepository.selectPageByMine(new Page<>(condition.getPage(), condition.getPageSize()), condition);
        return PageUtils.convertPage(pageResult, this::toTaskVO);
    }

    @Override
    public Page<TaskVO> listPendingReviewTasks(TaskCondition condition) {
        Long userId = condition.getAssigneeId();
        // 只有开发工程师可以审查，其他角色返回空
        SysUser user = sysUserRepository.selectById(userId);
        if (user == null || user.getPositionId() == null) {
            return new Page<>(condition.getPage(), condition.getPageSize());
        }
        HfPosition position = hfPositionRepository.selectById(user.getPositionId());
        if (position == null || !"DEV".equals(position.getCode())) {
            return new Page<>(condition.getPage(), condition.getPageSize());
        }
        // 查询当前用户参与的项目
        List<HfProjectMember> members = hfProjectMemberRepository.selectByUserId(userId);
        List<Long> projectIds = members.stream()
                .map(HfProjectMember::getProjectId)
                .collect(Collectors.toList());
        if (projectIds.isEmpty()) {
            return new Page<>(condition.getPage(), condition.getPageSize());
        }
        Page<HfTask> pageResult = hfTaskRepository.selectPageByPendingReview(
                new Page<>(condition.getPage(), condition.getPageSize()), condition, projectIds);
        return PageUtils.convertPage(pageResult, this::toTaskVO);
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
                t -> TaskStatusEnum.DONE.matches(t.getStatus())
                        || TaskStatusEnum.CLOSED.matches(t.getStatus())
                        || TaskStatusEnum.CANCELLED.matches(t.getStatus()));
        if (allDone && !subTasks.isEmpty()) {
            if (TaskStatusEnum.TODO.matches(parent.getStatus())) {
                parent.setStatus(TaskStatusEnum.DONE.getCode());
                hfTaskRepository.updateById(parent);
            } else if (TaskStatusEnum.IN_PROGRESS.matches(parent.getStatus())) {
                parent.setStatus(TaskStatusEnum.IN_REVIEW.getCode());
                hfTaskRepository.updateById(parent);
            }
        }
    }

    private TaskVO toTaskVO(HfTask task) {
        TaskVO vo = taskMapper.toTaskVO(task);
        fillUserNames(vo, task);
        return vo;
    }

    private List<TaskVO> toTaskVOList(List<HfTask> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return List.of();
        }
        Set<Long> userIds = tasks.stream()
                .flatMap(t -> Stream.of(t.getAssigneeId(), t.getReporterId(), t.getDeveloperId(), t.getTesterId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, SysUser> userMap = sysUserRepository.selectByIds(userIds)
                .stream().collect(Collectors.toMap(SysUser::getId, u -> u));
        return tasks.stream().map(task -> {
            TaskVO vo = taskMapper.toTaskVO(task);
            fillUserNamesFromMap(vo, task, userMap);
            return vo;
        }).collect(Collectors.toList());
    }

    private void fillUserNames(TaskVO vo, HfTask task) {
        if (task.getAssigneeId() != null) {
            vo.setAssigneeName(sysUserRepository.getDisplayName(task.getAssigneeId()));
        }
        if (task.getReporterId() != null) {
            vo.setReporterName(sysUserRepository.getDisplayName(task.getReporterId()));
        }
        if (task.getDeveloperId() != null) {
            vo.setDeveloperName(sysUserRepository.getDisplayName(task.getDeveloperId()));
        }
        if (task.getTesterId() != null) {
            vo.setTesterName(sysUserRepository.getDisplayName(task.getTesterId()));
        }
    }

    private void fillUserNamesFromMap(TaskVO vo, HfTask task, Map<Long, SysUser> userMap) {
        if (task.getAssigneeId() != null) {
            SysUser assignee = userMap.get(task.getAssigneeId());
            if (assignee != null) {
                vo.setAssigneeName(assignee.getNickname() != null ? assignee.getNickname() : assignee.getUsername());
            }
        }
        if (task.getReporterId() != null) {
            SysUser reporter = userMap.get(task.getReporterId());
            if (reporter != null) {
                vo.setReporterName(reporter.getNickname() != null ? reporter.getNickname() : reporter.getUsername());
            }
        }
        if (task.getDeveloperId() != null) {
            SysUser developer = userMap.get(task.getDeveloperId());
            if (developer != null) {
                vo.setDeveloperName(developer.getNickname() != null ? developer.getNickname() : developer.getUsername());
            }
        }
        if (task.getTesterId() != null) {
            SysUser tester = userMap.get(task.getTesterId());
            if (tester != null) {
                vo.setTesterName(tester.getNickname() != null ? tester.getNickname() : tester.getUsername());
            }
        }
    }

    private HfTask getTaskOrThrow(Long id) {
        HfTask task = hfTaskRepository.selectById(id);
        if (task == null) {
            throw new BusinessException(ResultCode.TASK_NOT_FOUND);
        }
        return task;
    }

    /**
     * 记录状态变更历史
     */
    private void recordStatusHistory(Long taskId, Long userId, Integer oldStatus,
                                     TaskStatusEnum newStatus, TaskActionEnum action) {
        taskHistoryService.recordHistory(TaskHistoryCommand.builder()
                .taskId(taskId).userId(userId).action(action)
                .field("status").oldValue(String.valueOf(oldStatus)).newValue(String.valueOf(newStatus.getCode())).build());
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
        // 项目经理不允许审查
        if ("PM".equals(position.getCode())) {
            throw new BusinessException(ResultCode.PM_CANNOT_REVIEW);
        }
        // 开发工程师审查自己的任务：仅当项目中只有自己一个开发工程师时允许
        if ("DEV".equals(position.getCode()) && task.getDeveloperId() != null && task.getDeveloperId().equals(userId)) {
            HfProject project = hfProjectRepository.selectById(task.getProjectId());
            if (project == null) {
                throw new BusinessException(ResultCode.CANNOT_REVIEW_OWN_TASK);
            }
            List<HfProjectMember> members = hfProjectMemberRepository.selectByProjectId(project.getId());
            Set<Long> memberUserIds = members.stream().map(HfProjectMember::getUserId).collect(Collectors.toSet());
            Map<Long, SysUser> memberUserMap = sysUserRepository.selectByIds(memberUserIds)
                    .stream().collect(Collectors.toMap(SysUser::getId, u -> u));
            Set<Long> memberPositionIds = memberUserMap.values().stream()
                    .map(SysUser::getPositionId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            Map<Long, HfPosition> memberPositionMap = memberPositionIds.isEmpty() ? Map.of()
                    : hfPositionRepository.selectByIds(memberPositionIds).stream()
                    .collect(Collectors.toMap(HfPosition::getId, p -> p));
            long devCount = members.stream().filter(m -> {
                SysUser member = memberUserMap.get(m.getUserId());
                if (member == null || member.getPositionId() == null) {
                    return false;
                }
                HfPosition memberPosition = memberPositionMap.get(member.getPositionId());
                return memberPosition != null && "DEV".equals(memberPosition.getCode());
            }).count();
            if (devCount > 1) {
                throw new BusinessException(ResultCode.CANNOT_REVIEW_OWN_TASK);
            }
        }
    }

    private void validateNotArchivedByTaskId(Long taskId) {
        HfTask task = hfTaskRepository.selectById(taskId);
        if (task != null) {
            hfProjectService.validateNotArchived(task.getProjectId());
        }
    }
}
