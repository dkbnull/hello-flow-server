package cn.wbnull.helloflow.app.controller;

import cn.wbnull.helloflow.app.dto.filter.FilterCreateRequest;
import cn.wbnull.helloflow.app.dto.filter.FilterUpdateRequest;
import cn.wbnull.helloflow.app.dto.filter.FilterVO;
import cn.wbnull.helloflow.app.service.HfFilterService;
import cn.wbnull.helloflow.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 过滤器控制器
 *
 * @author null
 * @date 2026-05-26
 */
@RestController
@RequestMapping("/api/filters")
@RequiredArgsConstructor
@Tag(name = "过滤器管理", description = "过滤器CRUD")
public class FilterController {

    private final HfFilterService hfFilterService;

    @GetMapping
    @Operation(summary = "过滤器列表")
    public Result<List<FilterVO>> listFilters() {
        return Result.success(hfFilterService.listFilters());
    }

    @PostMapping
    @Operation(summary = "创建过滤器")
    public Result<FilterVO> createFilter(@Valid @RequestBody FilterCreateRequest request) {
        return Result.success(hfFilterService.createFilter(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新过滤器")
    public Result<FilterVO> updateFilter(@PathVariable Long id, @Valid @RequestBody FilterUpdateRequest request) {
        return Result.success(hfFilterService.updateFilter(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除过滤器")
    public Result<Void> deleteFilter(@PathVariable Long id) {
        hfFilterService.deleteFilter(id);
        return Result.success();
    }
}
