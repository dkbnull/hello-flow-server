package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.condition.TaskCondition;
import cn.wbnull.helloflow.data.entity.HfTask;
import cn.wbnull.helloflow.data.enums.TaskStatusEnum;
import cn.wbnull.helloflow.data.mapper.HfTaskMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
public class HfTaskRepository extends BaseRepository<HfTaskMapper, HfTask> {

    private final HfTaskMapper hfTaskMapper;

    public HfTaskRepository(HfTaskMapper hfTaskMapper) {
        super(hfTaskMapper);
        this.hfTaskMapper = hfTaskMapper;
    }

    public List<HfTask> selectByParentId(Long parentId) {
        LambdaQueryWrapper<HfTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTask::getParentId, parentId);
        return hfTaskMapper.selectList(wrapper);
    }

    public List<HfTask> selectByProjectId(Long projectId) {
        LambdaQueryWrapper<HfTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTask::getProjectId, projectId);
        return hfTaskMapper.selectList(wrapper);
    }

    public List<HfTask> selectTopLevelByProjectId(Long projectId) {
        LambdaQueryWrapper<HfTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTask::getProjectId, projectId)
                .isNull(HfTask::getParentId);
        return hfTaskMapper.selectList(wrapper);
    }

    public List<HfTask> selectTopLevelBySprintId(Long sprintId) {
        LambdaQueryWrapper<HfTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTask::getSprintId, sprintId)
                .isNull(HfTask::getParentId);
        return hfTaskMapper.selectList(wrapper);
    }

    public List<HfTask> selectByProjectIdAndSprintId(Long projectId, Long sprintId) {
        LambdaQueryWrapper<HfTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTask::getProjectId, projectId)
                .eq(HfTask::getSprintId, sprintId);
        return hfTaskMapper.selectList(wrapper);
    }

    public List<HfTask> selectByProjectIdAndType(Long projectId, Integer type) {
        LambdaQueryWrapper<HfTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTask::getProjectId, projectId)
                .eq(HfTask::getType, type);
        return hfTaskMapper.selectList(wrapper);
    }

    public Page<HfTask> selectPageByCondition(Page<HfTask> page, TaskCondition condition) {
        LambdaQueryWrapper<HfTask> wrapper = new LambdaQueryWrapper<>();
        if (condition.getProjectId() != null) {
            wrapper.eq(HfTask::getProjectId, condition.getProjectId());
        }
        if (condition.getAssigneeId() != null) {
            wrapper.eq(HfTask::getAssigneeId, condition.getAssigneeId());
        }
        if (condition.getReporterId() != null) {
            wrapper.eq(HfTask::getReporterId, condition.getReporterId());
        }
        wrapper.isNull(HfTask::getParentId);
        applyCommonConditions(wrapper, condition);
        applyDateConditions(wrapper, condition);
        wrapper.orderByDesc(HfTask::getCreatedAt);
        return hfTaskMapper.selectPage(page, wrapper);
    }

    /**
     * 查询我负责的任务（负责人/开发工程师/测试工程师任一匹配）
     */
    public Page<HfTask> selectPageByMine(Page<HfTask> page, TaskCondition condition) {
        LambdaQueryWrapper<HfTask> wrapper = new LambdaQueryWrapper<>();
        Long userId = condition.getAssigneeId();
        wrapper.and(w -> w
                .eq(HfTask::getAssigneeId, userId)
                .or().eq(HfTask::getDeveloperId, userId)
                .or().eq(HfTask::getTesterId, userId));
        applyCommonConditions(wrapper, condition);
        applyDateConditions(wrapper, condition);
        wrapper.orderByDesc(HfTask::getCreatedAt);
        return hfTaskMapper.selectPage(page, wrapper);
    }

    private void applyCommonConditions(LambdaQueryWrapper<HfTask> wrapper, TaskCondition condition) {
        if (condition.getStatus() != null) {
            wrapper.eq(HfTask::getStatus, condition.getStatus());
        }
        if (condition.getType() != null) {
            wrapper.eq(HfTask::getType, condition.getType());
        }
        if (condition.getPriority() != null) {
            wrapper.eq(HfTask::getPriority, condition.getPriority());
        }
        if (condition.getSprintId() != null) {
            wrapper.eq(HfTask::getSprintId, condition.getSprintId());
        }
        if (condition.getIsDelayed() != null) {
            wrapper.eq(HfTask::getIsDelayed, condition.getIsDelayed());
        }
        if (condition.getKeyword() != null && !condition.getKeyword().trim().isEmpty()) {
            wrapper.like(HfTask::getTitle, condition.getKeyword().trim());
        }
    }

    private void applyDateConditions(LambdaQueryWrapper<HfTask> wrapper, TaskCondition condition) {
        if (condition.getDueDateStart() != null) {
            wrapper.ge(HfTask::getDueDate, condition.getDueDateStart());
        }
        if (condition.getDueDateEnd() != null) {
            wrapper.le(HfTask::getDueDate, condition.getDueDateEnd());
        }
        if (condition.getCreatedAtStart() != null) {
            wrapper.ge(HfTask::getCreatedAt, condition.getCreatedAtStart());
        }
        if (condition.getCreatedAtEnd() != null) {
            wrapper.le(HfTask::getCreatedAt, condition.getCreatedAtEnd());
        }
    }

    /**
     * 查询待当前用户审查的任务（状态为待审查，且当前用户不是任务的开发工程师，且任务属于当前用户参与的项目）
     */
    public Page<HfTask> selectPageByPendingReview(Page<HfTask> page, TaskCondition condition, List<Long> projectIds) {
        LambdaQueryWrapper<HfTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTask::getStatus, TaskStatusEnum.IN_REVIEW.getCode())
                .ne(HfTask::getDeveloperId, condition.getAssigneeId())
                .in(HfTask::getProjectId, projectIds);
        applyCommonConditions(wrapper, condition);
        applyDateConditions(wrapper, condition);
        wrapper.orderByDesc(HfTask::getCreatedAt);
        return hfTaskMapper.selectPage(page, wrapper);
    }

    public Integer selectMaxSeqByProjectId(Long projectId) {
        LambdaQueryWrapper<HfTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTask::getProjectId, projectId)
                .orderByDesc(HfTask::getTaskSeq)
                .last("LIMIT 1 FOR UPDATE");
        HfTask task = hfTaskMapper.selectOne(wrapper);
        return task != null ? task.getTaskSeq() : 0;
    }
}
