/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.core.persistence.querybuilder.chain;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.utils.FieldUtil;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.core.utils.UserUtil;

/**
 * 组织赋值处理
 *
 * @author Parker
 * @date 2020-09-13 19:36
 */
public class QueryOrgHandler implements QueryBuilderChain{

    /**
     * 子 责任链
     */
    private QueryBuilderChain queryBuilderChain;


    public QueryOrgHandler(){}

    /**
     * 构造函数
     * @param queryBuilderChain 责任链
     */
    public QueryOrgHandler(QueryBuilderChain queryBuilderChain){
        this.queryBuilderChain = queryBuilderChain;
    }

    @Override
    public <T extends BaseEntity> QueryWrapper<T> handler(Class<T> entityClazz, QueryWrapper<T> wrapper) {
        // 执行 子 责任链
        if(queryBuilderChain != null){
            wrapper = queryBuilderChain.handler(entityClazz, wrapper);
        }

        // 自身责任 -- 判断组织
        boolean flag = ReflectUtil.hasField(entityClazz, MyBatisConstants.FIELD_ORG_GROUP);
        if(flag) {
            // 1. 获得 用户 数据权限

            // 2. 获得 角色 数据权限

        }


        return wrapper;
    }

}
