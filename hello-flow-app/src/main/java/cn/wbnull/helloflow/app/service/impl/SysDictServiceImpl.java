package cn.wbnull.helloflow.app.service.impl;

import cn.wbnull.helloflow.app.dto.dict.*;
import cn.wbnull.helloflow.app.service.SysDictService;
import cn.wbnull.helloflow.common.exception.BusinessException;
import cn.wbnull.helloflow.common.model.ResultCode;
import cn.wbnull.helloflow.common.util.BeanCopyUtils;
import cn.wbnull.helloflow.data.entity.SysDictData;
import cn.wbnull.helloflow.data.entity.SysDictType;
import cn.wbnull.helloflow.data.repository.SysDictDataRepository;
import cn.wbnull.helloflow.data.repository.SysDictTypeRepository;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 字典服务实现
 *
 * @author null
 * @date 2026-05-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl implements SysDictService {

    private final SysDictTypeRepository sysDictTypeRepository;
    private final SysDictDataRepository sysDictDataRepository;

    @Override
    public Page<DictTypeVO> listDictTypes(String keyword, Integer page, Integer pageSize) {
        Page<SysDictType> pageResult = sysDictTypeRepository.selectPageByCondition(new Page<>(page, pageSize), keyword);
        Page<DictTypeVO> voPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        voPage.setRecords(pageResult.getRecords().stream().map(this::toDictTypeVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public DictTypeVO createDictType(DictTypeCreateRequest request) {
        SysDictType dictType = new SysDictType();
        BeanCopyUtils.copyNonNullProperties(request, dictType);
        dictType.setStatus(1);
        sysDictTypeRepository.insert(dictType);
        log.info("创建字典类型：code={}, name={}", dictType.getCode(), dictType.getName());
        return toDictTypeVO(dictType);
    }

    @Override
    public DictTypeVO updateDictType(Long id, DictTypeUpdateRequest request) {
        SysDictType dictType = sysDictTypeRepository.selectById(id);
        if (dictType == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        BeanCopyUtils.copyNonNullProperties(request, dictType);
        sysDictTypeRepository.updateById(dictType);
        log.info("更新字典类型：id={}", id);
        return toDictTypeVO(dictType);
    }

    @Override
    public Page<DictDataVO> listDictData(Long typeId, Integer page, Integer pageSize) {
        Page<SysDictData> pageResult = sysDictDataRepository.selectPageByTypeId(new Page<>(page, pageSize), typeId);
        Page<DictDataVO> voPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        voPage.setRecords(pageResult.getRecords().stream().map(this::toDictDataVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public DictDataVO createDictData(DictDataCreateRequest request) {
        SysDictData dictData = new SysDictData();
        BeanCopyUtils.copyNonNullProperties(request, dictData);
        if (dictData.getSort() == null) {
            dictData.setSort(0);
        }
        dictData.setStatus(1);
        sysDictDataRepository.insert(dictData);
        log.info("创建字典数据：typeId={}, label={}", dictData.getTypeId(), dictData.getLabel());
        return toDictDataVO(dictData);
    }

    @Override
    public DictDataVO updateDictData(Long id, DictDataUpdateRequest request) {
        SysDictData dictData = sysDictDataRepository.selectById(id);
        if (dictData == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        BeanCopyUtils.copyNonNullProperties(request, dictData);
        sysDictDataRepository.updateById(dictData);
        log.info("更新字典数据：id={}", id);
        return toDictDataVO(dictData);
    }

    private DictTypeVO toDictTypeVO(SysDictType dictType) {
        DictTypeVO vo = new DictTypeVO();
        BeanCopyUtils.copyNonNullProperties(dictType, vo);
        return vo;
    }

    private DictDataVO toDictDataVO(SysDictData dictData) {
        DictDataVO vo = new DictDataVO();
        BeanCopyUtils.copyNonNullProperties(dictData, vo);
        return vo;
    }
}
