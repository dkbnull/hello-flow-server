package cn.wbnull.helloflow.app.dto.stats;

import lombok.Data;

/**
 * 缺陷统计数据
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class DefectStatsVO {

    private Long totalDefects;
    private Long openDefects;
    private Long closedDefects;
    private Double fixRate;
}
