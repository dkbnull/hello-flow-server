package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.HfComment;
import cn.wbnull.helloflow.data.mapper.HfCommentMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评论数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
@RequiredArgsConstructor
public class HfCommentRepository {

    private final HfCommentMapper hfCommentMapper;

    public List<HfComment> selectByTaskId(Long taskId) {
        LambdaQueryWrapper<HfComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfComment::getTaskId, taskId)
                .orderByAsc(HfComment::getCreatedAt);
        return hfCommentMapper.selectList(wrapper);
    }

    public void insert(HfComment comment) {
        hfCommentMapper.insert(comment);
    }
}
