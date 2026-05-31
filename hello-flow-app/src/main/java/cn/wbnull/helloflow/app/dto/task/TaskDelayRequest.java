package cn.wbnull.helloflow.app.dto.task;

import lombok.Data;

/**
 * 任务延期请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class TaskDelayRequest {

    private String delayReason;
}
