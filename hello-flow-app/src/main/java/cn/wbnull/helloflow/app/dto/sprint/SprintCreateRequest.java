package cn.wbnull.helloflow.app.dto.sprint;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 迭代创建请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class SprintCreateRequest {

    @NotBlank(message = "Sprint名称不能为空")
    private String name;

    private String goal;

    @NotBlank(message = "Sprint开始日期不能为空")
    private String startDate;

    @NotBlank(message = "Sprint结束日期不能为空")
    private String endDate;
}
