package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.SysDictData;
import cn.wbnull.helloflow.data.mapper.SysDictDataMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 字典数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
@RequiredArgsConstructor
public class SysDictDataRepository {

    private final SysDictDataMapper sysDictDataMapper;

    public SysDictData selectById(Long id) {
        return sysDictDataMapper.selectById(id);
    }

    public Page<SysDictData> selectPageByTypeId(Page<SysDictData> page, Long typeId) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictData::getTypeId, typeId);
        wrapper.orderByAsc(SysDictData::getSort);
        return sysDictDataMapper.selectPage(page, wrapper);
    }

    public void insert(SysDictData dictData) {
        sysDictDataMapper.insert(dictData);
    }

    public void updateById(SysDictData dictData) {
        sysDictDataMapper.updateById(dictData);
    }
}
