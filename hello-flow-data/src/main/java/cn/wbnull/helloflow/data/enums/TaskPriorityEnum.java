package cn.wbnull.helloflow.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务优先级枚举
 *
 * @author null
 * @date 2026-05-26
 */
@Getter
@AllArgsConstructor
public enum TaskPriorityEnum {

    LOWEST(1, "最低"),
    LOW(2, "低"),
    MEDIUM(3, "中"),
    HIGH(4, "高"),
    HIGHEST(5, "最高");

    private final int code;
    private final String name;
}
