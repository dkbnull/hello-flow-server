package cn.wbnull.helloflow.app.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 任务状态流转请求
 *
 * @author null
 * @date 2026-06-05
 */
@Data
public class TaskTransitionRequest {

    /**
     * 目标状态（对应 TaskStatusEnum 的 code）
     * 2-进行中(start) 3-待审查(complete-dev) 4-待测试(review-pass)
     * 2-进行中(review-reject/test-reject) 1-未开始(reopen)
     * 5-已完成(test-pass) 6-已关闭(close) 7-取消(cancel)
     */
    @NotNull(message = "目标状态不能为空")
    private Integer targetStatus;

    /**
     * 取消原因（仅 cancel 时必填）
     */
    private String cancelReason;
}
