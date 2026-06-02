package cn.wbnull.helloflow.app.service;

import cn.wbnull.helloflow.app.dto.notification.NotificationSettingRequest;
import cn.wbnull.helloflow.app.dto.notification.NotificationVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 通知服务接口
 *
 * @author null
 * @date 2026-05-26
 */
public interface HfNotificationService {

    /**
     * 获取通知列表
     */
    Page<NotificationVO> listNotifications(Integer isRead, Integer page, Integer pageSize);

    /**
     * 获取未读数量
     */
    Long getUnreadCount();

    /**
     * 标记已读
     */
    void markRead(Long id);

    /**
     * 全部已读
     */
    void markAllRead();

    /**
     * 获取通知设置
     */
    NotificationVO.SettingVO getSetting();

    /**
     * 更新通知设置
     */
    void updateSetting(NotificationSettingRequest request);

    /**
     * 发送通知
     */
    void sendNotification(Long userId, String title, String content, Integer type, Long relatedId);
}
