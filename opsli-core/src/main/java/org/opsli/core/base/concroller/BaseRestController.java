package org.opsli.core.base.concroller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.annotation.EnableHotData;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.msg.CommonMsg;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.core.cache.local.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.lang.reflect.*;

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
public abstract class BaseRestController <T extends BaseEntity, S extends CrudServiceInterface<T>>{

    /** 开启热点数据状态 */
    protected boolean hotDataFlag = false;

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
    public T get(@RequestParam(required=false) String id) {
        T entity = null;
        if (StringUtils.isNotBlank(id)){
            // 如果开启缓存 先从缓存读
            if(hotDataFlag){
                entity = CacheUtil.get(id, entityClazz);
                if(entity != null){
                    return entity;
                }
            }
            // 如果缓存没读到 则去数据库读
            entity = IService.get(id);
            if(entity != null){
                // 如果开启缓存 将数据库查询对象 存如缓存
                if(hotDataFlag){
                    CacheUtil.put(id, entity);
                }
            }
        }
        if (entity == null){
            try {
                // 创建泛型对象
                entity = this.createModel();
            }catch (Exception e){
                log.error(CommonMsg.EXCEPTION_CONTROLLER_MODEL.toString()+" : {}",e.getMessage());
                throw new ServiceException(CommonMsg.EXCEPTION_CONTROLLER_MODEL);
            }
        }
        return entity;
    }





    // =================================================

    @PostConstruct
    public void init(){
        try {
            this.entityClazz = this.getModelClass();
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
     * 创建泛型对象
     * @return
     */
    private T createModel() {
        try {
            Class<T> modelClazz = this.entityClazz;
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
    private Class<T> getModelClass(){
        Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

}
