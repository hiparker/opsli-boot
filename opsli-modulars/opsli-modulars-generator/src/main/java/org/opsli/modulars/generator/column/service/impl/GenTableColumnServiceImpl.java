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
package org.opsli.modulars.generator.column.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.plugins.generator.exception.GeneratorException;
import org.opsli.plugins.generator.msg.GeneratorMsg;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.utils.ValidatorUtil;
import org.opsli.modulars.generator.column.entity.GenTableColumn;
import org.opsli.modulars.generator.column.mapper.GenTableColumnMapper;
import org.opsli.modulars.generator.column.service.IGenTableColumnService;
import org.opsli.modulars.generator.column.wrapper.GenTableColumnModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 代码生成器 - 表结构 接口实现类
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
@Service
public class GenTableColumnServiceImpl extends CrudServiceImpl<GenTableColumnMapper, GenTableColumn, GenTableColumnModel>
        implements IGenTableColumnService {

    @Autowired(required = false)
    private GenTableColumnMapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenTableColumnModel insert(GenTableColumnModel model) {
        // 验证对象
        ValidatorUtil.verify(model);

        if(model == null){
            return null;
        }

        // 唯一验证
        boolean verificationByFieldName = this.uniqueVerificationByFieldName(model);
        if(!verificationByFieldName){
            // 重复
            throw new GeneratorException(GeneratorMsg.EXCEPTION_TABLE_COLUMN_FIELD_NAME_REPEAT);
        }

        return super.insert(model);
    }

    /**
     * 批量新增
     * @param models model 数据模型
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertBatch(List<GenTableColumnModel> models) {

        if(models == null || models.size() == 0){
            return false;
        }

        for (GenTableColumnModel model : models) {

            // 验证对象合法性
            ValidatorUtil.verify(model);

            // 唯一验证
            boolean verificationByFieldName = this.uniqueVerificationByFieldName(model);
            if(!verificationByFieldName){
                // 重复
                throw new GeneratorException(GeneratorMsg.EXCEPTION_TABLE_COLUMN_FIELD_NAME_REPEAT);
            }

            // 默认清空 创建人和修改人
            if(model.getIzManual() != null && !model.getIzManual()){
                model.setCreateBy(null);
                model.setCreateTime(null);
                model.setUpdateBy(null);
                model.setUpdateTime(null);
                model.setId(null);
            }
        }


        List<GenTableColumn> entityList = transformMs2Ts(models);
        return super.saveBatch(entityList);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenTableColumnModel update(GenTableColumnModel model) {
        // 验证对象
        ValidatorUtil.verify(model);

        if(model == null){
            return null;
        }

        // 唯一验证
        boolean verificationByFieldName = this.uniqueVerificationByFieldName(model);
        if(!verificationByFieldName){
            // 重复
            throw new GeneratorException(GeneratorMsg.EXCEPTION_TABLE_COLUMN_FIELD_NAME_REPEAT);
        }

        return super.update(model);
    }

    @Override
    public List<GenTableColumnModel> getByTableId(String tableId) {
        if( StringUtils.isEmpty(tableId) ){
            return null;
        }

        QueryBuilder<GenTableColumn> queryBuilder =
                new GenQueryBuilder<>();
        QueryWrapper<GenTableColumn> wrapper = queryBuilder.build();
        wrapper.eq("table_id", tableId);
        wrapper.orderByAsc("sort");
        List<GenTableColumn> tableColumnList = this.findList(wrapper);

        return super.transformTs2Ms(tableColumnList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delByTableId(String tableId){

        QueryBuilder<GenTableColumn> queryBuilder =
                new GenQueryBuilder<>();
        QueryWrapper<GenTableColumn> wrapper = queryBuilder.build();
        wrapper.eq("table_id", tableId);
        super.remove(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delByTableIds(String[] tableIds){
        if(tableIds != null){
            for (String tableId : tableIds) {
                QueryBuilder<GenTableColumn> queryBuilder =
                        new GenQueryBuilder<>();
                QueryWrapper<GenTableColumn> wrapper = queryBuilder.build();
                wrapper.eq("table_id", tableId);
                super.remove(wrapper);
            }
        }
    }


    // ========================

    /**
     * 唯一验证
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public boolean uniqueVerificationByFieldName(GenTableColumnModel model){
        if(model == null){
            return false;
        }
        QueryWrapper<GenTableColumn> wrapper = new QueryWrapper<>();

        // code 唯一
        wrapper.eq("table_id", model.getTableId())
                .eq("field_name", model.getFieldName());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        return super.count(wrapper) == 0;
    }
}


