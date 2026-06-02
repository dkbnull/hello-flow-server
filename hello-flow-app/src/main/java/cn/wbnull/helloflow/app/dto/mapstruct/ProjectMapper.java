package cn.wbnull.helloflow.app.dto.mapstruct;

import cn.wbnull.helloflow.app.dto.project.ProjectVO;
import cn.wbnull.helloflow.data.entity.HfProject;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * 项目对象映射器
 *
 * @author null
 * @date 2026-06-01
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {

    ProjectVO toProjectVO(HfProject project);
}
