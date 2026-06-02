package cn.wbnull.helloflow.app.dto.task;

import lombok.Data;

import java.time.LocalDate;

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

    private LocalDate dueDate;

    private LocalDate planStartDate;

    private LocalDate planEndDate;

    private Long sprintId;
}
