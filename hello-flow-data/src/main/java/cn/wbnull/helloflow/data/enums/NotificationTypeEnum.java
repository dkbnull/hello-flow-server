package cn.wbnull.helloflow.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知类型枚举
 *
 * @author null
 * @date 2026-05-26
 */
@Getter
@AllArgsConstructor
public enum NotificationTypeEnum {

    TASK_ASSIGN(1, "任务分配"),
    STATUS_CHANGE(2, "状态变更"),
    REOPEN(3, "重新打开"),
    COMMENT(4, "评论"),
    SYSTEM(5, "系统通知");

    private final int code;
    private final String name;
}
