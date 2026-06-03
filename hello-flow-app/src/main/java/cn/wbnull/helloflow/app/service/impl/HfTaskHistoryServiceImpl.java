package cn.wbnull.helloflow.app.service.impl;

import cn.wbnull.helloflow.app.dto.task.TaskHistoryCommand;
import cn.wbnull.helloflow.app.dto.task.TaskHistoryVO;
import cn.wbnull.helloflow.app.service.HfTaskHistoryService;
import cn.wbnull.helloflow.common.util.BeanCopyUtils;
import cn.wbnull.helloflow.data.entity.HfTaskHistory;
import cn.wbnull.helloflow.data.entity.SysUser;
import cn.wbnull.helloflow.data.enums.TaskActionEnum;
import cn.wbnull.helloflow.data.enums.TaskPriorityEnum;
import cn.wbnull.helloflow.data.enums.TaskStatusEnum;
import cn.wbnull.helloflow.data.repository.HfTaskHistoryRepository;
import cn.wbnull.helloflow.data.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务历史服务实现
 *
 * @author null
 * @date 2026-05-26
 */
@Service
@RequiredArgsConstructor
public class HfTaskHistoryServiceImpl implements HfTaskHistoryService {

    private final HfTaskHistoryRepository hfTaskHistoryRepository;
    private final SysUserRepository sysUserRepository;

    @Override
    public List<TaskHistoryVO> listHistories(Long taskId) {
        List<HfTaskHistory> histories = hfTaskHistoryRepository.selectByTaskId(taskId);
        return histories.stream().map(this::toHistoryVO).collect(Collectors.toList());
    }

    @Override
    public void recordHistory(TaskHistoryCommand command) {
        HfTaskHistory history = new HfTaskHistory();
        BeanCopyUtils.copyNonNullProperties(command, history);
        history.setAction(command.getAction().getCode());
        hfTaskHistoryRepository.insert(history);
    }

    private TaskHistoryVO toHistoryVO(HfTaskHistory history) {
        TaskHistoryVO vo = new TaskHistoryVO();
        BeanCopyUtils.copyNonNullProperties(history, vo);

        String actionName = history.getAction();
        try {
            TaskActionEnum actionEnum = TaskActionEnum.valueOf(history.getAction());
            actionName = actionEnum.getName();
            vo.setAction(actionName);
        } catch (IllegalArgumentException ignored) {
        }

        SysUser user = sysUserRepository.selectById(history.getUserId());
        if (user != null) {
            vo.setUsername(sysUserRepository.getDisplayName(history.getUserId()));
        }

        vo.setDescription(buildDescription(actionName, history.getField(),
                history.getOldValue(), history.getNewValue()));
        return vo;
    }

    private String buildDescription(String actionName, String field, String oldValue, String newValue) {
        if (field == null) {
            return actionName;
        }
        String fieldName = translateFieldName(field);
        String oldDisplay = translateFieldValue(field, oldValue);
        String newDisplay = translateFieldValue(field, newValue);
        return fieldName + "：" + oldDisplay + " → " + newDisplay;
    }

    private String translateFieldName(String field) {
        switch (field) {
            case "status":
                return "状态";
            case "priority":
                return "优先级";
            case "assigneeId":
                return "负责人";
            case "isDelayed":
                return "延期标记";
            default:
                return field;
        }
    }

    private String translateFieldValue(String field, String value) {
        if (value == null) {
            return "无";
        }
        switch (field) {
            case "status":
                try {
                    return TaskStatusEnum.fromCode(Integer.parseInt(value)).getName();
                } catch (Exception e) {
                    return value;
                }
            case "priority":
                for (TaskPriorityEnum p : TaskPriorityEnum.values()) {
                    if (String.valueOf(p.getCode()).equals(value)) {
                        return p.getName();
                    }
                }
                return value;
            case "assigneeId":
                try {
                    String displayName = sysUserRepository.getDisplayName(Long.parseLong(value));
                    if (displayName != null) {
                        return displayName;
                    }
                } catch (Exception ignored) {
                }
                return value;
            case "isDelayed":
                return "1".equals(value) ? "是" : "否";
            default:
                return value;
        }
    }
}
