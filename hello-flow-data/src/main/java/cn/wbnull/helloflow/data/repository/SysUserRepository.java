package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.SysUser;
import cn.wbnull.helloflow.data.entity.SysUserRole;
import cn.wbnull.helloflow.data.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
public class SysUserRepository extends BaseRepository<SysUserMapper, SysUser> {

    private final SysUserMapper sysUserMapper;
    private final SysUserRoleRepository sysUserRoleRepository;
    private final SysRoleRepository sysRoleRepository;

    public SysUserRepository(SysUserMapper sysUserMapper,
                             SysUserRoleRepository sysUserRoleRepository,
                             SysRoleRepository sysRoleRepository) {
        super(sysUserMapper);
        this.sysUserMapper = sysUserMapper;
        this.sysUserRoleRepository = sysUserRoleRepository;
        this.sysRoleRepository = sysRoleRepository;
    }

    public List<SysUser> selectByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return sysUserMapper.selectByIds(ids);
    }

    public SysUser selectByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return sysUserMapper.selectOne(wrapper);
    }

    public Page<SysUser> selectPageByCondition(Page<SysUser> page, String keyword, Integer status, Long positionId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(SysUser::getUsername, keyword)
                    .or().like(SysUser::getNickname, keyword)
                    .or().like(SysUser::getEmail, keyword));
        }
        if (status != null) {
            wrapper.eq(SysUser::getStatus, status);
        }
        if (positionId != null) {
            wrapper.eq(SysUser::getPositionId, positionId);
        }
        wrapper.orderByDesc(SysUser::getCreatedAt);
        return sysUserMapper.selectPage(page, wrapper);
    }

    /**
     * 获取用户角色编码列表
     */
    public List<String> selectRoleCodesByUserId(Long userId) {
        List<SysUserRole> userRoles = sysUserRoleRepository.selectByUserId(userId);
        if (userRoles.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        return sysRoleRepository.selectByIds(roleIds).stream()
                .map(role -> role.getCode()).collect(Collectors.toList());
    }

    /**
     * 获取用户显示名称（nickname优先，username兜底）
     */
    public String getDisplayName(Long userId) {
        SysUser user = selectById(userId);
        return user != null ? (user.getNickname() != null ? user.getNickname() : user.getUsername()) : null;
    }
}
