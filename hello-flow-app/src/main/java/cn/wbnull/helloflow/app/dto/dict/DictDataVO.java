package cn.wbnull.helloflow.app.dto.dict;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典数据
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class DictDataVO {

    private Long id;
    private Long typeId;
    private String label;
    private String value;
    private Integer sort;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
}
