package cn.wbnull.helloflow.app.dto.notification;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知数据
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class NotificationVO {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Integer type;
    private Long relatedId;
    private Integer isRead;
    private LocalDateTime createdAt;

    /**
     * 通知设置VO
     */
    @Data
    public static class SettingVO {

        private Long userId;
        private Integer emailEnabled;
    }
}
