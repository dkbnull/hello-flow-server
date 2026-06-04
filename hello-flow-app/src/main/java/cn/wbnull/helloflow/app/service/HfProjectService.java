package cn.wbnull.helloflow.app.service;

import cn.wbnull.helloflow.app.dto.project.ProjectCreateRequest;
import cn.wbnull.helloflow.app.dto.project.ProjectQuery;
import cn.wbnull.helloflow.app.dto.project.ProjectUpdateRequest;
import cn.wbnull.helloflow.app.dto.project.ProjectVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 项目服务接口
 *
 * @author null
 * @date 2026-05-26
 */
public interface HfProjectService {

    /**
     * 创建项目
     */
    ProjectVO createProject(ProjectCreateRequest request);

    /**
     * 更新项目
     */
    ProjectVO updateProject(Long id, ProjectUpdateRequest request);

    /**
     * 获取项目详情
     */
    ProjectVO getProject(Long id);

    /**
     * 获取项目列表
     */
    Page<ProjectVO> listProjects(ProjectQuery query);

    /**
     * 获取项目成员列表（按职位筛选）
     */
    List<ProjectVO.MemberVO> listMembers(Long projectId, String positionCode);

    /**
     * 添加项目成员
     */
    void addMember(Long projectId, Long userId);

    /**
     * 移除项目成员
     */
    void removeMember(Long projectId, Long userId);

    /**
     * 校验项目未归档，归档项目抛出异常
     */
    void validateNotArchived(Long projectId);
}