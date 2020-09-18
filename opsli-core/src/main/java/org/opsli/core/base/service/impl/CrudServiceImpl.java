package org.opsli.core.base.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.core.base.service.base.BaseService;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;

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
public abstract class CrudServiceImpl<M extends BaseMapper<T>, T extends BaseEntity>
        extends BaseService<M, T> implements CrudServiceInterface<T> {

    /** entity Class 类 */
    protected Class<T> entityClazz;


    @Override
    public T get(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public T get(T entity) {
        if(entity == null)  return null;
        return baseMapper.selectById(entity.getId());
    }

    @Override
    public List<T> findList(T entity) {
        //baseMapper.selectList()
        return null;
    }

    @Override
    public T insert(T entity) {
        if(entity == null) return null;
        int count = baseMapper.insert(entity);
        if(count > 0){
            return entity;
        }
        return null;
    }

    @Override
    public T update(T entity) {
        if(entity == null) return null;
        int count = baseMapper.updateById(entity);
        if(count > 0){
            return entity;
        }
        return null;
    }

    @Override
    public int delete(String id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public int delete(T entity) {
        if(entity == null) return 0;
        return baseMapper.deleteById(entity.getId());
    }

    @Override
    public int deleteAll(String[] ids) {
        if(ids == null) return 0;
        List<String> idList = Arrays.asList(ids);
        return baseMapper.deleteBatchIds(idList);
    }

    @Override
    public int deleteAll(Collection<T> entitys) {
        if(entitys == null || entitys.isEmpty()) return 0;
        List<String> idList = Lists.newArrayListWithCapacity(entitys.size());
        for (T entity : entitys) {
            idList.add(entity.getId());
        }
        return baseMapper.deleteBatchIds(idList);
    }

    @Override
    public int deleteByLogic(String id) {
        return 0;
    }

    @Override
    public int deleteByLogic(T entity) {
        return 0;
    }

    @Override
    public int deleteAllByLogic(String[] ids) {
        return 0;
    }

    @Override
    public int deleteAllByLogic(Collection<T> entities) {
        return 0;
    }


}
