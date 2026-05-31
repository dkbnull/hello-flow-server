package cn.wbnull.helloflow.app.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 评论创建请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class CommentCreateRequest {

    @NotBlank(message = "评论内容不能为空")
    private String content;
}
