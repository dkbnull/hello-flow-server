package cn.wbnull.helloflow.app.dto.auth;

import lombok.Data;

import java.util.List;

/**
 * 登录响应
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private UserInfoVO user;

    @Data
    public static class UserInfoVO {

        private Long id;
        private String username;
        private String nickname;
        private String avatar;
        private String positionCode;
        private List<String> roles;
    }
}
