package cn.wbnull.helloflow.data.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页工具类
 *
 * @author null
 * @date 2026-06-02
 */
public class PageUtils {

    private PageUtils() {
    }

    /**
     * 将分页实体转换为分页VO
     *
     * @param sourcePage 源分页
     * @param converter  实体转VO的转换函数
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 转换后的分页
     */
    public static <S, T> Page<T> convertPage(Page<S> sourcePage, Function<S, T> converter) {
        Page<T> targetPage = new Page<>(sourcePage.getCurrent(), sourcePage.getSize(), sourcePage.getTotal());
        targetPage.setRecords(sourcePage.getRecords().stream().map(converter).collect(Collectors.toList()));
        return targetPage;
    }
}
