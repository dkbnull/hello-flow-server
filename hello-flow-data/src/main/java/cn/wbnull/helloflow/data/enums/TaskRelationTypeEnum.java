package cn.wbnull.helloflow.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务关联类型枚举
 *
 * @author null
 * @date 2026-05-26
 */
@Getter
@AllArgsConstructor
public enum TaskRelationTypeEnum {

    RELATED(1, "关联"),
    DEPENDS(2, "依赖"),
    DUPLICATE(3, "重复");

    private final int code;
    private final String name;
}
