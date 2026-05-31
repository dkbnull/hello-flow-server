package cn.wbnull.helloflow.app.dto.sprint;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 迭代数据
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class SprintVO {

    private Long id;
    private Long projectId;
    private String name;
    private String goal;
    private Integer status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long createdBy;
    private LocalDateTime createdAt;
}
