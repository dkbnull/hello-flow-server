package cn.wbnull.helloflow.app.dto.project;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目数据
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class ProjectVO {

    private Long id;
    private String code;
    private String name;
    private String description;
    private Long pmId;
    private String pmName;
    private Long devLeadId;
    private String devLeadName;
    private Long testLeadId;
    private String testLeadName;
    private Integer status;
    private Long createdBy;
    private LocalDateTime createdAt;
    private List<MemberVO> members;

    @Data
    public static class MemberVO {

        private Long userId;
        private String username;
        private String nickname;
        private String positionCode;
        private String positionName;
        private LocalDateTime joinedAt;
    }
}
