package cn.wbnull.helloflow.app.controller;

import cn.wbnull.helloflow.app.dto.notification.NotificationSettingRequest;
import cn.wbnull.helloflow.app.dto.notification.NotificationVO;
import cn.wbnull.helloflow.app.service.HfNotificationService;
import cn.wbnull.helloflow.common.model.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 通知控制器
 *
 * @author null
 * @date 2026-05-26
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "通知管理", description = "通知列表、已读、通知设置")
public class NotificationController {

    private final HfNotificationService hfNotificationService;

    @GetMapping("/notifications")
    @Operation(summary = "通知列表")
    public Result<Page<NotificationVO>> listNotifications(
            @RequestParam(required = false) Integer isRead,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.success(hfNotificationService.listNotifications(isRead, page, pageSize));
    }

    @GetMapping("/notifications/unread-count")
    @Operation(summary = "未读数量")
    public Result<Long> getUnreadCount() {
        return Result.success(hfNotificationService.getUnreadCount());
    }

    @PutMapping("/notifications/{id}/read")
    @Operation(summary = "标记已读")
    public Result<Void> markRead(@PathVariable Long id) {
        hfNotificationService.markRead(id);
        return Result.success();
    }

    @PutMapping("/notifications/read-all")
    @Operation(summary = "全部已读")
    public Result<Void> markAllRead() {
        hfNotificationService.markAllRead();
        return Result.success();
    }

    @GetMapping("/notification-settings")
    @Operation(summary = "获取通知设置")
    public Result<NotificationVO.SettingVO> getSetting() {
        return Result.success(hfNotificationService.getSetting());
    }

    @PutMapping("/notification-settings")
    @Operation(summary = "更新通知设置")
    public Result<Void> updateSetting(@Valid @RequestBody NotificationSettingRequest request) {
        hfNotificationService.updateSetting(request);
        return Result.success();
    }
}
