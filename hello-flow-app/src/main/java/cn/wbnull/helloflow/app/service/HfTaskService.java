package cn.wbnull.helloflow.app.service;

import cn.wbnull.helloflow.app.dto.task.*;
import cn.wbnull.helloflow.data.condition.TaskCondition;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 任务服务接口
 *
 * @author null
 * @date 2026-05-26
 */
public interface HfTaskService {

    /**
     * 创建任务
     */
    TaskVO createTask(TaskCreateRequest request);

    /**
     * 更新任务
     */
    TaskVO updateTask(Long id, TaskUpdateRequest request);

    /**
     * 删除任务（逻辑删除）
     */
    void deleteTask(Long id);

    /**
     * 获取任务详情
     */
    TaskVO getTask(Long id);

    /**
     * 任务列表（支持多维度检索）
     */
    Page<TaskVO> listTasks(TaskCondition condition);

    /**
     * 分配任务
     */
    void assignTask(Long id, TaskAssignRequest request);

    /**
     * 任务状态流转
     */
    void transitionTask(Long id, TaskTransitionRequest request);

    /**
     * 标记延期
     */
    void delayTask(Long id, TaskDelayRequest request);

    /**
     * 获取子任务列表
     */
    List<TaskVO> listSubTasks(Long parentId);

    /**
     * 创建子任务
     */
    TaskVO createSubTask(Long parentId, TaskCreateRequest request);

    /**
     * 获取任务关联列表
     */
    List<TaskVO.TaskRelationVO> listRelations(Long taskId);

    /**
     * 添加任务关联
     */
    void addRelation(Long taskId, TaskRelationCreateRequest request);

    /**
     * 删除任务关联
     */
    void removeRelation(Long relationId);

    /**
     * 获取我负责的任务（负责人/开发工程师/测试工程师任一匹配）
     */
    Page<TaskVO> listMyTasks(TaskCondition condition);

    /**
     * 获取待我审查的任务（状态为待审查，且我不是开发工程师，且任务属于我参与的项目）
     */
    Page<TaskVO> listPendingReviewTasks(TaskCondition condition);
}