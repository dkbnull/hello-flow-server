package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.HfPosition;
import cn.wbnull.helloflow.data.mapper.HfPositionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 岗位数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
public class HfPositionRepository extends BaseRepository<HfPositionMapper, HfPosition> {

    private final HfPositionMapper hfPositionMapper;

    public HfPositionRepository(HfPositionMapper hfPositionMapper) {
        super(hfPositionMapper);
        this.hfPositionMapper = hfPositionMapper;
    }

    public List<HfPosition> selectByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return hfPositionMapper.selectByIds(ids);
    }

    public HfPosition selectByCode(String code) {
        LambdaQueryWrapper<HfPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfPosition::getCode, code);
        return hfPositionMapper.selectOne(wrapper);
    }
}
