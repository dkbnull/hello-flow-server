package cn.wbnull.helloflow.app.service;

import cn.wbnull.helloflow.app.dto.filter.FilterCreateRequest;
import cn.wbnull.helloflow.app.dto.filter.FilterUpdateRequest;
import cn.wbnull.helloflow.app.dto.filter.FilterVO;

import java.util.List;

/**
 * 过滤器服务接口
 *
 * @author null
 * @date 2026-05-26
 */
public interface HfFilterService {

    /**
     * 获取过滤器列表
     */
    List<FilterVO> listFilters(Long userId);

    /**
     * 创建过滤器
     */
    FilterVO createFilter(FilterCreateRequest request, Long userId);

    /**
     * 更新过滤器
     */
    FilterVO updateFilter(Long id, FilterUpdateRequest request, Long userId);

    /**
     * 删除过滤器
     */
    void deleteFilter(Long id, Long userId);
}
