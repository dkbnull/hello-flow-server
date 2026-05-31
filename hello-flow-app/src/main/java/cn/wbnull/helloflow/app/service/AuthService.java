package cn.wbnull.helloflow.app.service;

import cn.wbnull.helloflow.app.dto.auth.LoginRequest;
import cn.wbnull.helloflow.app.dto.auth.LoginResponse;
import cn.wbnull.helloflow.app.dto.auth.RefreshTokenRequest;

/**
 * 认证服务接口
 *
 * @author null
 * @date 2026-05-26
 */
public interface AuthService {

    /**
     * 登录
     */
    LoginResponse login(LoginRequest request);

    /**
     * 刷新令牌
     */
    LoginResponse refreshToken(RefreshTokenRequest request);
}
