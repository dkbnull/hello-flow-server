package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.SysUserRole;
import cn.wbnull.helloflow.data.mapper.SysUserRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关联数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
@RequiredArgsConstructor
public class SysUserRoleRepository {

    private final SysUserRoleMapper sysUserRoleMapper;

    public List<SysUserRole> selectByUserId(Long userId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        return sysUserRoleMapper.selectList(wrapper);
    }

    public void insert(SysUserRole userRole) {
        sysUserRoleMapper.insert(userRole);
    }

    public void deleteByUserId(Long userId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        sysUserRoleMapper.delete(wrapper);
    }
}
