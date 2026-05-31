package cn.wbnull.helloflow.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务关联实体
 *
 * @author null
 * @date 2026-05-26
 */
@Data
@TableName("hf_task_relation")
public class HfTaskRelation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long taskId;

    private Long relatedTaskId;

    private Integer relationType;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
