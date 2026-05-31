package cn.wbnull.helloflow.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 任务实体
 *
 * @author null
 * @date 2026-05-26
 */
@Data
@TableName("hf_task")
public class HfTask {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long projectId;

    private String taskCode;

    private Integer taskSeq;

    private Long sprintId;

    private Long parentId;

    private Integer type;

    private String title;

    private String description;

    private Integer status;

    private Integer priority;

    private Long assigneeId;

    private Long reporterId;

    private Long developerId;

    private Long testerId;

    private LocalDate dueDate;

    private LocalDate planStartDate;

    private LocalDate planEndDate;

    private LocalDate actualStartDate;

    private LocalDate actualEndDate;

    private LocalDateTime closeDate;

    private String cancelReason;

    private String delayReason;

    private Integer isDelayed;

    @TableLogic
    private Integer isDeleted;

    private Long createdBy;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
