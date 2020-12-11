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
package org.opsli.modulars.creater.column.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.utils.ValidationUtil;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.creater.exception.CreaterException;
import org.opsli.core.creater.msg.CreaterMsg;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.modulars.creater.column.entity.CreaterTableColumn;
import org.opsli.modulars.creater.column.mapper.TableColumnMapper;
import org.opsli.modulars.creater.column.service.ITableColumnService;
import org.opsli.modulars.creater.column.wrapper.CreaterTableColumnModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @BelongsProject: opsli-boot
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:34
 * @Description: 代码生成器 - 表结构 接口实现类
 */
@Service
public class TableColumnServiceImpl extends CrudServiceImpl<TableColumnMapper, CreaterTableColumn, CreaterTableColumnModel>
        implements ITableColumnService {

    @Autowired(required = false)
    private TableColumnMapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreaterTableColumnModel insert(CreaterTableColumnModel model) {
        // 验证对象
        ValidationUtil.verify(model);

        if(model == null){
            return null;
        }

        CreaterTableColumn entity = super.transformM2T(model);
        // 唯一验证
        Integer count = mapper.uniqueVerificationByFieldName(entity);
        if(count != null && count > 0){
            // 重复
         throw new CreaterException(CreaterMsg.EXCEPTION_TABLE_COLUMN_FIELD_NAME_REPEAT);
        }

        return super.insert(model);
    }

    /**
     * 批量新增
     * @param models model 数据模型
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertBatch(List<CreaterTableColumnModel> models) {

        if(models == null || models.size() == 0){
            return false;
        }

        for (CreaterTableColumnModel model : models) {

            // 验证对象合法性
            ValidationUtil.verify(model);

            // 唯一验证
            CreaterTableColumn entity = super.transformM2T(model);
            Integer count = mapper.uniqueVerificationByFieldName(entity);
            if(count != null && count > 0){
                // 重复
                throw new CreaterException(CreaterMsg.EXCEPTION_TABLE_COLUMN_FIELD_NAME_REPEAT);
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


        List<CreaterTableColumn> entitys = transformMs2Ts(models);
        return super.saveBatch(entitys);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreaterTableColumnModel update(CreaterTableColumnModel model) {
        // 验证对象
        ValidationUtil.verify(model);

        if(model == null){
            return null;
        }

        CreaterTableColumn entity = super.transformM2T(model);
        // 唯一验证
        Integer count = mapper.uniqueVerificationByFieldName(entity);
        if(count != null && count > 0){
            // 重复
            throw new CreaterException(CreaterMsg.EXCEPTION_TABLE_COLUMN_FIELD_NAME_REPEAT);
        }

        return super.update(model);
    }

    @Override
    public List<CreaterTableColumnModel> getByTableId(String tableId) {
        if( StringUtils.isEmpty(tableId) ){
            return null;
        }

        QueryBuilder<CreaterTableColumn> queryBuilder =
                new GenQueryBuilder<>();
        QueryWrapper<CreaterTableColumn> wrapper = queryBuilder.build();
        wrapper.eq("table_id", tableId);
        wrapper.orderByAsc("sort");
        List<CreaterTableColumn> tableColumnList = this.findList(wrapper);

        return super.transformTs2Ms(tableColumnList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delByTableId(String tableId){

        QueryBuilder<CreaterTableColumn> queryBuilder =
                new GenQueryBuilder<>();
        QueryWrapper<CreaterTableColumn> wrapper = queryBuilder.build();
        wrapper.eq("table_id", tableId);
        super.remove(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delByTableIds(String[] tableIds){
        if(tableIds != null){
            for (String tableId : tableIds) {
                QueryBuilder<CreaterTableColumn> queryBuilder =
                        new GenQueryBuilder<>();
                QueryWrapper<CreaterTableColumn> wrapper = queryBuilder.build();
                wrapper.eq("table_id", tableId);
                super.remove(wrapper);
            }
        }
    }
}


