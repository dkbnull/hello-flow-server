package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.SysDictType;
import cn.wbnull.helloflow.data.mapper.SysDictTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 字典类型数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
@RequiredArgsConstructor
public class SysDictTypeRepository {

    private final SysDictTypeMapper sysDictTypeMapper;

    public SysDictType selectById(Long id) {
        return sysDictTypeMapper.selectById(id);
    }

    public Page<SysDictType> selectPageByCondition(Page<SysDictType> page, String keyword) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(SysDictType::getName, keyword).or().like(SysDictType::getCode, keyword);
        }
        wrapper.orderByDesc(SysDictType::getCreatedAt);
        return sysDictTypeMapper.selectPage(page, wrapper);
    }

    public void insert(SysDictType dictType) {
        sysDictTypeMapper.insert(dictType);
    }

    public void updateById(SysDictType dictType) {
        sysDictTypeMapper.updateById(dictType);
    }
}
