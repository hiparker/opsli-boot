package org.opsli.core.base.service.interfaces;



import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import java.util.Collection;
import java.util.List;

/**
 * 增删改查 总接口
 *
 * 对于 增加、修改、删除处理 ===
 *
 *      不需要做锁状态处理
 *      不需要判断是否成功
 *      能往下走的只能是成功
 *      异常问题 已经统一被处理
 *
 * @param <E>
 * @param <T>
 */
public interface CrudServiceInterface<E,T> extends BaseServiceInterface<T> {


    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    E get(String id);

    /**
     * 获取单条数据
     *
     * @param model
     * @return
     */
    E get(E model);


    /**
     * 插入数据
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param model
     * @return
     */
    E insert(E model);

    /**
     * 更新数据
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param model
     * @return
     */
    E update(E model);


    /**
     * 删除数据（物理删除，从数据库中彻底删除）
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param id
     * @return
     * @see int delete(T entity)
     */
    int delete(String id);


    /**
     * 删除数据（物理删除，从数据库中彻底删除）
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param model
     * @return
     */
    int delete(E model);


    /**
     * 批量物理删除
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param ids
     * @return
     */
    int deleteAll(String[] ids);

    /**
     * 批量物理删除
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param models
     * @return
     */
    int deleteAll(Collection<E> models);


    /**
     * 查询数据列表
     *
     * @param model
     * @return
     */
    List<E> findList(E model);

    /**
     * 查询所有数据列表
     *
     * @return
     * @see List<E> findAllList(T entity)
     */
    //Page findPage(Page page, T t);


}





