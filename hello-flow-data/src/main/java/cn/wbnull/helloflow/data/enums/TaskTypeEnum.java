package cn.wbnull.helloflow.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务类型枚举
 *
 * @author null
 * @date 2026-05-26
 */
@Getter
@AllArgsConstructor
public enum TaskTypeEnum {

    REQUIREMENT(1, "需求"),
    IMPROVEMENT(2, "完善"),
    DEFECT(3, "缺陷");

    private final int code;
    private final String name;
}
