package org.opsli.core.base.service.interfaces;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.base.service
 * @Author: Parker
 * @CreateTime: 2020-09-17 12:33
 * @Description: Opsli BaseServiceInterface
 */
public interface BaseServiceInterface<T> extends IService<T> {

    /**
     * 获得当前Service Clazz
     * @return
     */
    Class<?> getServiceClazz();

}
