package cn.wbnull.helloflow.app.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * 任务创建请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class TaskCreateRequest {

    @NotBlank(message = "任务标题不能为空")
    private String title;

    private String description;

    private Integer type;

    private Integer priority;

    private Long assigneeId;

    private Long developerId;

    private Long testerId;

    private LocalDate dueDate;

    private Long sprintId;

    private Long parentId;

    private Long relatedTaskId;

    private Integer relationType;
}
