package cn.wbnull.helloflow.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 项目成员实体
 *
 * @author null
 * @date 2026-05-26
 */
@Data
@TableName("hf_project_member")
public class HfProjectMember {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long projectId;

    private Long userId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime joinedAt;

    @TableLogic
    private Integer isDeleted;
}
