package cn.wbnull.helloflow.app.dto.role;

import lombok.Data;

/**
 * 角色更新请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class RoleUpdateRequest {

    private String name;

    private String description;

    private Integer status;
}
