package cn.wbnull.helloflow.app.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 项目创建请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class ProjectCreateRequest {

    @NotBlank(message = "项目编码不能为空")
    private String code;

    @NotBlank(message = "项目名称不能为空")
    private String name;

    private String description;

    private Long pmId;

    private Long devLeadId;

    private Long testLeadId;
}
