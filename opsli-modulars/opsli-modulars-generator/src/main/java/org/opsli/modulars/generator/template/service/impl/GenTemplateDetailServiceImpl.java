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
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.FieldUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.modulars.generator.template.entity.GenTemplateDetail;
import org.opsli.modulars.generator.template.mapper.GenTemplateDetailMapper;
import org.opsli.modulars.generator.template.service.IGenTemplateDetailService;
import org.opsli.modulars.generator.template.wrapper.GenTemplateDetailModel;
import org.opsli.plugins.generator.utils.GenTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 代码模板详情 Service Impl
 *
 * @author Parker
 * @date 2021-05-28 17:12:38
 */
@Service
public class GenTemplateDetailServiceImpl extends CrudServiceImpl<GenTemplateDetailMapper, GenTemplateDetail, GenTemplateDetailModel>
    implements IGenTemplateDetailService {

    @Autowired(required = false)
    private GenTemplateDetailMapper mapper;


    /**
     * 新增
     * @param model model 数据模型
     * @return DictDetailModel
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public GenTemplateDetailModel insert(GenTemplateDetailModel model) {
        GenTemplateDetailModel ret = super.insert(model);
        if(ret != null){
            // 删除缓存
            this.clearCache(Collections.singletonList(
                    ret.getParentId()
            ));
        }

        return ret;
    }


    /**
     * 修改
     * @param model model 数据模型
     * @return GenTemplateDetailModel
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public GenTemplateDetailModel update(GenTemplateDetailModel model) {
        // 旧数据 用于删除老缓存
        GenTemplateDetailModel oldModel = this.get(model);

        GenTemplateDetailModel ret = super.update(model);
        if(ret != null){
            // 删除缓存
            this.clearCache(Collections.singletonList(
                    oldModel.getParentId()
            ));
        }

        return ret;
    }

    /**
     * 删除
     * @param id ID
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        GenTemplateDetailModel baseModel = this.get(id);
        boolean ret = super.delete(id);
        if(ret){
            // 删除缓存
            this.clearCache(Collections.singletonList(
                    baseModel.getParentId()
            ));
        }
        return ret;
    }

    /**
     * 删除 - 多个
     * @param ids id数组
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAll(String[] ids) {
        QueryBuilder<GenTemplateDetail> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<GenTemplateDetail> queryWrapper = queryBuilder.build();
        List<?> idList = Convert.toList(ids);
        queryWrapper.in(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID),idList);
        List<GenTemplateDetail> list = this.findList(queryWrapper);
        boolean ret = super.deleteAll(ids);

        if(ret){
            if(CollUtil.isNotEmpty(list)){
                Set<String> parentIds = new HashSet<>();
                // 封装数据
                for (GenTemplateDetail genTemplateDetail : list) {
                    parentIds.add(genTemplateDetail.getParentId());
                }

                List<String> parentIdList = Lists.newArrayListWithCapacity(parentIds.size());

                // 删除缓存
                this.clearCache(parentIdList);
            }
        }
        return ret;
    }

    /**
     * 根据 父类ID 全部删除
     * @param parentId 父类ID
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delByParent(String parentId) {
        if(StringUtils.isEmpty(parentId)) {
            return false;
        }

        String key = FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID);
        QueryBuilder<GenTemplateDetail> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<GenTemplateDetail> queryWrapper = queryBuilder.build();
        queryWrapper.eq(key, parentId);

        boolean removeFlag = super.remove(queryWrapper);
        if(removeFlag){
            // 删除缓存
            this.clearCache(Collections.singletonList(
                    parentId
            ));
        }
        return removeFlag;
    }

    /**
     * 根据 父类ID 全部删除
     * @param parentId 父类ID
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<GenTemplateDetailModel> findListByParent(String parentId) {
        if(StringUtils.isEmpty(parentId)) {
            return ListUtil.empty();
        }

        String key = FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID);
        QueryBuilder<GenTemplateDetail> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<GenTemplateDetail> queryWrapper = queryBuilder.build();
        queryWrapper.eq(key, parentId);

        return super.transformTs2Ms(this.findList(queryWrapper));
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
