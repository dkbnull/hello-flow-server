package cn.wbnull.helloflow.app.dto.task;

import cn.wbnull.helloflow.data.enums.TaskActionEnum;
import lombok.Builder;
import lombok.Data;

/**
 * 任务历史记录命令
 *
 * @author null
 * @date 2026-05-30
 */
@Data
@Builder
public class TaskHistoryCommand {

    private Long taskId;

    private Long userId;

    private TaskActionEnum action;

    private String field;

    private String oldValue;

    private String newValue;
}
