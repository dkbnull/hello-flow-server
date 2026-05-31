package cn.wbnull.helloflow.app.service.impl;

import cn.wbnull.helloflow.app.dto.filter.FilterCreateRequest;
import cn.wbnull.helloflow.app.dto.filter.FilterUpdateRequest;
import cn.wbnull.helloflow.app.dto.filter.FilterVO;
import cn.wbnull.helloflow.app.service.HfFilterService;
import cn.wbnull.helloflow.common.exception.BusinessException;
import cn.wbnull.helloflow.common.model.ResultCode;
import cn.wbnull.helloflow.common.util.BeanCopyUtils;
import cn.wbnull.helloflow.data.entity.HfFilter;
import cn.wbnull.helloflow.data.repository.HfFilterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 过滤器服务实现
 *
 * @author null
 * @date 2026-05-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HfFilterServiceImpl implements HfFilterService {

    private final HfFilterRepository hfFilterRepository;

    @Override
    public List<FilterVO> listFilters(Long userId) {
        List<HfFilter> filters = hfFilterRepository.selectByUserId(userId);
        return filters.stream().map(this::toFilterVO).collect(Collectors.toList());
    }

    @Override
    public FilterVO createFilter(FilterCreateRequest request, Long userId) {
        HfFilter filter = new HfFilter();
        BeanCopyUtils.copyNonNullProperties(request, filter);
        filter.setUserId(userId);
        if (filter.getIsDefault() == null) {
            filter.setIsDefault(0);
        }
        hfFilterRepository.insert(filter);
        log.info("创建过滤器：userId={}, name={}", userId, filter.getName());
        return toFilterVO(filter);
    }

    @Override
    public FilterVO updateFilter(Long id, FilterUpdateRequest request, Long userId) {
        HfFilter filter = hfFilterRepository.selectById(id);
        if (filter == null || !filter.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        BeanCopyUtils.copyNonNullProperties(request, filter);
        hfFilterRepository.updateById(filter);
        log.info("更新过滤器：id={}, userId={}", id, userId);
        return toFilterVO(filter);
    }

    @Override
    public void deleteFilter(Long id, Long userId) {
        HfFilter filter = hfFilterRepository.selectById(id);
        if (filter == null || !filter.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        hfFilterRepository.deleteById(id);
        log.info("删除过滤器：id={}, userId={}", id, userId);
    }

    private FilterVO toFilterVO(HfFilter filter) {
        FilterVO vo = new FilterVO();
        BeanCopyUtils.copyNonNullProperties(filter, vo);
        return vo;
    }
}
