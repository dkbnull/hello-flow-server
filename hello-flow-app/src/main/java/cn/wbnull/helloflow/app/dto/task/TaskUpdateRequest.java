package cn.wbnull.helloflow.app.dto.task;

import lombok.Data;

/**
 * 任务更新请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class TaskUpdateRequest {

    private String title;

    private String description;

    private Integer type;

    private Integer priority;

    private String dueDate;

    private String planStartDate;

    private String planEndDate;

    private Long sprintId;
}
