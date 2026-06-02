package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.SysUser;
import cn.wbnull.helloflow.data.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 用户数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
@RequiredArgsConstructor
public class SysUserRepository {

    private final SysUserMapper sysUserMapper;

    public SysUser selectById(Long id) {
        return sysUserMapper.selectById(id);
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

    public void insert(SysUser user) {
        sysUserMapper.insert(user);
    }

    public void updateById(SysUser user) {
        sysUserMapper.updateById(user);
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
}
