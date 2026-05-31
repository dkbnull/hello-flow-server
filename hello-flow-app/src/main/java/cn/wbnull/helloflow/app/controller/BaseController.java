package cn.wbnull.helloflow.app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 控制器基类，提供当前登录用户信息获取等公共方法
 *
 * @author null
 * @date 2026-05-30
 */
public abstract class BaseController {

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID
     */
    protected Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }

    /**
     * 判断当前用户是否为管理员
     *
     * @return 是否管理员
     */
    protected boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()));
    }
}
