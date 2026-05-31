package cn.wbnull.helloflow.data.enums;

import cn.wbnull.helloflow.common.exception.BusinessException;
import cn.wbnull.helloflow.common.model.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态枚举
 *
 * @author null
 * @date 2026-05-26
 */
@Getter
@AllArgsConstructor
public enum TaskStatusEnum {

    TODO(1, "未开始"),
    IN_PROGRESS(2, "进行中"),
    IN_REVIEW(3, "待评审"),
    IN_TEST(4, "待测试"),
    DONE(5, "已完成"),
    CLOSED(6, "已关闭"),
    CANCELLED(7, "取消");

    private final int code;
    private final String name;

    public static TaskStatusEnum fromCode(int code) {
        for (TaskStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new BusinessException(ResultCode.PARAM_VALIDATION_FAILED, "无效的任务状态：" + code);
    }
}
