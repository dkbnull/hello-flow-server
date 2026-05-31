package cn.wbnull.helloflow.app.dto.user;

import lombok.Data;

/**
 * 用户更新请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class UserUpdateRequest {

    private String nickname;

    private String email;

    private String phone;

    private Long positionId;

    private Long roleId;
}
