package cn.wbnull.helloflow.app.dto.role;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 角色创建请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class RoleCreateRequest {

    @NotBlank(message = "角色名称不能为空")
    private String name;

    @NotBlank(message = "角色编码不能为空")
    private String code;

    private String description;
}
