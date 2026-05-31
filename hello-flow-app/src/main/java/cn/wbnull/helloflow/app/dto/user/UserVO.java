package cn.wbnull.helloflow.app.dto.user;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户数据
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class UserVO {

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private Long positionId;
    private String positionName;
    private String positionCode;
    private Integer status;
    private List<String> roles;
    private LocalDateTime createdAt;
}
