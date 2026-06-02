package cn.wbnull.helloflow.app.service.impl;

import cn.wbnull.helloflow.app.dto.notification.NotificationSettingRequest;
import cn.wbnull.helloflow.app.dto.notification.NotificationVO;
import cn.wbnull.helloflow.app.service.HfNotificationService;
import cn.wbnull.helloflow.common.util.BeanCopyUtils;
import cn.wbnull.helloflow.data.entity.HfNotification;
import cn.wbnull.helloflow.data.entity.HfUserNotificationSetting;
import cn.wbnull.helloflow.data.repository.HfNotificationRepository;
import cn.wbnull.helloflow.data.repository.HfUserNotificationSettingRepository;
import cn.wbnull.helloflow.security.util.SecurityUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 通知服务实现
 *
 * @author null
 * @date 2026-05-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HfNotificationServiceImpl implements HfNotificationService {

    private final HfNotificationRepository hfNotificationRepository;
    private final HfUserNotificationSettingRepository hfUserNotificationSettingRepository;

    @Override
    public Page<NotificationVO> listNotifications(Integer isRead, Integer page, Integer pageSize) {
        Long userId = SecurityUtils.getCurrentUserId();
        Page<HfNotification> pageResult = hfNotificationRepository.selectPageByCondition(new Page<>(page, pageSize), userId, isRead);
        Page<NotificationVO> voPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        voPage.setRecords(pageResult.getRecords().stream().map(this::toNotificationVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public Long getUnreadCount() {
        Long userId = SecurityUtils.getCurrentUserId();
        return hfNotificationRepository.selectUnreadCount(userId);
    }

    @Override
    public void markRead(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        HfNotification notification = hfNotificationRepository.selectById(id);
        if (notification != null && notification.getUserId().equals(userId)) {
            notification.setIsRead(1);
            hfNotificationRepository.updateById(notification);
        }
    }

    @Override
    public void markAllRead() {
        Long userId = SecurityUtils.getCurrentUserId();
        List<HfNotification> unreadList = hfNotificationRepository.selectUnreadByUserId(userId);
        for (HfNotification notification : unreadList) {
            notification.setIsRead(1);
            hfNotificationRepository.updateById(notification);
        }
    }

    @Override
    public NotificationVO.SettingVO getSetting() {
        Long userId = SecurityUtils.getCurrentUserId();
        HfUserNotificationSetting setting = hfUserNotificationSettingRepository.selectByUserId(userId);
        if (setting == null) {
            NotificationVO.SettingVO vo = new NotificationVO.SettingVO();
            vo.setUserId(userId);
            vo.setEmailEnabled(0);
            return vo;
        }
        NotificationVO.SettingVO vo = new NotificationVO.SettingVO();
        vo.setUserId(userId);
        vo.setEmailEnabled(setting.getEmailEnabled());
        return vo;
    }

    @Override
    public void updateSetting(NotificationSettingRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        HfUserNotificationSetting setting = hfUserNotificationSettingRepository.selectByUserId(userId);
        if (setting == null) {
            setting = new HfUserNotificationSetting();
            setting.setUserId(userId);
            setting.setEmailEnabled(request.getEmailEnabled() != null ? request.getEmailEnabled() : 0);
            hfUserNotificationSettingRepository.insert(setting);
        } else {
            if (request.getEmailEnabled() != null) {
                setting.setEmailEnabled(request.getEmailEnabled());
            }
            hfUserNotificationSettingRepository.updateById(setting);
        }
    }

    @Override
    public void sendNotification(Long userId, String title, String content, Integer type, Long relatedId) {
        HfNotification notification = new HfNotification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setRelatedId(relatedId);
        notification.setIsRead(0);
        hfNotificationRepository.insert(notification);
        log.info("发送通知给用户 {}：{}", userId, title);
    }

    private NotificationVO toNotificationVO(HfNotification notification) {
        NotificationVO vo = new NotificationVO();
        BeanCopyUtils.copyNonNullProperties(notification, vo);
        return vo;
    }
}
