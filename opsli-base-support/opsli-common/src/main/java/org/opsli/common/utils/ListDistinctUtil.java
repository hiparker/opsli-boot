package org.opsli.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @BelongsProject: think-bboss-parent
 * @BelongsPackage: com.think.bboss.common.utils
 * @Author: Parker
 * @CreateTime: 2021-01-05 14:26
 * @Description: List 去重
 */
@Slf4j
public final class ListDistinctUtil {

    /**
     * 针对 包装类型 进行去重
     * 如 String Integer 等
     *
     * @param list 源数据
     * @return List
     */
    public static <T> List<T> distinct(List<T> list) {
        if(CollUtil.isEmpty(list)){
            return ListUtil.empty();
        }

        List<T> distinctList;
        try {
            distinctList = list.stream()
                    .distinct()
                    .collect(Collectors.toList());
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return list;
        }

        if(CollUtil.isEmpty(distinctList)){
            return ListUtil.empty();
        }
        return distinctList;
    }

    /**
     * 针对 对象 进行去重
     * 如 Object 等 需要自行传入 比较条件
     *
     * @param list 源数据
     * @param comparator 比较器
     * @return List
     */
    public static <T> List<T> distinct(List<T> list, Comparator<T> comparator) {
        if(CollUtil.isEmpty(list)){
            return ListUtil.empty();
        }

        List<T> distinctList;
        try {
            // 去重处理 这里不放在SQL 是为了保证数据库兼容性
            distinctList = list.stream().collect(
                    Collectors.collectingAndThen(
                            Collectors.toCollection(
                                    () -> new TreeSet<>(comparator)
                            ),
                            ArrayList::new
                    )
            );
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return list;
        }

        if(CollUtil.isEmpty(distinctList)){
            return ListUtil.empty();
        }
        return distinctList;
    }


    // ==========================

    private ListDistinctUtil(){}

}
