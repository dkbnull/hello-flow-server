package cn.wbnull.helloflow.app.dto.comment;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论数据
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class CommentVO {

    private Long id;
    private Long taskId;
    private Long userId;
    private String username;
    private String nickname;
    private String avatar;
    private String content;
    private LocalDateTime createdAt;
}
