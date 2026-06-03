package cn.wbnull.helloflow.data.repository;

import cn.wbnull.helloflow.data.entity.HfNotification;
import cn.wbnull.helloflow.data.mapper.HfNotificationMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通知数据访问层
 *
 * @author null
 * @date 2026-05-27
 */
@Repository
public class HfNotificationRepository extends BaseRepository<HfNotificationMapper, HfNotification> {

    private final HfNotificationMapper hfNotificationMapper;

    public HfNotificationRepository(HfNotificationMapper hfNotificationMapper) {
        super(hfNotificationMapper);
        this.hfNotificationMapper = hfNotificationMapper;
    }

    public Page<HfNotification> selectPageByCondition(Page<HfNotification> page, Long userId, Integer isRead) {
        LambdaQueryWrapper<HfNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfNotification::getUserId, userId);
        if (isRead != null) {
            wrapper.eq(HfNotification::getIsRead, isRead);
        }
        wrapper.orderByDesc(HfNotification::getCreatedAt);
        return hfNotificationMapper.selectPage(page, wrapper);
    }

    public Long selectUnreadCount(Long userId) {
        LambdaQueryWrapper<HfNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfNotification::getUserId, userId)
                .eq(HfNotification::getIsRead, 0);
        return hfNotificationMapper.selectCount(wrapper);
    }

    public List<HfNotification> selectUnreadByUserId(Long userId) {
        LambdaQueryWrapper<HfNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HfNotification::getUserId, userId)
                .eq(HfNotification::getIsRead, 0);
        return hfNotificationMapper.selectList(wrapper);
    }
}
