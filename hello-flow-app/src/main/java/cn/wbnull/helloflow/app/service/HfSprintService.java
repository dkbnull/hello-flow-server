package cn.wbnull.helloflow.app.service;

import cn.wbnull.helloflow.app.dto.sprint.SprintCreateRequest;
import cn.wbnull.helloflow.app.dto.sprint.SprintUpdateRequest;
import cn.wbnull.helloflow.app.dto.sprint.SprintVO;

import java.util.List;

/**
 * 迭代服务接口
 *
 * @author null
 * @date 2026-05-26
 */
public interface HfSprintService {

    /**
     * 创建Sprint
     */
    SprintVO createSprint(SprintCreateRequest request);

    /**
     * 更新Sprint
     */
    SprintVO updateSprint(Long id, SprintUpdateRequest request);

    /**
     * 获取Sprint列表
     */
    List<SprintVO> listSprints(Long projectId);

    /**
     * 开始Sprint
     */
    void startSprint(Long id);

    /**
     * 完成Sprint
     */
    void completeSprint(Long id);
}
