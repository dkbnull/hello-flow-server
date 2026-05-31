package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.HfProject;
import cn.wbnull.helloflow.data.mapper.HfProjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 项目数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
@RequiredArgsConstructor
public class HfProjectRepository {

    private final HfProjectMapper hfProjectMapper;

    public HfProject selectById(Long id) {
        return hfProjectMapper.selectById(id);
    }

    public void insert(HfProject project) {
        hfProjectMapper.insert(project);
    }

    public void updateById(HfProject project) {
        hfProjectMapper.updateById(project);
    }

    public Page<HfProject> selectPageByCondition(Page<HfProject> page, String keyword, Integer status, List<Long> projectIds) {
        LambdaQueryWrapper<HfProject> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(HfProject::getName, keyword);
        }
        if (status != null) {
            wrapper.eq(HfProject::getStatus, status);
        }
        if (projectIds != null) {
            wrapper.in(HfProject::getId, projectIds);
        }
        wrapper.orderByDesc(HfProject::getCreatedAt);
        return hfProjectMapper.selectPage(page, wrapper);
    }

    public HfProject selectByCode(String code) {
        LambdaQueryWrapper<HfProject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfProject::getCode, code);
        return hfProjectMapper.selectOne(wrapper);
    }

    public HfProject selectByName(String name) {
        LambdaQueryWrapper<HfProject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfProject::getName, name);
        return hfProjectMapper.selectOne(wrapper);
    }
}
