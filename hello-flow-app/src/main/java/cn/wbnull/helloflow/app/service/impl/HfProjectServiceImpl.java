package cn.wbnull.helloflow.app.service.impl;

import cn.wbnull.helloflow.app.dto.mapstruct.ProjectMapper;
import cn.wbnull.helloflow.app.dto.project.ProjectCreateRequest;
import cn.wbnull.helloflow.app.dto.project.ProjectQueryRequest;
import cn.wbnull.helloflow.app.dto.project.ProjectUpdateRequest;
import cn.wbnull.helloflow.app.dto.project.ProjectVO;
import cn.wbnull.helloflow.app.service.HfProjectService;
import cn.wbnull.helloflow.common.exception.BusinessException;
import cn.wbnull.helloflow.common.model.ResultCode;
import cn.wbnull.helloflow.common.util.BeanCopyUtils;
import cn.wbnull.helloflow.data.entity.HfPosition;
import cn.wbnull.helloflow.data.entity.HfProject;
import cn.wbnull.helloflow.data.entity.HfProjectMember;
import cn.wbnull.helloflow.data.entity.SysUser;
import cn.wbnull.helloflow.data.repository.HfPositionRepository;
import cn.wbnull.helloflow.data.repository.HfProjectMemberRepository;
import cn.wbnull.helloflow.data.repository.HfProjectRepository;
import cn.wbnull.helloflow.data.repository.SysUserRepository;
import cn.wbnull.helloflow.data.util.PageUtils;
import cn.wbnull.helloflow.security.util.SecurityUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目服务实现
 *
 * @author null
 * @date 2026-05-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HfProjectServiceImpl implements HfProjectService {

    private final HfProjectRepository hfProjectRepository;
    private final HfProjectMemberRepository hfProjectMemberRepository;
    private final SysUserRepository sysUserRepository;
    private final HfPositionRepository hfPositionRepository;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectVO createProject(ProjectCreateRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        validateCodeUnique(request.getCode(), null);
        validateNameUnique(request.getName(), null);
        validatePositionMatch(request.getPmId(), "PM");
        validatePositionMatch(request.getDevLeadId(), "DEV");
        validatePositionMatch(request.getTestLeadId(), "QA");

        HfProject project = new HfProject();
        BeanCopyUtils.copyNonNullProperties(request, project);
        project.setStatus(1);
        project.setCreatedBy(userId);
        hfProjectRepository.insert(project);

        addMemberInternal(project.getId(), request.getPmId());
        addMemberInternal(project.getId(), request.getDevLeadId());
        addMemberInternal(project.getId(), request.getTestLeadId());
        addMemberInternal(project.getId(), userId);

        log.info("创建项目：{}", project.getName());
        return toProjectVO(project);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectVO updateProject(Long id, ProjectUpdateRequest request) {
        HfProject project = hfProjectRepository.selectById(id);
        if (project == null) {
            throw new BusinessException(ResultCode.PROJECT_NOT_FOUND);
        }

        // 归档项目仅允许修改status（取消归档），其他字段禁止修改
        if (project.getStatus() == 0) {
            if (request.getStatus() == null || request.getStatus() != 1) {
                throw new BusinessException(ResultCode.PROJECT_ARCHIVED);
            }
            // 取消归档：只更新状态
            project.setStatus(1);
            hfProjectRepository.updateById(project);
            log.info("取消归档项目：id={}", id);
            return toProjectVO(project);
        }

        validateCodeUnique(request.getCode(), id);
        validateNameUnique(request.getName(), id);
        if (request.getPmId() != null) {
            validatePositionMatch(request.getPmId(), "PM");
            addMemberInternal(project.getId(), request.getPmId());
            project.setPmId(request.getPmId());
        }
        if (request.getDevLeadId() != null) {
            validatePositionMatch(request.getDevLeadId(), "DEV");
            addMemberInternal(project.getId(), request.getDevLeadId());
            project.setDevLeadId(request.getDevLeadId());
        }
        if (request.getTestLeadId() != null) {
            validatePositionMatch(request.getTestLeadId(), "QA");
            addMemberInternal(project.getId(), request.getTestLeadId());
            project.setTestLeadId(request.getTestLeadId());
        }
        BeanCopyUtils.copyNonNullProperties(request, project, "pmId", "devLeadId", "testLeadId");
        hfProjectRepository.updateById(project);
        log.info("更新项目：id={}", id);
        return toProjectVO(project);
    }

    @Override
    public ProjectVO getProject(Long id) {
        HfProject project = hfProjectRepository.selectById(id);
        if (project == null) {
            throw new BusinessException(ResultCode.PROJECT_NOT_FOUND);
        }
        return toProjectVO(project);
    }

    @Override
    public Page<ProjectVO> listProjects(ProjectQueryRequest query) {
        query.setUserId(SecurityUtils.getCurrentUserId());
        query.setAdmin(SecurityUtils.isAdmin());
        List<Long> projectIds = null;
        if (!query.isAdmin()) {
            List<HfProjectMember> memberships = hfProjectMemberRepository.selectByUserId(query.getUserId());
            projectIds = memberships.stream().map(HfProjectMember::getProjectId).collect(Collectors.toList());
            if (projectIds.isEmpty()) {
                Page<ProjectVO> emptyPage = new Page<>(query.getPage(), query.getPageSize(), 0);
                emptyPage.setRecords(List.of());
                return emptyPage;
            }
        }
        Page<HfProject> pageResult = hfProjectRepository.selectPageByCondition(
                new Page<>(query.getPage(), query.getPageSize()), query.getKeyword(), query.getStatus(), projectIds);
        return PageUtils.convertPage(pageResult, this::toProjectVO);
    }

    @Override
    public List<ProjectVO.MemberVO> listMembers(Long projectId, String positionCode) {
        HfPosition filterPosition = null;
        if (positionCode != null && !positionCode.isEmpty()) {
            filterPosition = hfPositionRepository.selectByCode(positionCode);
            if (filterPosition == null) {
                return List.of();
            }
        }
        List<HfProjectMember> members = hfProjectMemberRepository.selectByProjectId(projectId);
        if (members.isEmpty()) {
            return List.of();
        }
        Set<Long> userIds = members.stream().map(HfProjectMember::getUserId).collect(Collectors.toSet());
        Map<Long, SysUser> userMap = sysUserRepository.selectByIds(userIds)
                .stream().collect(Collectors.toMap(SysUser::getId, u -> u));
        Set<Long> positionIds = userMap.values().stream()
                .map(SysUser::getPositionId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, HfPosition> positionMap = positionIds.isEmpty() ? Map.of()
                : hfPositionRepository.selectByIds(positionIds).stream().collect(Collectors.toMap(HfPosition::getId, p -> p));

        List<ProjectVO.MemberVO> result = new ArrayList<>();
        for (HfProjectMember member : members) {
            SysUser user = userMap.get(member.getUserId());
            if (user == null) {
                continue;
            }
            HfPosition userPosition = user.getPositionId() != null ? positionMap.get(user.getPositionId()) : null;
            if (filterPosition != null) {
                if (userPosition == null || !filterPosition.getId().equals(userPosition.getId())) {
                    continue;
                }
            }
            ProjectVO.MemberVO vo = new ProjectVO.MemberVO();
            vo.setUserId(user.getId());
            vo.setUsername(user.getUsername());
            vo.setNickname(user.getNickname());
            vo.setJoinedAt(member.getJoinedAt());
            if (userPosition != null) {
                vo.setPositionCode(userPosition.getCode());
                vo.setPositionName(userPosition.getName());
            }
            result.add(vo);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMember(Long projectId, Long userId) {
        validateNotArchived(projectId);
        addMemberInternal(projectId, userId);
        log.info("添加项目成员：projectId={}, userId={}", projectId, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMember(Long projectId, Long userId) {
        validateNotArchived(projectId);
        hfProjectMemberRepository.deleteByProjectIdAndUserId(projectId, userId);
        log.info("移除项目成员：projectId={}, userId={}", projectId, userId);
    }

    private void validateCodeUnique(String code, Long excludeId) {
        HfProject existing = hfProjectRepository.selectByCode(code);
        if (existing != null && !existing.getId().equals(excludeId)) {
            throw new BusinessException(ResultCode.PROJECT_CODE_EXISTS);
        }
    }

    @Override
    public void validateNotArchived(Long projectId) {
        HfProject project = hfProjectRepository.selectById(projectId);
        if (project != null && project.getStatus() == 0) {
            throw new BusinessException(ResultCode.PROJECT_ARCHIVED);
        }
    }

    private void validateNameUnique(String name, Long excludeId) {
        HfProject existing = hfProjectRepository.selectByName(name);
        if (existing != null && !existing.getId().equals(excludeId)) {
            throw new BusinessException(ResultCode.PROJECT_NAME_EXISTS);
        }
    }

    private void validatePositionMatch(Long userId, String expectedPositionCode) {
        if (userId == null) {
            return;
        }
        SysUser user = sysUserRepository.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        if (user.getPositionId() == null) {
            throw new BusinessException(ResultCode.POSITION_MISMATCH);
        }
        HfPosition position = hfPositionRepository.selectById(user.getPositionId());
        if (position == null || !expectedPositionCode.equals(position.getCode())) {
            throw new BusinessException(ResultCode.POSITION_MISMATCH);
        }
    }

    private void addMemberInternal(Long projectId, Long userId) {
        if (userId == null) {
            return;
        }
        HfProjectMember existing = hfProjectMemberRepository.selectByProjectIdAndUserId(projectId, userId);
        if (existing == null) {
            HfProjectMember member = new HfProjectMember();
            member.setProjectId(projectId);
            member.setUserId(userId);
            hfProjectMemberRepository.insert(member);
        }
    }

    private ProjectVO toProjectVO(HfProject project) {
        ProjectVO vo = projectMapper.toProjectVO(project);
        if (project.getPmId() != null) {
            vo.setPmName(sysUserRepository.getDisplayName(project.getPmId()));
        }
        if (project.getDevLeadId() != null) {
            vo.setDevLeadName(sysUserRepository.getDisplayName(project.getDevLeadId()));
        }
        if (project.getTestLeadId() != null) {
            vo.setTestLeadName(sysUserRepository.getDisplayName(project.getTestLeadId()));
        }
        return vo;
    }
}
