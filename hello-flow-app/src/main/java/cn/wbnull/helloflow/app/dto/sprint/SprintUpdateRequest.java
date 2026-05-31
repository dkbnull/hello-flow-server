package cn.wbnull.helloflow.app.dto.sprint;

import lombok.Data;

/**
 * 迭代更新请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class SprintUpdateRequest {

    private String name;

    private String goal;

    private String startDate;

    private String endDate;
}
