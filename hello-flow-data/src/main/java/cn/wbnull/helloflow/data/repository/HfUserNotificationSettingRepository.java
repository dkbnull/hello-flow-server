package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.HfUserNotificationSetting;
import cn.wbnull.helloflow.data.mapper.HfUserNotificationSettingMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 用户通知设置数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
@RequiredArgsConstructor
public class HfUserNotificationSettingRepository {

    private final HfUserNotificationSettingMapper hfUserNotificationSettingMapper;

    public HfUserNotificationSetting selectByUserId(Long userId) {
        LambdaQueryWrapper<HfUserNotificationSetting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfUserNotificationSetting::getUserId, userId);
        return hfUserNotificationSettingMapper.selectOne(wrapper);
    }

    public void insert(HfUserNotificationSetting setting) {
        hfUserNotificationSettingMapper.insert(setting);
    }

    public void updateById(HfUserNotificationSetting setting) {
        hfUserNotificationSettingMapper.updateById(setting);
    }
}
