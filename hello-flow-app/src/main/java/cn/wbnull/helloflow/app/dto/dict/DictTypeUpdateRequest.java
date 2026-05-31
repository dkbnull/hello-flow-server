package cn.wbnull.helloflow.app.dto.dict;

import lombok.Data;

/**
 * 字典类型更新请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class DictTypeUpdateRequest {

    private String name;

    private String remark;

    private Integer status;
}
