package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.HfFilter;
import cn.wbnull.helloflow.data.mapper.HfFilterMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 过滤器数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
@RequiredArgsConstructor
public class HfFilterRepository {

    private final HfFilterMapper hfFilterMapper;

    public HfFilter selectById(Long id) {
        return hfFilterMapper.selectById(id);
    }

    public List<HfFilter> selectByUserId(Long userId) {
        LambdaQueryWrapper<HfFilter> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfFilter::getUserId, userId)
                .orderByDesc(HfFilter::getCreatedAt);
        return hfFilterMapper.selectList(wrapper);
    }

    public void insert(HfFilter filter) {
        hfFilterMapper.insert(filter);
    }

    public void updateById(HfFilter filter) {
        hfFilterMapper.updateById(filter);
    }

    public void deleteById(Long id) {
        hfFilterMapper.deleteById(id);
    }
}
