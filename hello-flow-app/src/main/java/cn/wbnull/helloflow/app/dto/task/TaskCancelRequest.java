package cn.wbnull.helloflow.app.dto.task;

import lombok.Data;

/**
 * 任务取消请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class TaskCancelRequest {

    private String cancelReason;
}
