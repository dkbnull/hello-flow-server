package cn.wbnull.helloflow.app.service;

import cn.wbnull.helloflow.app.dto.dict.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 字典服务接口
 *
 * @author null
 * @date 2026-05-26
 */
public interface SysDictService {

    /**
     * 获取字典类型列表
     */
    Page<DictTypeVO> listDictTypes(String keyword, Integer page, Integer pageSize);

    /**
     * 创建字典类型
     */
    DictTypeVO createDictType(DictTypeCreateRequest request);

    /**
     * 更新字典类型
     */
    DictTypeVO updateDictType(Long id, DictTypeUpdateRequest request);

    /**
     * 获取字典数据列表
     */
    Page<DictDataVO> listDictData(Long typeId, Integer page, Integer pageSize);

    /**
     * 创建字典数据
     */
    DictDataVO createDictData(DictDataCreateRequest request);

    /**
     * 更新字典数据
     */
    DictDataVO updateDictData(Long id, DictDataUpdateRequest request);
}