package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.HfSprint;
import cn.wbnull.helloflow.data.mapper.HfSprintMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 迭代数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
@RequiredArgsConstructor
public class HfSprintRepository {

    private final HfSprintMapper hfSprintMapper;

    public HfSprint selectById(Long id) {
        return hfSprintMapper.selectById(id);
    }

    public List<HfSprint> selectByProjectId(Long projectId) {
        LambdaQueryWrapper<HfSprint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfSprint::getProjectId, projectId)
                .orderByDesc(HfSprint::getCreatedAt);
        return hfSprintMapper.selectList(wrapper);
    }

    public void insert(HfSprint sprint) {
        hfSprintMapper.insert(sprint);
    }

    public void updateById(HfSprint sprint) {
        hfSprintMapper.updateById(sprint);
    }
}
