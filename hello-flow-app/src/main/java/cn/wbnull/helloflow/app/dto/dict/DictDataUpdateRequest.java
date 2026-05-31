package cn.wbnull.helloflow.app.dto.dict;

import lombok.Data;

/**
 * 字典数据更新请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class DictDataUpdateRequest {

    private String label;

    private String value;

    private Integer sort;

    private String remark;

    private Integer status;
}
