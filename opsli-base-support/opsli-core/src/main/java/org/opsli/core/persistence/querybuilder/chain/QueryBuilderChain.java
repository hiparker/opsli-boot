package org.opsli.core.persistence.querybuilder.chain;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.opsli.core.base.entity.BaseEntity;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.persistence.querybuilder.chain
 * @Author: Parker
 * @CreateTime: 2020-09-22 10:35
 * @Description: 查询构建器责任链
 */
public interface QueryBuilderChain {

    /**
     * 执行
     * @param wrapper
     * @param <T>
     */
    <T extends BaseEntity> QueryWrapper<T> handler(Class<T> entityClazz, QueryWrapper<T> wrapper);

}
