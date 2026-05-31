package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.HfTaskRelation;
import cn.wbnull.helloflow.data.mapper.HfTaskRelationMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务关联数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
@RequiredArgsConstructor
public class HfTaskRelationRepository {

    private final HfTaskRelationMapper hfTaskRelationMapper;

    public List<HfTaskRelation> selectByTaskId(Long taskId) {
        LambdaQueryWrapper<HfTaskRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTaskRelation::getTaskId, taskId);
        return hfTaskRelationMapper.selectList(wrapper);
    }

    public HfTaskRelation selectByTaskIdAndRelatedTaskIdAndType(Long taskId, Long relatedTaskId, Integer relationType) {
        LambdaQueryWrapper<HfTaskRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTaskRelation::getTaskId, taskId)
                .eq(HfTaskRelation::getRelatedTaskId, relatedTaskId)
                .eq(HfTaskRelation::getRelationType, relationType);
        return hfTaskRelationMapper.selectOne(wrapper);
    }

    public void insert(HfTaskRelation relation) {
        hfTaskRelationMapper.insert(relation);
    }

    public void deleteById(Long id) {
        hfTaskRelationMapper.deleteById(id);
    }
}
