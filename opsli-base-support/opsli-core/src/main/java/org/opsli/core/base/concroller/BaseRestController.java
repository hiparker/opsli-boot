package org.opsli.core.base.concroller;


import cn.hutool.core.util.TypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.EnableHotData;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.msg.CommonMsg;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.core.cache.local.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.base.concroller
 * @Author: Parker
 * @CreateTime: 2020-09-13 21:16
 * @Description: Controller 基类
 *
 * 默认 范型引用 子类的Service ， 为简单的CRUD做足准备
 *
 */
@Slf4j
@RestController
public abstract class BaseRestController <E extends ApiWrapper, T extends BaseEntity, S extends CrudServiceInterface<E,T>>{

    /** 开启热点数据状态 */
    protected boolean hotDataFlag = false;

    /** Model Clazz 类 */
    protected Class<E> modelClazz;
    /** Model 泛型游标 */
    private static final int modelIndex = 0;
    /** Entity Clazz 类 */
    protected Class<T> entityClazz;
    /** Entity 泛型游标 */
    private static final int entityIndex = 1;

    @Autowired(required = false)
    protected S IService;


    /**
     * 默认 直接设置 传入数据的
     * 根据id 从缓存 直接查询 数据对象
     *
     * @param id
     * @return
     */
    @ModelAttribute
    public E get(@RequestParam(required=false) String id) {
        E model = null;
        if (StringUtils.isNotBlank(id)){
            // 如果开启缓存 先从缓存读
            if(hotDataFlag){
                model = WrapperUtil.transformInstance(
                        CacheUtil.get(id, entityClazz),modelClazz);
                if(model != null){
                    return model;
                }
            }
            // 如果缓存没读到 则去数据库读
            model = WrapperUtil.transformInstance(IService.get(id),modelClazz);
            if(model != null){
                // 如果开启缓存 将数据库查询对象 存如缓存
                if(hotDataFlag){
                    // 这里会 同步更新到本地Ehcache 和 Redis缓存
                    // 如果其他服务器缓存也丢失了 则 回去Redis拉取
                    CacheUtil.put(id, model);
                }
            }
        }
        if (model == null){
            try {
                // 创建泛型对象
                model = this.createModel();
            }catch (Exception e){
                log.error(CommonMsg.EXCEPTION_CONTROLLER_MODEL.toString()+" : {}",e.getMessage());
                throw new ServiceException(CommonMsg.EXCEPTION_CONTROLLER_MODEL);
            }
        }
        return model;
    }

    // =================================================

    @PostConstruct
    public void init(){
        try {
            this.modelClazz = this.getModelClass();
            this.entityClazz = this.getEntityClass();
            Class<?> serviceClazz = IService.getServiceClazz();
            // 判断是否开启热点数据
            if(serviceClazz != null){
                EnableHotData annotation = serviceClazz.getAnnotation(EnableHotData.class);
                if(annotation != null){
                    this.hotDataFlag = true;
                }
            }

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }


    /**
     * 创建包装类泛型对象
     * @return
     */
    private E createModel() {
        try {
            Class<E> modelClazz = this.modelClazz;
            return modelClazz.newInstance();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
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
