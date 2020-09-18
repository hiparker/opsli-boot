package org.opsli.core.base.service.interfaces;



import java.util.Collection;
import java.util.List;

public interface CrudServiceInterface<T> extends BaseServiceInterface<T> {


    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    T get(String id);

    /**
     * 获取单条数据
     *
     * @param entity
     * @return
     */
    T get(T entity);


    /**
     * 查询数据列表
     *
     * @param entity
     * @return
     */
    List<T> findList(T entity);


    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    T insert(T entity);

    /**
     * 更新数据
     *
     * @param entity
     * @return
     */
    T update(T entity);


    /**
     * 删除数据（物理删除，从数据库中彻底删除）
     *
     * @param id
     * @return
     * @see int delete(T entity)
     */
    int delete(String id);


    /**
     * 删除数据（物理删除，从数据库中彻底删除）
     *
     * @param entity
     * @return
     */
    int delete(T entity);


    /**
     * 批量物理删除
     * @param ids
     * @return
     */
    int deleteAll(String[] ids);

    /**
     * 批量物理删除
     * @param entitys
     * @return
     */
    int deleteAll(Collection<T> entitys);


    /**
     * 删除数据（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     *
     * @param id
     * @return
     * @see int delete(T entity)
     */
    int deleteByLogic(String id);


    /**
     * 删除数据（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
     *
     * @param entity
     * @return
     * @see int delete(T entity)
     */
    int deleteByLogic(T entity);


    /**
     * 批量逻辑删除
     * @param ids
     * @return
     */
    int deleteAllByLogic(String[] ids);

    /**
     * 批量逻辑删除
     * @param entitys
     * @return
     */
    int deleteAllByLogic(Collection<T> entitys);

    /**
     * 查询所有数据列表
     *
     * @return
     * @see List<T> findAllList(T entity)
     */
    //Page findPage(Page page, T t);


}





