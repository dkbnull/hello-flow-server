package cn.wbnull.helloflow.app.dto.filter;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 过滤器创建请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class FilterCreateRequest {

    @NotBlank(message = "过滤器名称不能为空")
    private String name;

    private String conditions;

    private Integer isDefault;
}
