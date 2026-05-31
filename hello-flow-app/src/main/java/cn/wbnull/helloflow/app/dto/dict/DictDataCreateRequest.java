package cn.wbnull.helloflow.app.dto.dict;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 字典数据创建请求
 *
 * @author null
 * @date 2026-05-26
 */
@Data
public class DictDataCreateRequest {

    @NotNull(message = "字典类型ID不能为空")
    private Long typeId;

    @NotBlank(message = "数据标签不能为空")
    private String label;

    @NotBlank(message = "数据值不能为空")
    private String value;

    private Integer sort;

    private String remark;
}
