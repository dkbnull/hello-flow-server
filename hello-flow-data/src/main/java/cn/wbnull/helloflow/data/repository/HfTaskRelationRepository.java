package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.HfTaskRelation;
import cn.wbnull.helloflow.data.mapper.HfTaskRelationMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务关联数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
public class HfTaskRelationRepository extends BaseRepository<HfTaskRelationMapper, HfTaskRelation> {

    private final HfTaskRelationMapper hfTaskRelationMapper;

    public HfTaskRelationRepository(HfTaskRelationMapper hfTaskRelationMapper) {
        super(hfTaskRelationMapper);
        this.hfTaskRelationMapper = hfTaskRelationMapper;
    }

    public List<HfTaskRelation> selectByTaskId(Long taskId) {
        LambdaQueryWrapper<HfTaskRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTaskRelation::getTaskId, taskId);
        return hfTaskRelationMapper.selectList(wrapper);
    }

    public List<HfTaskRelation> selectByRelatedTaskId(Long relatedTaskId) {
        LambdaQueryWrapper<HfTaskRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTaskRelation::getRelatedTaskId, relatedTaskId);
        return hfTaskRelationMapper.selectList(wrapper);
    }

    public HfTaskRelation selectByTaskIdAndRelatedTaskIdAndType(Long taskId, Long relatedTaskId, Integer relationType) {
        LambdaQueryWrapper<HfTaskRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTaskRelation::getTaskId, taskId)
                .eq(HfTaskRelation::getRelatedTaskId, relatedTaskId)
                .eq(HfTaskRelation::getRelationType, relationType);
        return hfTaskRelationMapper.selectOne(wrapper);
    }

    /**
     * 查询反向关联：relatedTaskId作为当前任务，taskId作为关联任务
     */
    public HfTaskRelation selectByRelatedTaskIdAndTaskIdAndType(Long relatedTaskId, Long taskId, Integer relationType) {
        LambdaQueryWrapper<HfTaskRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTaskRelation::getTaskId, relatedTaskId)
                .eq(HfTaskRelation::getRelatedTaskId, taskId)
                .eq(HfTaskRelation::getRelationType, relationType);
        return hfTaskRelationMapper.selectOne(wrapper);
    }
}
