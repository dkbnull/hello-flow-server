package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.HfTaskHistory;
import cn.wbnull.helloflow.data.mapper.HfTaskHistoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务历史数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
public class HfTaskHistoryRepository extends BaseRepository<HfTaskHistoryMapper, HfTaskHistory> {

    private final HfTaskHistoryMapper hfTaskHistoryMapper;

    public HfTaskHistoryRepository(HfTaskHistoryMapper hfTaskHistoryMapper) {
        super(hfTaskHistoryMapper);
        this.hfTaskHistoryMapper = hfTaskHistoryMapper;
    }

    public List<HfTaskHistory> selectByTaskId(Long taskId) {
        LambdaQueryWrapper<HfTaskHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTaskHistory::getTaskId, taskId)
                .orderByDesc(HfTaskHistory::getCreatedAt);
        return hfTaskHistoryMapper.selectList(wrapper);
    }
}
