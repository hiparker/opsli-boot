package org.opsli.core.persistence.querybuilder.chain;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.utils.HumpUtil;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.core.utils.UserUtil;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.tenant
 * @Author: Parker
 * @CreateTime: 2020-09-22 00:50
 * @Description: 多租户赋值处理
 */
public class TenantHandler implements QueryBuilderChain{

    /**
     * 子 责任链
     */
    private QueryBuilderChain queryBuilderChain;


    public TenantHandler(){}

    /**
     * 构造函数
     * @param queryBuilderChain 责任链
     */
    public TenantHandler(QueryBuilderChain queryBuilderChain){
        this.queryBuilderChain = queryBuilderChain;
    }

    /**
     * 查询构建器处理
     * @param entityClazz
     * @param wrapper
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseEntity> QueryWrapper<T> handler(Class<T> entityClazz, QueryWrapper<T> wrapper) {
        // 执行责任链
        if(queryBuilderChain != null){
            wrapper = queryBuilderChain.handler(entityClazz, wrapper);
        }

        // 判断多租户
        boolean tenantFlag = ReflectUtil.hasField(entityClazz, MyBatisConstants.FIELD_TENANT);
        if(tenantFlag) {
            String tenantId = UserUtil.getTenantId();
            if (StringUtils.isNotEmpty(tenantId)) {
                wrapper.eq(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_TENANT), tenantId);
            }
        }
        return wrapper;

    }

}
