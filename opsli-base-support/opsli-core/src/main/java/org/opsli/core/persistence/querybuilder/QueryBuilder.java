package org.opsli.core.persistence.querybuilder;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.opsli.core.base.entity.BaseEntity;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.persistence.querybuilder
 * @Author: Parker
 * @CreateTime: 2020-09-21 23:53
 * @Description: 查询构造器
 */
public interface QueryBuilder<T extends BaseEntity> {

    /**
     * 构造器
     * @param <T>
     * @return
     */
    QueryWrapper<T> build();

}
