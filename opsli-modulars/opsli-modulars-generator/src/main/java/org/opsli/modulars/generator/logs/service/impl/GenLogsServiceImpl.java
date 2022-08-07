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
package org.opsli.modulars.generator.logs.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.menu.MenuApi;
import org.opsli.api.wrapper.system.menu.MenuFullModel;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.plugins.generator.exception.GeneratorException;
import org.opsli.plugins.generator.msg.GeneratorMsg;
import org.opsli.plugins.generator.strategy.create.CodeBuilder;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.modulars.generator.column.service.IGenTableColumnService;
import org.opsli.modulars.generator.column.wrapper.GenTableColumnModel;
import org.opsli.modulars.generator.logs.entity.GenLogs;
import org.opsli.modulars.generator.logs.mapper.GenLogsMapper;
import org.opsli.modulars.generator.logs.service.IGenLogsService;
import org.opsli.modulars.generator.logs.wrapper.GenBuilderModel;
import org.opsli.modulars.generator.logs.wrapper.GenLogsModel;
import org.opsli.modulars.generator.table.service.IGenTableService;
import org.opsli.modulars.generator.table.wrapper.GenTableAndColumnModel;
import org.opsli.modulars.generator.table.wrapper.GenTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 代码生成器 - 日志 接口实现类
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
@Service
public class GenLogsServiceImpl extends CrudServiceImpl<GenLogsMapper, GenLogs, GenLogsModel>
        implements IGenLogsService {

    @Autowired(required = false)
    private GenLogsMapper mapper;
    @Autowired
    private IGenTableService iGenTableService;
    @Autowired
    private IGenTableColumnService iGenTableColumnService;
    @Autowired
    private MenuApi menuApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delByTableId(String tableId) {
        QueryBuilder<GenLogs> queryBuilder =
                new GenQueryBuilder<>();
        QueryWrapper<GenLogs> wrapper = queryBuilder.build();
        wrapper.eq("table_id", tableId);
        super.remove(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delByTableIds(String[] tableIds) {
        if(tableIds != null){
            for (String tableId : tableIds) {
                QueryBuilder<GenLogs> queryBuilder =
                        new GenQueryBuilder<>();
                QueryWrapper<GenLogs> wrapper = queryBuilder.build();
                wrapper.eq("table_id", tableId);
                super.remove(wrapper);
            }
        }
    }

    @Override
    public GenLogsModel getByTableId(String tableId) {
        QueryBuilder<GenLogs> queryBuilder =
                new GenQueryBuilder<>();
        QueryWrapper<GenLogs> wrapper = queryBuilder.build();
        wrapper.eq("table_id", tableId);

        GenLogsModel model = null;
        Page<GenLogs, GenLogsModel> page = new Page<>(1,1);
        page.setQueryWrapper(wrapper);
        page = this.findPage(page);
        if(page != null && CollUtil.isNotEmpty(page.getList())){
            model = page.getList().get(0);
        }

        return model;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(GenLogsModel model, HttpServletResponse response) {
        if(model == null){
            // 生成失败，数据为空
            throw new GeneratorException(GeneratorMsg.EXCEPTION_CREATE_NULL);
        }
        model.setVersion(null);
        // 强制设置为小写 保持权限统一
        model.setModuleName(null==model.getModuleName()
                ?null:model.getModuleName().toLowerCase());
        model.setSubModuleName(null==model.getSubModuleName()
                ?null:model.getSubModuleName().toLowerCase());


        GenLogsModel saveModel = this.save(model);
        if(saveModel != null){
            GenTableModel genTableModel = iGenTableService.get(saveModel.getTableId());
            if(genTableModel == null){
                // 生成失败，暂无表数据
                throw new GeneratorException(GeneratorMsg.EXCEPTION_CREATE_TABLE_NULL);
            }

            GenTableAndColumnModel currTableModel = WrapperUtil.transformInstance(
                    genTableModel, GenTableAndColumnModel.class
            );
            List<GenTableColumnModel> columnModelList = iGenTableColumnService.
                    getByTableId(currTableModel.getId());
            if(columnModelList == null || columnModelList.isEmpty()){
                // 生成失败，暂无表字段
                throw new GeneratorException(GeneratorMsg.EXCEPTION_CREATE_FIELD_NULL);
            }


            // 赋值表字段
            currTableModel.setColumnList(columnModelList);

            // 生成代码
            GenBuilderModel builderModel = WrapperUtil.transformInstance(
                    saveModel, GenBuilderModel.class
            );
            builderModel.setModel(currTableModel);

            // 生成代码
            CodeBuilder.INSTANCE.build(builderModel, response);

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createMenu(String menuParentId, String tableId) {
        if(StringUtils.isEmpty(menuParentId)){
            // 上级菜单不可为空
            throw new GeneratorException(GeneratorMsg.EXCEPTION_CREATE_MENU_PARENT_NULL);
        }

        GenLogsModel byTableId = this.getByTableId(tableId);
        if(byTableId == null){
            // 生成菜单失败，请先 生成代码
            throw new GeneratorException(GeneratorMsg.EXCEPTION_CREATE_MENU_CODE_NULL);
        }

        // 完整新增菜单
        MenuFullModel menuFullModel = new MenuFullModel();
        menuFullModel.setParentId(menuParentId);
        menuFullModel.setTitle(byTableId.getCodeTitle());
        menuFullModel.setModuleName(byTableId.getModuleName());
        menuFullModel.setSubModuleName(byTableId.getSubModuleName());
        ResultWrapper<?> insertRet = menuApi.saveMenuByFull(menuFullModel);

        return ResultWrapper.isSuccess(insertRet);
    }


}


