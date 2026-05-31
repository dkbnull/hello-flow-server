package cn.wbnull.helloflow.app.dto.dict;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典类型数据
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class DictTypeVO {

    private Long id;
    private String name;
    private String code;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
}
