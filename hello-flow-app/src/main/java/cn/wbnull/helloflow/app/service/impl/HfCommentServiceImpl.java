package cn.wbnull.helloflow.app.service.impl;

import cn.wbnull.helloflow.app.dto.comment.CommentCreateRequest;
import cn.wbnull.helloflow.app.dto.comment.CommentVO;
import cn.wbnull.helloflow.app.service.HfCommentService;
import cn.wbnull.helloflow.common.util.BeanCopyUtils;
import cn.wbnull.helloflow.data.entity.HfComment;
import cn.wbnull.helloflow.data.entity.SysUser;
import cn.wbnull.helloflow.data.repository.HfCommentRepository;
import cn.wbnull.helloflow.data.repository.SysUserRepository;
import cn.wbnull.helloflow.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论服务实现
 *
 * @author null
 * @date 2026-05-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HfCommentServiceImpl implements HfCommentService {

    private final HfCommentRepository hfCommentRepository;
    private final SysUserRepository sysUserRepository;

    @Override
    public List<CommentVO> listComments(Long taskId) {
        List<HfComment> comments = hfCommentRepository.selectByTaskId(taskId);
        return comments.stream().map(this::toCommentVO).collect(Collectors.toList());
    }

    @Override
    public CommentVO addComment(Long taskId, CommentCreateRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        HfComment comment = new HfComment();
        BeanCopyUtils.copyNonNullProperties(request, comment);
        comment.setTaskId(taskId);
        comment.setUserId(userId);
        hfCommentRepository.insert(comment);
        log.info("添加评论：taskId={}, userId={}", taskId, userId);
        return toCommentVO(comment);
    }

    private CommentVO toCommentVO(HfComment comment) {
        CommentVO vo = new CommentVO();
        BeanCopyUtils.copyNonNullProperties(comment, vo);

        SysUser user = sysUserRepository.selectById(comment.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
            vo.setNickname(user.getNickname());
            vo.setAvatar(user.getAvatar());
        }
        return vo;
    }
}
