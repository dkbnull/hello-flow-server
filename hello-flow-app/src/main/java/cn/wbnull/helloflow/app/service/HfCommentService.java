package cn.wbnull.helloflow.app.service;

import cn.wbnull.helloflow.app.dto.comment.CommentCreateRequest;
import cn.wbnull.helloflow.app.dto.comment.CommentVO;

import java.util.List;

/**
 * 评论服务接口
 *
 * @author null
 * @date 2026-05-26
 */
public interface HfCommentService {

    /**
     * 获取评论列表
     */
    List<CommentVO> listComments(Long taskId);

    /**
     * 添加评论
     */
    CommentVO addComment(Long taskId, CommentCreateRequest request);
}
