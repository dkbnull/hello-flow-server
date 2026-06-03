package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.SysDictData;
import cn.wbnull.helloflow.data.mapper.SysDictDataMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

/**
 * 字典数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
public class SysDictDataRepository extends BaseRepository<SysDictDataMapper, SysDictData> {

    private final SysDictDataMapper sysDictDataMapper;

    public SysDictDataRepository(SysDictDataMapper sysDictDataMapper) {
        super(sysDictDataMapper);
        this.sysDictDataMapper = sysDictDataMapper;
    }

    public Page<SysDictData> selectPageByTypeId(Page<SysDictData> page, Long typeId) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictData::getTypeId, typeId);
        wrapper.orderByAsc(SysDictData::getSort);
        return sysDictDataMapper.selectPage(page, wrapper);
    }
}
