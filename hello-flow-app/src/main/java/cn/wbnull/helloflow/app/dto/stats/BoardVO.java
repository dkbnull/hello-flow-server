package cn.wbnull.helloflow.app.dto.stats;

import lombok.Data;

import java.util.List;

/**
 * 看板数据
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class BoardVO {

    private List<BoardColumn> columns;

    @Data
    public static class BoardColumn {

        private Integer status;
        private String statusName;
        private List<BoardTask> tasks;
    }

    @Data
    public static class BoardTask {

        private Long id;
        private String title;
        private Integer type;
        private Integer priority;
        private Integer isDelayed;
        private Long assigneeId;
        private String assigneeName;
        private String dueDate;
    }
}
