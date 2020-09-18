package org.opsli.core.base.service.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.core.base.service.interfaces.BaseServiceInterface;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.base.service
 * @Author: Parker
 * @CreateTime: 2020-09-17 12:33
 * @Description: Opsli Base Service
 */
public abstract class BaseService <M extends BaseMapper<T>, T extends BaseEntity>
        extends ServiceImpl<M, T> implements BaseServiceInterface<T> {

    @Override
    public Class<?> getServiceClazz() {
        return this.getClass();
    }
}
