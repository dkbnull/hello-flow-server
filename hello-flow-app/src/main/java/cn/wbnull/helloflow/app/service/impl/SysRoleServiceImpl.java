package cn.wbnull.helloflow.app.service.impl;

import cn.wbnull.helloflow.app.dto.role.RoleCreateRequest;
import cn.wbnull.helloflow.app.dto.role.RoleUpdateRequest;
import cn.wbnull.helloflow.app.service.SysRoleService;
import cn.wbnull.helloflow.common.exception.BusinessException;
import cn.wbnull.helloflow.common.model.ResultCode;
import cn.wbnull.helloflow.common.util.BeanCopyUtils;
import cn.wbnull.helloflow.data.entity.SysRole;
import cn.wbnull.helloflow.data.repository.SysRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色服务实现
 *
 * @author null
 * @date 2026-05-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleRepository sysRoleRepository;

    @Override
    public List<SysRole> listRoles() {
        return sysRoleRepository.selectList();
    }

    @Override
    public SysRole createRole(RoleCreateRequest request) {
        SysRole role = new SysRole();
        BeanCopyUtils.copyNonNullProperties(request, role);
        role.setStatus(1);
        sysRoleRepository.insert(role);
        log.info("创建角色：code={}, name={}", role.getCode(), role.getName());
        return role;
    }

    @Override
    public SysRole updateRole(Long id, RoleUpdateRequest request) {
        SysRole role = sysRoleRepository.selectById(id);
        if (role == null) {
            throw new BusinessException(ResultCode.ROLE_NOT_FOUND);
        }
        BeanCopyUtils.copyNonNullProperties(request, role);
        sysRoleRepository.updateById(role);
        log.info("更新角色：id={}", id);
        return role;
    }
}
