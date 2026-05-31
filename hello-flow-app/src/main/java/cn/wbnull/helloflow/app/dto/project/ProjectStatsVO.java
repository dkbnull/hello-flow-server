package cn.wbnull.helloflow.app.dto.project;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 项目统计数据
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class ProjectStatsVO {

    private Map<Integer, Long> statusDistribution;
    private Long totalTasks;
    private Long completedTasks;
    private Double completionRate;
    private List<DailyCount> recentTrend;

    @Data
    public static class DailyCount {

        private String date;
        private Long count;
    }
}
