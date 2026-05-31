package cn.wbnull.helloflow.data.mapper;

import cn.wbnull.helloflow.data.entity.HfUserNotificationSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户通知设置Mapper接口
 *
 * @author null
 * @date 2026-05-26
 */
@Mapper
public interface HfUserNotificationSettingMapper extends BaseMapper<HfUserNotificationSetting> {
}
