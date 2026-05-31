package cn.wbnull.helloflow.app.dto.dict;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 字典类型创建请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class DictTypeCreateRequest {

    @NotBlank(message = "字典类型名称不能为空")
    private String name;

    @NotBlank(message = "字典类型编码不能为空")
    private String code;

    private String remark;
}
