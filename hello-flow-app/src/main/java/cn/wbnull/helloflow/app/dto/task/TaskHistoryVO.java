package cn.wbnull.helloflow.app.dto.task;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务历史数据
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class TaskHistoryVO {

    private Long id;
    private Long taskId;
    private Long userId;
    private String username;
    private String action;
    private String field;
    private String oldValue;
    private String newValue;
    private String description;
    private LocalDateTime createdAt;
}
