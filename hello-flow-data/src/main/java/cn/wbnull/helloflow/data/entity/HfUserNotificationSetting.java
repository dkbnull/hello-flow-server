package cn.wbnull.helloflow.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户通知设置实体
 *
 * @author null
 * @date 2026-05-26
 */
@Data
@TableName("hf_user_notification_setting")
public class HfUserNotificationSetting {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Integer emailEnabled;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
