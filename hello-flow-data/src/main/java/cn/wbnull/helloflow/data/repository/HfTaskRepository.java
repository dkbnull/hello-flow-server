package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.condition.TaskCondition;
import cn.wbnull.helloflow.data.entity.HfTask;
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
        wrapper.isNull(HfTask::getParentId);
        applyCommonConditions(wrapper, condition);
        applyDateConditions(wrapper, condition);
        wrapper.orderByDesc(HfTask::getCreatedAt);
        return hfTaskMapper.selectPage(page, wrapper);
    }

    public Page<HfTask> selectPageByAssigneeId(Page<HfTask> page, TaskCondition condition) {
        LambdaQueryWrapper<HfTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTask::getAssigneeId, condition.getAssigneeId());
        applyCommonConditions(wrapper, condition);
        applyDateConditions(wrapper, condition);
        wrapper.orderByDesc(HfTask::getCreatedAt);
        return hfTaskMapper.selectPage(page, wrapper);
    }

    public Page<HfTask> selectPageByReporterId(Page<HfTask> page, TaskCondition condition) {
        LambdaQueryWrapper<HfTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTask::getReporterId, condition.getReporterId());
        applyCommonConditions(wrapper, condition);
        applyDateConditions(wrapper, condition);
        wrapper.orderByDesc(HfTask::getCreatedAt);
        return hfTaskMapper.selectPage(page, wrapper);
    }

    public Page<HfTask> selectPageByRelatedUserId(Page<HfTask> page, TaskCondition condition) {
        LambdaQueryWrapper<HfTask> wrapper = new LambdaQueryWrapper<>();
        Long userId = condition.getAssigneeId();
        wrapper.and(w -> w
                .eq(HfTask::getAssigneeId, userId)
                .or().eq(HfTask::getReporterId, userId)
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

    public Integer selectMaxSeqByProjectId(Long projectId) {
        LambdaQueryWrapper<HfTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfTask::getProjectId, projectId)
                .orderByDesc(HfTask::getTaskSeq)
                .last("LIMIT 1 FOR UPDATE");
        HfTask task = hfTaskMapper.selectOne(wrapper);
        return task != null ? task.getTaskSeq() : 0;
    }
}
