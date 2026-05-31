package cn.wbnull.helloflow.data.mapper;

import cn.wbnull.helloflow.data.entity.HfComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论Mapper接口
 *
 * @author null
 * @date 2026-05-26
 */
@Mapper
public interface HfCommentMapper extends BaseMapper<HfComment> {
}
