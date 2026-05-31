package cn.wbnull.helloflow.app.dto.stats;

import lombok.Data;

/**
 * 成员工作量数据
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class MemberWorkloadVO {

    private Long userId;
    private String username;
    private String nickname;
    private Long totalTasks;
    private Long completedTasks;
    private Long inProgressTasks;
    private Long todoTasks;
}
