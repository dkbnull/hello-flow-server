package cn.wbnull.helloflow.data.mapper;

import cn.wbnull.helloflow.data.entity.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色关联Mapper接口
 *
 * @author null
 * @date 2026-05-26
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
}
