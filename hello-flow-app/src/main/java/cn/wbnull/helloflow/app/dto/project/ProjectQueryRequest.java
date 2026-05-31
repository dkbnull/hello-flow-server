package cn.wbnull.helloflow.app.dto.project;

import lombok.Data;

/**
 * 项目查询请求
 *
 * @author null
 * @date 2026-05-30
 */
@Data
public class ProjectQueryRequest {

    private String keyword;

    private Integer status;

    private Integer page = 1;

    private Integer pageSize = 20;

    private Long userId;

    private boolean isAdmin;
}
