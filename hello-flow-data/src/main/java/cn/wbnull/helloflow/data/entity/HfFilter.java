package cn.wbnull.helloflow.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 过滤器实体
 *
 * @author null
 * @date 2026-05-26
 */
@Data
@TableName("hf_filter")
public class HfFilter {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String name;

    private String conditions;

    private Integer isDefault;

    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
