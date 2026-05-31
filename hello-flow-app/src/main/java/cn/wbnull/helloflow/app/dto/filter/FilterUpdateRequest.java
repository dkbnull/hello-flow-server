package cn.wbnull.helloflow.app.dto.filter;

import lombok.Data;

/**
 * 过滤器更新请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class FilterUpdateRequest {

    private String name;

    private String conditions;

    private Integer isDefault;
}
