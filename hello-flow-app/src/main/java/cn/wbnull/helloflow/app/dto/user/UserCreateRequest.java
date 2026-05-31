package cn.wbnull.helloflow.app.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户创建请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class UserCreateRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String nickname;

    private String email;

    private String phone;

    @NotNull(message = "职位ID不能为空")
    private Long positionId;

    @NotNull(message = "角色ID不能为空")
    private Long roleId;
}
