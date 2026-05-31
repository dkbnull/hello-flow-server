package cn.wbnull.helloflow.data.condition;

import lombok.Data;

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

    private String dueDateStart;

    private String dueDateEnd;

    private String createdAtStart;

    private String createdAtEnd;

    private String keyword;

    private Integer page = 1;

    private Integer pageSize = 20;
}
