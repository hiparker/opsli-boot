package org.opsli.core.base.service.interfaces;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.opsli.core.persistence.Page;

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
 * @param <T>
 * @param <E>
 */
public interface CrudServiceInterface<T,E> extends BaseServiceInterface<T> {


    /**
     * 获取单条数据
     *
     * @param id ID
     * @return E
     */
    E get(String id);

    /**
     * 获取单条数据
     *
     * @param model model 数据模型
     * @return E
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
     * @param model model 数据模型
     * @return E
     */
    E insert(E model);

    /**
     * 批量插入数据(批量插入数据 暂不支持更新热数据)
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param models model 数据模型
     * @return E
     */
    boolean insertBatch(List<E> models);


    /**
     * 更新数据
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param model model 数据模型
     * @return E
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
     * @param id ID
     * @return boolean
     */
    boolean delete(String id);


    /**
     * 删除数据（物理删除，从数据库中彻底删除）
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param model 数据模型
     * @return boolean
     */
    boolean delete(E model);


    /**
     * 批量物理删除
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param ids id数组
     * @return boolean
     */
    boolean deleteAll(String[] ids);

    /**
     * 批量物理删除
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param models 封装模型
     * @return boolean
     */
    boolean deleteAll(Collection<E> models);


    /**
     * 查询数据列表
     *
     * @param queryWrapper 查询条件构造器
     * @return List<E>
     */
    List<T> findList(QueryWrapper<T> queryWrapper);


    /**
     * 查询全部数据列表
     *
     * @return List<T>
     */
    List<T> findAllList();

    /**
     * 查询分页数据
     *
     * @return  Page<T>
     */
    Page<T,E> findPage(Page<T,E> page);


}





