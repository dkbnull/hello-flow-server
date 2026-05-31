package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.HfTaskHistory;
import cn.wbnull.helloflow.data.mapper.HfTaskHistoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务历史数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
@RequiredArgsConstructor
public class HfTaskHistoryRepository {

    private final HfTaskHistoryMapper hfTaskHistoryMapper;

    public List<HfTaskHistory> selectByTaskId(Long taskId) {
        LambdaQueryWrapper<HfTaskHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTaskHistory::getTaskId, taskId)
                .orderByDesc(HfTaskHistory::getCreatedAt);
        return hfTaskHistoryMapper.selectList(wrapper);
    }

    public void insert(HfTaskHistory history) {
        hfTaskHistoryMapper.insert(history);
    }
}
