package cn.wbnull.helloflow.app.controller;

import cn.wbnull.helloflow.app.dto.dict.*;
import cn.wbnull.helloflow.app.service.SysDictService;
import cn.wbnull.helloflow.common.model.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 字典控制器
 *
 * @author null
 * @date 2026-05-26
 */
@RestController
@RequestMapping("/api/dict-types")
@RequiredArgsConstructor
@Tag(name = "字典管理", description = "字典类型和字典数据CRUD")
public class DictController {

    private final SysDictService sysDictService;

    @GetMapping
    @Operation(summary = "字典类型列表")
    public Result<Page<DictTypeVO>> listDictTypes(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.success(sysDictService.listDictTypes(keyword, page, pageSize));
    }

    @PostMapping
    @Operation(summary = "创建字典类型")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<DictTypeVO> createDictType(@Valid @RequestBody DictTypeCreateRequest request) {
        return Result.success(sysDictService.createDictType(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新字典类型")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<DictTypeVO> updateDictType(@PathVariable Long id, @RequestBody DictTypeUpdateRequest request) {
        return Result.success(sysDictService.updateDictType(id, request));
    }

    @GetMapping("/{typeId}/data")
    @Operation(summary = "字典数据列表")
    public Result<Page<DictDataVO>> listDictData(
            @PathVariable Long typeId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.success(sysDictService.listDictData(typeId, page, pageSize));
    }

    @PostMapping("/data")
    @Operation(summary = "创建字典数据")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<DictDataVO> createDictData(@Valid @RequestBody DictDataCreateRequest request) {
        return Result.success(sysDictService.createDictData(request));
    }

    @PutMapping("/data/{id}")
    @Operation(summary = "更新字典数据")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<DictDataVO> updateDictData(@PathVariable Long id, @RequestBody DictDataUpdateRequest request) {
        return Result.success(sysDictService.updateDictData(id, request));
    }
}