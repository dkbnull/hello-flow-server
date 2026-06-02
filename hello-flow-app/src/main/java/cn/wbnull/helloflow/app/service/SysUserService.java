package cn.wbnull.helloflow.app.service;

import cn.wbnull.helloflow.app.dto.user.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author null
 * @date 2026-05-26
 */
public interface SysUserService {

    /**
     * 创建用户
     */
    UserVO createUser(UserCreateRequest request);

    /**
     * 更新用户
     */
    UserVO updateUser(Long id, UserUpdateRequest request);

    /**
     * 更新用户状态
     */
    void updateUserStatus(Long id, UserStatusRequest request);

    /**
     * 获取用户列表
     */
    Page<UserVO> listUsers(String keyword, Integer status, Long positionId, Integer page, Integer pageSize);

    /**
     * 获取当前用户信息
     */
    UserVO getCurrentUser();

    /**
     * 更新个人信息
     */
    UserVO updateProfile(UserProfileUpdateRequest request);

    /**
     * 获取用户角色编码列表
     */
    List<String> getUserRoleCodes(Long userId);
}