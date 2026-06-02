package cn.wbnull.helloflow.app.dto.mapstruct;

import cn.wbnull.helloflow.app.dto.task.TaskVO;
import cn.wbnull.helloflow.data.entity.HfTask;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * 任务对象映射器
 *
 * @author null
 * @date 2026-06-01
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    TaskVO toTaskVO(HfTask task);
}
