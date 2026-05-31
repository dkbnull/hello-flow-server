package cn.wbnull.helloflow.app.service.impl;

import cn.wbnull.helloflow.app.dto.project.ProjectStatsVO;
import cn.wbnull.helloflow.app.dto.stats.BoardVO;
import cn.wbnull.helloflow.app.dto.stats.BurndownVO;
import cn.wbnull.helloflow.app.dto.stats.DefectStatsVO;
import cn.wbnull.helloflow.app.dto.stats.MemberWorkloadVO;
import cn.wbnull.helloflow.app.service.BoardStatsService;
import cn.wbnull.helloflow.common.util.BeanCopyUtils;
import cn.wbnull.helloflow.data.entity.HfTask;
import cn.wbnull.helloflow.data.enums.TaskStatusEnum;
import cn.wbnull.helloflow.data.repository.HfTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 看板与统计服务实现
 *
 * @author null
 * @date 2026-05-30
 */
@Service
@RequiredArgsConstructor
public class BoardStatsServiceImpl implements BoardStatsService {

    private final HfTaskRepository hfTaskRepository;

    @Override
    public BoardVO getProjectBoard(Long projectId) {
        List<HfTask> tasks = hfTaskRepository.selectTopLevelByProjectId(projectId);
        return buildBoard(tasks);
    }

    @Override
    public BoardVO getSprintBoard(Long sprintId) {
        List<HfTask> tasks = hfTaskRepository.selectTopLevelBySprintId(sprintId);
        return buildBoard(tasks);
    }

    @Override
    public ProjectStatsVO getProjectStats(Long projectId) {
        List<HfTask> tasks = hfTaskRepository.selectByProjectId(projectId);

        ProjectStatsVO stats = new ProjectStatsVO();
        stats.setTotalTasks((long) tasks.size());

        Map<Integer, Long> statusDist = tasks.stream()
                .collect(Collectors.groupingBy(HfTask::getStatus, Collectors.counting()));
        stats.setStatusDistribution(statusDist);

        long completed = statusDist.getOrDefault(TaskStatusEnum.DONE.getCode(), 0L)
                + statusDist.getOrDefault(TaskStatusEnum.CLOSED.getCode(), 0L);
        stats.setCompletedTasks(completed);
        stats.setCompletionRate(tasks.isEmpty() ? 0.0 : (double) completed / tasks.size() * 100);

        return stats;
    }

    @Override
    public BurndownVO getBurndown(Long projectId, Long sprintId) {
        BurndownVO burndown = new BurndownVO();
        List<HfTask> tasks = hfTaskRepository.selectByProjectIdAndSprintId(projectId, sprintId);
        burndown.setTotalPoints(tasks.size());
        return burndown;
    }

    @Override
    public List<MemberWorkloadVO> getMemberWorkload(Long projectId) {
        List<HfTask> tasks = hfTaskRepository.selectByProjectId(projectId);

        Map<Long, List<HfTask>> byAssignee = tasks.stream()
                .filter(t -> t.getAssigneeId() != null)
                .collect(Collectors.groupingBy(HfTask::getAssigneeId));

        return byAssignee.entrySet().stream().map(entry -> {
            MemberWorkloadVO vo = new MemberWorkloadVO();
            vo.setUserId(entry.getKey());
            vo.setTotalTasks((long) entry.getValue().size());
            vo.setCompletedTasks(entry.getValue().stream()
                    .filter(t -> t.getStatus() == TaskStatusEnum.DONE.getCode()
                            || t.getStatus() == TaskStatusEnum.CLOSED.getCode())
                    .count());
            vo.setInProgressTasks(entry.getValue().stream()
                    .filter(t -> t.getStatus() == TaskStatusEnum.IN_PROGRESS.getCode())
                    .count());
            vo.setTodoTasks(entry.getValue().stream()
                    .filter(t -> t.getStatus() == TaskStatusEnum.TODO.getCode())
                    .count());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public DefectStatsVO getDefectStats(Long projectId) {
        List<HfTask> defects = hfTaskRepository.selectByProjectIdAndType(projectId, 3);

        DefectStatsVO stats = new DefectStatsVO();
        stats.setTotalDefects((long) defects.size());
        stats.setOpenDefects(defects.stream()
                .filter(t -> t.getStatus() != TaskStatusEnum.DONE.getCode()
                        && t.getStatus() != TaskStatusEnum.CLOSED.getCode()
                        && t.getStatus() != TaskStatusEnum.CANCELLED.getCode())
                .count());
        stats.setClosedDefects(defects.stream()
                .filter(t -> t.getStatus() == TaskStatusEnum.DONE.getCode()
                        || t.getStatus() == TaskStatusEnum.CLOSED.getCode())
                .count());
        stats.setFixRate(defects.isEmpty() ? 0.0 : (double) stats.getClosedDefects() / defects.size() * 100);

        return stats;
    }

    private BoardVO buildBoard(List<HfTask> tasks) {
        BoardVO board = new BoardVO();
        Map<Integer, List<HfTask>> grouped = tasks.stream()
                .collect(Collectors.groupingBy(HfTask::getStatus));

        List<BoardVO.BoardColumn> columns = new ArrayList<>();
        for (TaskStatusEnum status : TaskStatusEnum.values()) {
            BoardVO.BoardColumn column = new BoardVO.BoardColumn();
            column.setStatus(status.getCode());
            column.setStatusName(status.getName());
            column.setTasks(grouped.getOrDefault(status.getCode(), Collections.emptyList()).stream()
                    .map(t -> {
                        BoardVO.BoardTask bt = new BoardVO.BoardTask();
                        BeanCopyUtils.copyNonNullProperties(t, bt, "dueDate");
                        bt.setDueDate(t.getDueDate() != null ? t.getDueDate().toString() : null);
                        return bt;
                    }).collect(Collectors.toList()));
            columns.add(column);
        }
        board.setColumns(columns);
        return board;
    }
}
