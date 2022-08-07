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

package org.opsli.modulars.generator.template.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.FieldUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.modulars.generator.template.entity.GenTemplate;
import org.opsli.modulars.generator.template.mapper.GenTemplateMapper;
import org.opsli.modulars.generator.template.service.IGenTemplateDetailService;
import org.opsli.modulars.generator.template.service.IGenTemplateService;
import org.opsli.modulars.generator.template.wrapper.GenTemplateAndDetailModel;
import org.opsli.modulars.generator.template.wrapper.GenTemplateCopyModel;
import org.opsli.modulars.generator.template.wrapper.GenTemplateDetailModel;
import org.opsli.modulars.generator.template.wrapper.GenTemplateModel;
import org.opsli.plugins.generator.exception.GeneratorException;
import org.opsli.plugins.generator.msg.GeneratorMsg;
import org.opsli.plugins.generator.utils.GenTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


/**
 * 代码模板 Service Impl
 *
 * @author Parker
 * @date 2021-05-27 14:33:49
 */
@Service
public class GenTemplateServiceImpl extends CrudServiceImpl<GenTemplateMapper, GenTemplate, GenTemplateModel>
    implements IGenTemplateService {

    @Autowired(required = false)
    private GenTemplateMapper mapper;
    @Autowired
    private IGenTemplateDetailService iGenTemplateDetailService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public GenTemplateModel insertAndDetail(GenTemplateAndDetailModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        boolean verificationByTemplateName = this.uniqueVerificationByTemplateName(model);
        if(!verificationByTemplateName){
            // 重复
            throw new GeneratorException(GeneratorMsg.EXCEPTION_TEMPLATE_NAME_REPEAT);
        }

        GenTemplateModel insertModel = super.insert(
                WrapperUtil.transformInstance(model, GenTemplateModel.class));

        if(insertModel != null){

            // 保存模型明细
            List<GenTemplateDetailModel> detailList = model.getDetailList();
            if(!CollUtil.isEmpty(detailList)){
                for (GenTemplateDetailModel templateDetailModel : detailList) {
                    templateDetailModel.setParentId(insertModel.getId());
                    iGenTemplateDetailService.insert(templateDetailModel);
                }
            }
        }

        return insertModel;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public GenTemplateModel updateAndDetail(GenTemplateAndDetailModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        boolean verificationByTemplateName = this.uniqueVerificationByTemplateName(model);
        if(!verificationByTemplateName){
            // 重复
            throw new GeneratorException(GeneratorMsg.EXCEPTION_TEMPLATE_NAME_REPEAT);
        }

        GenTemplateModel updateModel = super.update(
                WrapperUtil.transformInstance(model, GenTemplateModel.class));

        if(updateModel != null){

            // 删除子表明细
            iGenTemplateDetailService.delByParent(model.getId());
            // 保存模型明细
            List<GenTemplateDetailModel> detailList = model.getDetailList();
            if(!CollUtil.isEmpty(detailList)){
                for (GenTemplateDetailModel templateDetailModel : detailList) {
                    templateDetailModel.setParentId(updateModel.getId());
                    iGenTemplateDetailService.insert(templateDetailModel);
                }
            }

            // 删除缓存
            this.clearCache(Collections.singletonList(
                    updateModel.getId()
            ));
        }

        return updateModel;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public GenTemplateModel copy(GenTemplateCopyModel model) {
        if(model == null){
            return null;
        }

        GenTemplateModel genTemplateModel = this.get(model.getId());

        model.setId(null);

        // 唯一验证
        boolean verificationByTemplateName = this.uniqueVerificationByTemplateName(
                WrapperUtil.transformInstance(model, GenTemplateAndDetailModel.class));
        if(!verificationByTemplateName){
            // 重复
            throw new GeneratorException(GeneratorMsg.EXCEPTION_TEMPLATE_NAME_REPEAT);
        }

        if(genTemplateModel == null){
            // 暂无该模板
            throw new GeneratorException(GeneratorMsg.EXCEPTION_TEMPLATE_COPY_NULL);
        }

        // 设置模板名称
        genTemplateModel.setTempName(model.getTempName());

        // 获得子类模板
        List<GenTemplateDetailModel> genTemplateDetailModelList =
                iGenTemplateDetailService.findListByParent(genTemplateModel.getId());

        GenTemplateModel insertModel = super.insert(
                WrapperUtil.transformInstance(genTemplateModel, GenTemplateModel.class));

        if(insertModel != null){
            // 保存模型明细
            for (GenTemplateDetailModel templateDetailModel : genTemplateDetailModelList) {
                templateDetailModel.setParentId(insertModel.getId());
                iGenTemplateDetailService.insert(templateDetailModel);
            }
        }

        return insertModel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        GenTemplateModel base = this.get(id);
        if (base == null) {
            return false;
        }

        // 验证 是否允许删除
        QueryWrapper<GenTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("table_type", base.getTableType());
        queryWrapper.notIn(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID), id);
        long count = this.count(queryWrapper);
        if(count == 0){
            // 代码模板同一表类型下，至少保障有一个模板
            throw new GeneratorException(GeneratorMsg.EXCEPTION_TEMPLATE_AT_LEAST_ONE);
        }

        iGenTemplateDetailService.delByParent(id);

        return super.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAll(String[] ids) {
        QueryBuilder<GenTemplate> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<GenTemplate> queryWrapper = queryBuilder.build();
        queryWrapper.in(MyBatisConstants.FIELD_ID, Convert.toList(String.class, ids));

        // 查询数据 抽取Map 字典 便于后续查询
        List<GenTemplate> baseList = this.findList(queryWrapper);
        for (GenTemplate base : baseList) {
            // 先删除子数据
            iGenTemplateDetailService.delByParent(base.getId());
        }

        return super.deleteAll(ids);
    }

    /**
     * 唯一验证
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public boolean uniqueVerificationByTemplateName(GenTemplateAndDetailModel model){
        if(model == null){
            return false;
        }
        QueryWrapper<GenTemplate> wrapper = new QueryWrapper<>();

        // code 唯一
        wrapper.eq("temp_name", model.getTempName());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        return super.count(wrapper) == 0;
    }

    // ====================


    /**
     * 清除缓存
     * @param parentIdList 父级ID集合
     */
    private void clearCache(List<String> parentIdList) {
        // 删除缓存
        if (CollUtil.isNotEmpty(parentIdList)) {
            int cacheCount = 0;
            for (String parentId : parentIdList) {
                cacheCount++;
                boolean tmp = GenTemplateUtil.delAll(parentId);
                if(tmp){
                    cacheCount--;
                }
            }
            // 判断删除状态
            if(cacheCount != 0){
                // 删除缓存失败
                throw new ServiceException(CoreMsg.CACHE_DEL_EXCEPTION);
            }
        }
    }

}
