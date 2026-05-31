package cn.wbnull.helloflow.app.dto.user;

import lombok.Data;

/**
 * 个人信息更新请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class UserProfileUpdateRequest {

    private String nickname;

    private String email;

    private String phone;

    private String avatar;
}
