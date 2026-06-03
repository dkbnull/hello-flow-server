package cn.wbnull.helloflow.data.mapper;

import cn.wbnull.helloflow.data.entity.HfProjectMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 项目成员Mapper接口
 *
 * @author null
 * @date 2026-05-26
 */
@Mapper
public interface HfProjectMemberMapper extends BaseMapper<HfProjectMember> {

    /**
     * 查询项目成员（忽略逻辑删除条件）
     */
    HfProjectMember selectIgnoreDeleted(@Param("projectId") Long projectId, @Param("userId") Long userId);

    /**
     * 恢复已逻辑删除的项目成员记录（忽略逻辑删除条件）
     */
    void restoreIgnoreDeleted(@Param("projectId") Long projectId, @Param("userId") Long userId);
}
