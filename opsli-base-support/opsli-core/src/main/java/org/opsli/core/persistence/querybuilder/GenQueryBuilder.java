package org.opsli.core.persistence.querybuilder;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.opsli.core.base.entity.BaseEntity;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.persistence.querybuilder
 * @Author: Parker
 * @CreateTime: 2020-09-21 23:57
 * @Description: Web 条件构造器
 */
public class GenQueryBuilder<T extends BaseEntity>  implements QueryBuilder<T> {


    /**
     * 构造函数 只是生产 查询器
     */
    public GenQueryBuilder(){

    }

    @Override
    public QueryWrapper<T> build() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        return queryWrapper;
    }

}
