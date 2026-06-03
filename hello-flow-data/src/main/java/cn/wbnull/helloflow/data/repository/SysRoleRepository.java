package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.SysRole;
import cn.wbnull.helloflow.data.mapper.SysRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
public class SysRoleRepository extends BaseRepository<SysRoleMapper, SysRole> {

    private final SysRoleMapper sysRoleMapper;

    public SysRoleRepository(SysRoleMapper sysRoleMapper) {
        super(sysRoleMapper);
        this.sysRoleMapper = sysRoleMapper;
    }

    public List<SysRole> selectList() {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        return sysRoleMapper.selectList(wrapper);
    }

    public List<SysRole> selectByIds(List<Long> ids) {
        return sysRoleMapper.selectByIds(ids);
    }
}
