package cn.wbnull.helloflow.common.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

/**
 * Bean属性拷贝工具类
 *
 * @author null
 * @date 2026-05-28
 */
public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    /**
     * 拷贝source中非null的属性到target
     *
     * @param source 数据源
     * @param target 目标对象
     */
    public static void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * 拷贝source中非null的属性到target，忽略指定字段
     *
     * @param source           数据源
     * @param target           目标对象
     * @param ignoreProperties 忽略的属性名
     */
    public static void copyNonNullProperties(Object source, Object target, String... ignoreProperties) {
        String[] nullNames = getNullPropertyNames(source);
        String[] combined = merge(nullNames, ignoreProperties);
        BeanUtils.copyProperties(source, target, combined);
    }

    private static String[] getNullPropertyNames(Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames.toArray(new String[0]);
    }

    private static String[] merge(String[] a, String[] b) {
        String[] result = new String[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
