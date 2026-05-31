package cn.wbnull.helloflow.app.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 任务关联创建请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class TaskRelationCreateRequest {

    @NotNull(message = "关联任务ID不能为空")
    private Long relatedTaskId;

    @NotNull(message = "关联类型不能为空")
    private Integer relationType;
}
