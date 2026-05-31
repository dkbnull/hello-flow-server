package cn.wbnull.helloflow.app.dto.filter;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 过滤器数据
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class FilterVO {

    private Long id;
    private Long userId;
    private String name;
    private String conditions;
    private Integer isDefault;
    private LocalDateTime createdAt;
}
