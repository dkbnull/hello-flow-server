package cn.wbnull.helloflow.app.service.impl;

import cn.wbnull.helloflow.app.dto.auth.LoginRequest;
import cn.wbnull.helloflow.app.dto.auth.LoginResponse;
import cn.wbnull.helloflow.app.dto.auth.RefreshTokenRequest;
import cn.wbnull.helloflow.app.service.AuthService;
import cn.wbnull.helloflow.common.exception.BusinessException;
import cn.wbnull.helloflow.common.model.ResultCode;
import cn.wbnull.helloflow.data.entity.HfPosition;
import cn.wbnull.helloflow.data.entity.SysUser;
import cn.wbnull.helloflow.data.repository.HfPositionRepository;
import cn.wbnull.helloflow.data.repository.SysUserRepository;
import cn.wbnull.helloflow.security.config.JwtProperties;
import cn.wbnull.helloflow.security.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 认证服务实现
 *
 * @author null
 * @date 2026-05-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserRepository sysUserRepository;
    private final HfPositionRepository hfPositionRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        SysUser user = sysUserRepository.selectByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.LOGIN_FAILED);
        }
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.ACCOUNT_DISABLED);
        }

        List<String> roles = sysUserRepository.selectRoleCodesByUserId(user.getId());

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername(), roles);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(jwtProperties.getAccessTokenExpiration());

        LoginResponse.UserInfoVO userInfo = new LoginResponse.UserInfoVO();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setRoles(roles);
        if (user.getPositionId() != null) {
            HfPosition position = hfPositionRepository.selectById(user.getPositionId());
            if (position != null) {
                userInfo.setPositionCode(position.getCode());
            }
        }
        response.setUser(userInfo);

        log.info("用户 {} 登录成功", user.getUsername());
        return response;
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }
        Long userId = jwtTokenProvider.getUserIdFromToken(request.getRefreshToken());
        SysUser user = sysUserRepository.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        List<String> roles = sysUserRepository.selectRoleCodesByUserId(userId);
        String accessToken = jwtTokenProvider.generateAccessToken(userId, user.getUsername(), roles);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(jwtProperties.getAccessTokenExpiration());
        log.info("刷新令牌：userId={}", userId);
        return response;
    }
}
