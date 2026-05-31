package cn.wbnull.helloflow.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务操作枚举
 *
 * @author null
 * @date 2026-05-26
 */
@Getter
@AllArgsConstructor
public enum TaskActionEnum {

    CREATE("CREATE", "创建"),
    ASSIGN("ASSIGN", "分配"),
    STATUS_CHANGE("STATUS_CHANGE", "状态变更"),
    PRIORITY_CHANGE("PRIORITY_CHANGE", "优先级变更"),
    UPDATE("UPDATE", "更新"),
    DELETE("DELETE", "删除"),
    DELAY("DELAY", "标记延期"),
    CANCEL("CANCEL", "取消"),
    REOPEN("REOPEN", "重新打开"),
    CLOSE("CLOSE", "关闭"),
    TEST_REJECT("TEST_REJECT", "测试不通过");

    private final String code;
    private final String name;
}
