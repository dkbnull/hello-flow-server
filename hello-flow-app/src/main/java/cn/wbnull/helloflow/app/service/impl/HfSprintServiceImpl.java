package cn.wbnull.helloflow.app.service.impl;

import cn.wbnull.helloflow.app.dto.sprint.SprintCreateRequest;
import cn.wbnull.helloflow.app.dto.sprint.SprintUpdateRequest;
import cn.wbnull.helloflow.app.dto.sprint.SprintVO;
import cn.wbnull.helloflow.app.service.HfProjectService;
import cn.wbnull.helloflow.app.service.HfSprintService;
import cn.wbnull.helloflow.common.exception.BusinessException;
import cn.wbnull.helloflow.common.model.ResultCode;
import cn.wbnull.helloflow.common.util.BeanCopyUtils;
import cn.wbnull.helloflow.data.entity.HfSprint;
import cn.wbnull.helloflow.data.repository.HfSprintRepository;
import cn.wbnull.helloflow.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 迭代服务实现
 *
 * @author null
 * @date 2026-05-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HfSprintServiceImpl implements HfSprintService {

    private final HfSprintRepository hfSprintRepository;
    private final HfProjectService hfProjectService;

    @Override
    public SprintVO createSprint(SprintCreateRequest request) {
        Long projectId = request.getProjectId();
        hfProjectService.validateNotArchived(projectId);
        Long userId = SecurityUtils.getCurrentUserId();
        HfSprint sprint = new HfSprint();
        BeanCopyUtils.copyNonNullProperties(request, sprint, "startDate", "endDate");
        sprint.setProjectId(projectId);
        sprint.setStatus(1);
        if (request.getStartDate() != null) {
            sprint.setStartDate(LocalDate.parse(request.getStartDate()));
        }
        if (request.getEndDate() != null) {
            sprint.setEndDate(LocalDate.parse(request.getEndDate()));
        }
        sprint.setCreatedBy(userId);
        hfSprintRepository.insert(sprint);
        log.info("创建迭代：projectId={}, name={}", projectId, sprint.getName());
        return toSprintVO(sprint);
    }

    @Override
    public SprintVO updateSprint(Long id, SprintUpdateRequest request) {
        HfSprint sprint = getSprintOrThrow(id);
        hfProjectService.validateNotArchived(sprint.getProjectId());
        BeanCopyUtils.copyNonNullProperties(request, sprint, "startDate", "endDate");
        if (request.getStartDate() != null) {
            sprint.setStartDate(LocalDate.parse(request.getStartDate()));
        }
        if (request.getEndDate() != null) {
            sprint.setEndDate(LocalDate.parse(request.getEndDate()));
        }
        hfSprintRepository.updateById(sprint);
        return toSprintVO(sprint);
    }

    @Override
    public List<SprintVO> listSprints(Long projectId) {
        List<HfSprint> sprints = hfSprintRepository.selectByProjectId(projectId);
        return sprints.stream().map(this::toSprintVO).collect(Collectors.toList());
    }

    @Override
    public void startSprint(Long id) {
        HfSprint sprint = getSprintOrThrow(id);
        hfProjectService.validateNotArchived(sprint.getProjectId());
        sprint.setStatus(2);
        hfSprintRepository.updateById(sprint);
        log.info("开始迭代：id={}", id);
    }

    @Override
    public void completeSprint(Long id) {
        HfSprint sprint = getSprintOrThrow(id);
        hfProjectService.validateNotArchived(sprint.getProjectId());
        sprint.setStatus(3);
        hfSprintRepository.updateById(sprint);
        log.info("完成迭代：id={}", id);
    }

    private HfSprint getSprintOrThrow(Long id) {
        HfSprint sprint = hfSprintRepository.selectById(id);
        if (sprint == null) {
            throw new BusinessException(ResultCode.SPRINT_NOT_FOUND);
        }
        return sprint;
    }

    private SprintVO toSprintVO(HfSprint sprint) {
        SprintVO vo = new SprintVO();
        BeanCopyUtils.copyNonNullProperties(sprint, vo);
        return vo;
    }
}
