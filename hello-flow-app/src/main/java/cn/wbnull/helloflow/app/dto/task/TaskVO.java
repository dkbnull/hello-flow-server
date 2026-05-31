package cn.wbnull.helloflow.app.dto.task;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务数据
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class TaskVO {

    private Long id;
    private Long projectId;
    private String taskCode;
    private Long sprintId;
    private Long parentId;
    private Integer type;
    private String title;
    private String description;
    private Integer status;
    private Integer priority;
    private Long assigneeId;
    private String assigneeName;
    private Long reporterId;
    private String reporterName;
    private Long developerId;
    private String developerName;
    private Long testerId;
    private String testerName;
    private LocalDate dueDate;
    private LocalDate planStartDate;
    private LocalDate planEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private LocalDateTime closeDate;
    private Integer isDelayed;
    private String delayReason;
    private String cancelReason;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TaskVO> subTasks;
    private List<TaskRelationVO> relations;

    @Data
    public static class TaskRelationVO {

        private Long id;
        private Long relatedTaskId;
        private String relatedTaskTitle;
        private Integer relationType;
    }
}
