package cn.wbnull.helloflow.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 迭代实体
 *
 * @author null
 * @date 2026-05-26
 */
@Data
@TableName("hf_sprint")
public class HfSprint {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long projectId;

    private String name;

    private String goal;

    private Integer status;

    private LocalDate startDate;

    private LocalDate endDate;

    @TableLogic
    private Integer isDeleted;

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
