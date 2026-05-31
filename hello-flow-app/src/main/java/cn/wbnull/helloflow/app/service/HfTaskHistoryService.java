package cn.wbnull.helloflow.app.service;

import cn.wbnull.helloflow.app.dto.task.TaskHistoryCommand;
import cn.wbnull.helloflow.app.dto.task.TaskHistoryVO;

import java.util.List;

/**
 * 任务历史服务接口
 *
 * @author null
 * @date 2026-05-26
 */
public interface HfTaskHistoryService {

    /**
     * 获取任务操作历史
     */
    List<TaskHistoryVO> listHistories(Long taskId);

    /**
     * 记录操作历史
     */
    void recordHistory(TaskHistoryCommand command);
}
