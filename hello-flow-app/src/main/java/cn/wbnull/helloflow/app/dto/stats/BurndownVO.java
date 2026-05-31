package cn.wbnull.helloflow.app.dto.stats;

import lombok.Data;

import java.util.List;

/**
 * 燃尽图数据
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class BurndownVO {

    private String sprintName;
    private String startDate;
    private String endDate;
    private Integer totalPoints;
    private List<DailyPoint> actual;
    private List<DailyPoint> ideal;

    @Data
    public static class DailyPoint {

        private String date;
        private Integer remaining;
    }
}
