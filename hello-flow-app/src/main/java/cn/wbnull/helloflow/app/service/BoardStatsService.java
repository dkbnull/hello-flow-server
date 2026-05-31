package cn.wbnull.helloflow.app.service;

import cn.wbnull.helloflow.app.dto.project.ProjectStatsVO;
import cn.wbnull.helloflow.app.dto.stats.BoardVO;
import cn.wbnull.helloflow.app.dto.stats.BurndownVO;
import cn.wbnull.helloflow.app.dto.stats.DefectStatsVO;
import cn.wbnull.helloflow.app.dto.stats.MemberWorkloadVO;

import java.util.List;

/**
 * 看板与统计服务接口
 *
 * @author null
 * @date 2026-05-30
 */
public interface BoardStatsService {

    BoardVO getProjectBoard(Long projectId);

    BoardVO getSprintBoard(Long sprintId);

    ProjectStatsVO getProjectStats(Long projectId);

    BurndownVO getBurndown(Long projectId, Long sprintId);

    List<MemberWorkloadVO> getMemberWorkload(Long projectId);

    DefectStatsVO getDefectStats(Long projectId);
}
