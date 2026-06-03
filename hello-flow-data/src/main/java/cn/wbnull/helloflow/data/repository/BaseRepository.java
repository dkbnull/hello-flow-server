package cn.wbnull.helloflow.data.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * Repository 泛型基类，提供通用 CRUD 方法
 *
 * @param <M> Mapper 类型
 * @param <T> 实体类型
 * @author null
 * @date 2026-06-02
 */
public abstract class BaseRepository<M extends BaseMapper<T>, T> {

    protected final M mapper;

    protected BaseRepository(M mapper) {
        this.mapper = mapper;
    }

    public T selectById(Long id) {
        return mapper.selectById(id);
    }

    public void insert(T entity) {
        mapper.insert(entity);
    }

    public void updateById(T entity) {
        mapper.updateById(entity);
    }

    public void deleteById(Long id) {
        mapper.deleteById(id);
    }
}
