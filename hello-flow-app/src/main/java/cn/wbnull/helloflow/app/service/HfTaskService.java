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
    TaskVO createTask(Long projectId, TaskCreateRequest request, Long userId);

    /**
     * 更新任务
     */
    TaskVO updateTask(Long id, TaskUpdateRequest request, Long userId);

    /**
     * 删除任务（逻辑删除）
     */
    void deleteTask(Long id, Long userId);

    /**
     * 获取任务详情
     */
    TaskVO getTask(Long id);

    /**
     * 任务列表（支持多维度检索）
     */
    Page<TaskVO> listTasks(TaskQueryRequest query);

    /**
     * 分配任务
     */
    void assignTask(Long id, TaskAssignRequest request, Long operatorId);

    /**
     * 开始开发
     */
    void startTask(Long id, Long userId);

    /**
     * 开发完成
     */
    void completeDev(Long id, Long userId);

    /**
     * 评审通过
     */
    void reviewPass(Long id, Long userId);

    /**
     * 评审不通过
     */
    void reviewReject(Long id, Long userId);

    /**
     * 测试通过
     */
    void testPass(Long id, Long userId);

    /**
     * 测试不通过
     */
    void testReject(Long id, Long userId);

    /**
     * 重新打开
     */
    void reopenTask(Long id, Long userId);

    /**
     * 关闭任务
     */
    void closeTask(Long id, Long userId);

    /**
     * 取消任务
     */
    void cancelTask(Long id, TaskCancelRequest request, Long userId);

    /**
     * 标记延期
     */
    void delayTask(Long id, TaskDelayRequest request, Long userId);

    /**
     * 获取子任务列表
     */
    List<TaskVO> listSubTasks(Long parentId);

    /**
     * 创建子任务
     */
    TaskVO createSubTask(Long parentId, TaskCreateRequest request, Long userId);

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
     * 获取我的任务
     */
    Page<TaskVO> listMyTasks(TaskCondition condition);

    /**
     * 获取我创建的任务
     */
    Page<TaskVO> listReportedTasks(TaskCondition condition);

    /**
     * 获取与我相关的任务（负责人/创建人/开发工程师/测试工程师）
     */
    Page<TaskVO> listRelatedTasks(TaskCondition condition);
}