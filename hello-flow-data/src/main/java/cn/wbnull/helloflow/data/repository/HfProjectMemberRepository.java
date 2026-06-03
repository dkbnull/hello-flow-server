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

    public void deleteByProjectIdAndUserId(Long projectId, Long userId) {
        LambdaQueryWrapper<HfProjectMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfProjectMember::getProjectId, projectId)
                .eq(HfProjectMember::getUserId, userId);
        hfProjectMemberMapper.delete(wrapper);
    }
}
