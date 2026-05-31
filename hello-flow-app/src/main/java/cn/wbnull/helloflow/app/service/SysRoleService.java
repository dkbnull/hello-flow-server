package cn.wbnull.helloflow.app.service;

import cn.wbnull.helloflow.app.dto.role.RoleCreateRequest;
import cn.wbnull.helloflow.app.dto.role.RoleUpdateRequest;
import cn.wbnull.helloflow.data.entity.SysRole;

import java.util.List;

/**
 * 角色服务接口
 *
 * @author null
 * @date 2026-05-26
 */
public interface SysRoleService {

    /**
     * 获取角色列表
     */
    List<SysRole> listRoles();

    /**
     * 创建角色
     */
    SysRole createRole(RoleCreateRequest request);

    /**
     * 更新角色
     */
    SysRole updateRole(Long id, RoleUpdateRequest request);
}
