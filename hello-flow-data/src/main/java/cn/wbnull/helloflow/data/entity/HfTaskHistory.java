package cn.wbnull.helloflow.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务历史实体
 *
 * @author null
 * @date 2026-05-26
 */
@Data
@TableName("hf_task_history")
public class HfTaskHistory {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long taskId;

    private Long userId;

    private String action;

    private String field;

    private String oldValue;

    private String newValue;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
