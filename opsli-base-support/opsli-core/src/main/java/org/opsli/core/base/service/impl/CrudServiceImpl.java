package org.opsli.core.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.EnableHotData;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.core.base.service.base.BaseService;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
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
 */
@Slf4j
public abstract class CrudServiceImpl<M extends BaseMapper<T>, E extends ApiWrapper, T extends BaseEntity>
        extends BaseService<M, T> implements CrudServiceInterface<E,T> {

    /** entity Class 类 */
    protected Class<T> entityClazz;

    /** entity Class 类 */
    protected Class<E> modelClazz;

    @Override
    public E get(String id) {
        return transformT2M(
                baseMapper.selectById(id)
        );
    }

    @Override
    public E get(E model) {
        if(model == null)  return null;
        return transformT2M(
                baseMapper.selectById(model.getId())
        );
    }

    @Override
    public E insert(E model) {
        if(model == null) return null;
        T entity = transformM2T(model);
        int count = baseMapper.insert(entity);
        if(count > 0){
            return transformT2M(entity);
        }
        return null;
    }

    @Override
    public E update(E model) {
        if(model == null) return null;
        T entity = transformM2T(model);
        int count = baseMapper.updateById(entity);
        if(count > 0){
            return transformT2M(entity);
        }
        return null;
    }

    @Override
    public int delete(String id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public int delete(E model) {
        if(model == null) return 0;
        return baseMapper.deleteById(model.getId());
    }

    @Override
    public int deleteAll(String[] ids) {
        if(ids == null) return 0;
        List<String> idList = Arrays.asList(ids);
        return baseMapper.deleteBatchIds(idList);
    }

    @Override
    public int deleteAll(Collection<E> models) {
        if(models == null || models.isEmpty()) return 0;
        List<String> idList = Lists.newArrayListWithCapacity(models.size());
        for (E entity : models) {
            idList.add(entity.getId());
        }
        return baseMapper.deleteBatchIds(idList);
    }

    @Override
    public List<E> findList(E model) {
        return null;
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
        Class<E> tClass = (Class<E>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return tClass;
    }

    /**
     * 获得 泛型 Clazz
     * @return
     */
    private Class<T> getEntityClass(){
        Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[2];
        return tClass;
    }

}
