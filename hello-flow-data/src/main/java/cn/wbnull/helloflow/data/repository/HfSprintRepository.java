package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.HfSprint;
import cn.wbnull.helloflow.data.mapper.HfSprintMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 迭代数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
public class HfSprintRepository extends BaseRepository<HfSprintMapper, HfSprint> {

    private final HfSprintMapper hfSprintMapper;

    public HfSprintRepository(HfSprintMapper hfSprintMapper) {
        super(hfSprintMapper);
        this.hfSprintMapper = hfSprintMapper;
    }

    public List<HfSprint> selectByProjectId(Long projectId) {
        LambdaQueryWrapper<HfSprint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfSprint::getProjectId, projectId)
                .orderByDesc(HfSprint::getCreatedAt);
        return hfSprintMapper.selectList(wrapper);
    }
}
