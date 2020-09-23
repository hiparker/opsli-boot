package org.opsli.core.base.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.TypeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.core.base.service.base.BaseService;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.chain.TenantHandler;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.base.service.impl
 * @Author: Parker
 * @CreateTime: 2020-09-14 17:31
 * @Description: CurdServiceImpl 基类 - 实现类
 *
 * 这里 可能觉得 增改 最次返回的也是实体对象 设计的有问题
 *
 * 其实是为了 热点数据 切面用的 用来 增加或者清理数据
 *
 * 没有 直接用一个save类的原因，可能是觉得 新增和修改分开一些会比较好 防止串了数据
 *
 * 批量查询  做了多租户判断
 * 单独按照ID查询数据 和 按照ID修改、删除数据 隔离级别暂时不需要
 * 既然能从page列表中看到的数据 则是这个租户的数据
 *
 */
@Slf4j
@Transactional(readOnly = true)
public abstract class CrudServiceImpl<M extends BaseMapper<T>, E extends ApiWrapper, T extends BaseEntity>
        extends BaseService<M, T> implements CrudServiceInterface<E,T> {


    /** Model Clazz 类 */
    protected Class<E> modelClazz;
    /** Model 泛型游标 */
    private static final int modelIndex = 1;
    /** Entity Clazz 类 */
    protected Class<T> entityClazz;
    /** Entity 泛型游标 */
    private static final int entityIndex = 2;


    @Override
    public E get(String id) {
        return transformT2M(
                super.getById(id)
        );
    }

    @Override
    public E get(E model) {
        if(model == null)  return null;
        return transformT2M(
                super.getById(model.getId())
        );
    }

    @Override
    @Transactional(readOnly = false)
    public E insert(E model) {
        if(model == null) return null;
        T entity = transformM2T(model);
        boolean ret = super.save(entity);
        if(ret){
            return transformT2M(entity);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean insertBatch(List<E> models) {
        if(models == null || models.size() == 0) return false;
        List<T> entitys = transformMs2Ts(models);
        return super.saveBatch(entitys);
    }

    @Override
    @Transactional(readOnly = false)
    public E update(E model) {
        if(model == null) return null;
        T entity = transformM2T(model);
        boolean ret = super.updateById(entity);
        if(ret){
            return transformT2M(entity);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean delete(String id) {
        return super.removeById(id);
    }

    @Override
    @Transactional(readOnly = false)
    public boolean delete(E model) {
        if(model == null) return false;
        return super.removeById(model.getId());
    }

    @Override
    @Transactional(readOnly = false)
    public boolean deleteAll(String[] ids) {
        if(ids == null) return false;
        List<String> idList = Convert.toList(String.class, ids);
        return super.removeByIds(idList);
    }

    @Override
    @Transactional(readOnly = false)
    public boolean deleteAll(Collection<E> models) {
        if(models == null || models.isEmpty()) return false;
        List<String> idList = Lists.newArrayListWithCapacity(models.size());
        for (E entity : models) {
            idList.add(entity.getId());
        }
        return super.removeByIds(idList);
    }

    @Override
    public List<T> findList(QueryWrapper<T> queryWrapper) {
        // 多租户处理
        TenantHandler tenantHandler = new TenantHandler();
        QueryWrapper<T> qWrapper = tenantHandler.handler(entityClazz, queryWrapper);
        return super.list(qWrapper);
    }

    @Override
    public List<T> findAllList() {
        QueryBuilder<T> queryBuilder = new GenQueryBuilder<>();
        // 多租户处理
        TenantHandler tenantHandler = new TenantHandler();
        QueryWrapper<T> qWrapper = tenantHandler.handler(entityClazz, queryBuilder.build());
        return super.list(qWrapper);
    }

    @Override
    public Page<E,T> findPage(Page<E,T> page) {
        page.pageHelperBegin();
        try{
            List<T> list = this.findList(page.getQueryWrapper());
            List<E> es = WrapperUtil.transformInstance(list, modelClazz);
            PageInfo<E> pageInfo = new PageInfo<>(es);
            page.instance(pageInfo);
        } finally {
            page.pageHelperEnd();
        }
        return page;
    }

    // ======================== 对象转化 ========================

    /**
     * 转化 entity 为 model
     * @param entity
     * @return
     */
    protected E transformT2M(T entity){
        return WrapperUtil.transformInstance(entity, modelClazz);
    }

    /**
     * 集合对象
     * 转化 entitys 为 models
     * @param entitys
     * @return
     */
    protected List<E> transformTs2Ms(List<T> entitys){
        return WrapperUtil.transformInstance(entitys, modelClazz);
    }


    /**
     * 转化 model 为 entity
     * @param model
     * @return
     */
    protected T transformM2T(E model){
        return WrapperUtil.transformInstance(model, entityClazz);
    }

    /**
     * 集合对象
     * 转化 models 为 entitys
     * @param models
     * @return
     */
    protected List<T> transformMs2Ts(List<E> models){
        return WrapperUtil.transformInstance(models, entityClazz);
    }


    // ======================== 初始化 ========================

    /**
     * 初始化
     */
    @PostConstruct
    public void init(){
        try {
            this.modelClazz = this.getModelClass();
            this.entityClazz = this.getEntityClass();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }


    /**
     * 获得 泛型 Clazz
     * @return
     */
    private Class<E> getModelClass(){
        Class<E> tClass = null;
        Type typeArgument = TypeUtil.getTypeArgument(getClass().getGenericSuperclass(), modelIndex);
        if(typeArgument != null){
            tClass = (Class<E>) typeArgument;
        }
        return tClass;
    }

    /**
     * 获得 泛型 Clazz
     * @return
     */
    private Class<T> getEntityClass(){
        Class<T> tClass = null;
        Type typeArgument = TypeUtil.getTypeArgument(getClass().getGenericSuperclass(), entityIndex);
        if(typeArgument != null){
            tClass = (Class<T>) typeArgument;
        }
        return tClass;
    }

}
