package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.SysRole;
import cn.wbnull.helloflow.data.mapper.SysRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
@RequiredArgsConstructor
public class SysRoleRepository {

    private final SysRoleMapper sysRoleMapper;

    public List<SysRole> selectList() {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        return sysRoleMapper.selectList(wrapper);
    }

    public List<SysRole> selectByIds(List<Long> ids) {
        return sysRoleMapper.selectByIds(ids);
    }

    public SysRole selectById(Long id) {
        return sysRoleMapper.selectById(id);
    }

    public void insert(SysRole role) {
        sysRoleMapper.insert(role);
    }

    public void updateById(SysRole role) {
        sysRoleMapper.updateById(role);
    }
}
