package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.HfUserNotificationSetting;
import cn.wbnull.helloflow.data.mapper.HfUserNotificationSettingMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Repository;

/**
 * 用户通知设置数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
public class HfUserNotificationSettingRepository extends BaseRepository<HfUserNotificationSettingMapper, HfUserNotificationSetting> {

    private final HfUserNotificationSettingMapper hfUserNotificationSettingMapper;

    public HfUserNotificationSettingRepository(HfUserNotificationSettingMapper hfUserNotificationSettingMapper) {
        super(hfUserNotificationSettingMapper);
        this.hfUserNotificationSettingMapper = hfUserNotificationSettingMapper;
    }

    public HfUserNotificationSetting selectByUserId(Long userId) {
        LambdaQueryWrapper<HfUserNotificationSetting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfUserNotificationSetting::getUserId, userId);
        return hfUserNotificationSettingMapper.selectOne(wrapper);
    }
}
