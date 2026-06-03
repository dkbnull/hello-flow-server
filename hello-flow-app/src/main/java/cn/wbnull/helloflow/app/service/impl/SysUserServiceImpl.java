package cn.wbnull.helloflow.app.service.impl;

import cn.wbnull.helloflow.app.dto.user.*;
import cn.wbnull.helloflow.app.service.SysUserService;
import cn.wbnull.helloflow.common.exception.BusinessException;
import cn.wbnull.helloflow.common.model.ResultCode;
import cn.wbnull.helloflow.common.util.BeanCopyUtils;
import cn.wbnull.helloflow.data.entity.HfPosition;
import cn.wbnull.helloflow.data.entity.SysUser;
import cn.wbnull.helloflow.data.entity.SysUserRole;
import cn.wbnull.helloflow.data.repository.HfPositionRepository;
import cn.wbnull.helloflow.data.repository.SysUserRepository;
import cn.wbnull.helloflow.data.repository.SysUserRoleRepository;
import cn.wbnull.helloflow.data.util.PageUtils;
import cn.wbnull.helloflow.security.util.SecurityUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务实现
 *
 * @author null
 * @date 2026-05-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserRepository sysUserRepository;
    private final SysUserRoleRepository sysUserRoleRepository;
    private final HfPositionRepository hfPositionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO createUser(UserCreateRequest request) {
        SysUser existing = sysUserRepository.selectByUsername(request.getUsername());
        if (existing != null) {
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }
        SysUser user = new SysUser();
        BeanCopyUtils.copyNonNullProperties(request, user, "password");
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(1);
        sysUserRepository.insert(user);
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(request.getRoleId());
        sysUserRoleRepository.insert(userRole);
        log.info("创建用户：{}", user.getUsername());
        return toUserVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO updateUser(Long id, UserUpdateRequest request) {
        SysUser user = sysUserRepository.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        BeanCopyUtils.copyNonNullProperties(request, user);
        sysUserRepository.updateById(user);
        if (request.getRoleId() != null) {
            sysUserRoleRepository.deleteByUserId(id);
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(id);
            userRole.setRoleId(request.getRoleId());
            sysUserRoleRepository.insert(userRole);
        }
        log.info("更新用户：id={}", id);
        return toUserVO(user);
    }

    @Override
    public void updateUserStatus(Long id, UserStatusRequest request) {
        SysUser user = sysUserRepository.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        user.setStatus(request.getStatus());
        sysUserRepository.updateById(user);
        log.info("更新用户状态：id={}, status={}", id, request.getStatus());
    }

    @Override
    public Page<UserVO> listUsers(String keyword, Integer status, Long positionId, Integer page, Integer pageSize) {
        Page<SysUser> pageResult = sysUserRepository.selectPageByCondition(new Page<>(page, pageSize), keyword, status, positionId);
        return PageUtils.convertPage(pageResult, this::toUserVO);
    }

    @Override
    public UserVO getCurrentUser() {
        Long userId = SecurityUtils.getCurrentUserId();
        SysUser user = sysUserRepository.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return toUserVO(user);
    }

    @Override
    public UserVO updateProfile(UserProfileUpdateRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        SysUser user = sysUserRepository.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        BeanCopyUtils.copyNonNullProperties(request, user);
        sysUserRepository.updateById(user);
        log.info("更新个人信息：userId={}", userId);
        return toUserVO(user);
    }

    @Override
    public List<String> getUserRoleCodes(Long userId) {
        return sysUserRepository.selectRoleCodesByUserId(userId);
    }

    private UserVO toUserVO(SysUser user) {
        UserVO vo = new UserVO();
        BeanCopyUtils.copyNonNullProperties(user, vo);
        vo.setRoles(sysUserRepository.selectRoleCodesByUserId(user.getId()));
        if (user.getPositionId() != null) {
            HfPosition position = hfPositionRepository.selectById(user.getPositionId());
            if (position != null) {
                vo.setPositionName(position.getName());
                vo.setPositionCode(position.getCode());
            }
        }
        return vo;
    }
}
