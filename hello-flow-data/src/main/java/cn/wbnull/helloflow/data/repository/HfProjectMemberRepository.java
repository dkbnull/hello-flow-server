package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.HfProjectMember;
import cn.wbnull.helloflow.data.mapper.HfProjectMemberMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 项目成员数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
public class HfProjectMemberRepository extends BaseRepository<HfProjectMemberMapper, HfProjectMember> {

    private final HfProjectMemberMapper hfProjectMemberMapper;

    public HfProjectMemberRepository(HfProjectMemberMapper hfProjectMemberMapper) {
        super(hfProjectMemberMapper);
        this.hfProjectMemberMapper = hfProjectMemberMapper;
    }

    public List<HfProjectMember> selectByUserId(Long userId) {
        LambdaQueryWrapper<HfProjectMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfProjectMember::getUserId, userId);
        return hfProjectMemberMapper.selectList(wrapper);
    }

    public List<HfProjectMember> selectByProjectId(Long projectId) {
        LambdaQueryWrapper<HfProjectMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfProjectMember::getProjectId, projectId);
        return hfProjectMemberMapper.selectList(wrapper);
    }

    public HfProjectMember selectByProjectIdAndUserId(Long projectId, Long userId) {
        LambdaQueryWrapper<HfProjectMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfProjectMember::getProjectId, projectId)
                .eq(HfProjectMember::getUserId, userId);
        return hfProjectMemberMapper.selectOne(wrapper);
    }

    /**
     * 查询项目成员（忽略逻辑删除条件），用于判断是否存在已删除记录
     */
    public HfProjectMember selectByProjectIdAndUserIdIgnoreDeleted(Long projectId, Long userId) {
        return hfProjectMemberMapper.selectIgnoreDeleted(projectId, userId);
    }

    /**
     * 恢复已逻辑删除的项目成员记录
     */
    public void restoreByProjectIdAndUserId(Long projectId, Long userId) {
        hfProjectMemberMapper.restoreIgnoreDeleted(projectId, userId);
    }

    public void deleteByProjectIdAndUserId(Long projectId, Long userId) {
        LambdaQueryWrapper<HfProjectMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfProjectMember::getProjectId, projectId)
                .eq(HfProjectMember::getUserId, userId);
        hfProjectMemberMapper.delete(wrapper);
    }
}
