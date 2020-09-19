package org.opsli.core.base.concroller;


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
import java.lang.reflect.ParameterizedType;

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
    /** Entity Clazz 类 */
    protected Class<T> entityClazz;

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
        Class<E> tClass = (Class<E>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

    /**
     * 获得 泛型 Clazz
     * @return
     */
    private Class<T> getEntityClass(){
        Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return tClass;
    }

}
