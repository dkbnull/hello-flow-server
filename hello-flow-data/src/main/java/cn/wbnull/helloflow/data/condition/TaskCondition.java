package cn.wbnull.helloflow.data.condition;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 任务查询条件
 *
 * @author null
 * @date 2026-05-30
 */
@Data
public class TaskCondition {

    private Long projectId;

    private Integer status;

    private Integer type;

    private Integer priority;

    private Long assigneeId;

    private Long reporterId;

    private Long sprintId;

    private Integer isDelayed;

    private LocalDate dueDateStart;

    private LocalDate dueDateEnd;

    private LocalDateTime createdAtStart;

    private LocalDateTime createdAtEnd;

    private String keyword;

    private Integer page = 1;

    private Integer pageSize = 10;
}
