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
package org.opsli.modulars.system.dict.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.dict.DictDetailModel;
import org.opsli.api.wrapper.system.dict.DictModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.FieldUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.utils.DictUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.dict.entity.SysDictDetail;
import org.opsli.modulars.system.dict.mapper.DictDetailMapper;
import org.opsli.modulars.system.dict.service.IDictDetailService;
import org.opsli.modulars.system.dict.service.IDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 数据字典 明细 接口实现类
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Service
public class DictDetailServiceImpl extends CrudServiceImpl<DictDetailMapper, SysDictDetail, DictDetailModel> implements IDictDetailService {

    @Autowired(required = false)
    private DictDetailMapper mapper;

    @Lazy
    @Autowired
    private IDictService iDictService;

    /**
     * 新增
     * @param model model 数据模型
     * @return DictDetailModel
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public DictDetailModel insert(DictDetailModel model) {

        // 唯一验证
        boolean verificationByNameOrValue = this.uniqueVerificationByNameOrValue(model);
        if(!verificationByNameOrValue){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_DICT_DETAIL_UNIQUE);
        }

        DictDetailModel ret = super.insert(model);
        if(ret != null){
            List<DictDetailModel> listByTypeCode = this.findListByTypeCode(ret.getTypeCode());
            if(CollUtil.isNotEmpty(listByTypeCode)){
                // 删除缓存
                this.clearCache(Collections.singletonList(
                        ret.getTypeCode()
                ));
            }
        }

        return ret;
    }

    /**
     * 修改
     * @param model model 数据模型
     * @return DictDetailModel
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public DictDetailModel update(DictDetailModel model) {

        // 唯一验证
        boolean verificationByNameOrValue = this.uniqueVerificationByNameOrValue(model);
        if(!verificationByNameOrValue){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_DICT_DETAIL_UNIQUE);
        }

        // 旧数据 用于删除老缓存
        DictDetailModel oldModel = this.get(model);

        DictDetailModel ret = super.update(model);
        if(ret != null){
            List<DictDetailModel> listByTypeCode = this.findListByTypeCode(oldModel.getTypeCode());
            if(CollUtil.isNotEmpty(listByTypeCode)){
                // 删除缓存
                this.clearCache(Collections.singletonList(
                        oldModel.getTypeCode()
                ));
            }
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
        DictDetailModel dictModel = this.get(id);
        boolean ret = super.delete(id);
        if(ret){
            // 删除缓存
            this.clearCache(Collections.singletonList(
                    dictModel.getTypeCode()
            ));
        }
        return ret;
    }

    /**
     * 删除
     * @param model 数据模型
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(DictDetailModel model) {
        DictDetailModel dictModel = this.get(model);
        boolean ret = super.delete(model);
        if(ret){
            // 删除缓存
            this.clearCache(Collections.singletonList(
                    dictModel.getTypeCode()
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
        QueryBuilder<SysDictDetail> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysDictDetail> queryWrapper = queryBuilder.build();
        List<?> idList = Convert.toList(ids);
        queryWrapper.in(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID),idList);
        List<SysDictDetail> list = this.findList(queryWrapper);
        boolean ret = super.deleteAll(ids);

        if(ret){
            if(CollUtil.isNotEmpty(list)){
                Set<String> typeCodes = new HashSet<>();
                // 封装数据
                for (SysDictDetail sysDictDetail : list) {
                    typeCodes.add(sysDictDetail.getTypeCode());
                }

                List<String> typeCodeList = Lists.newArrayListWithCapacity(typeCodes.size());

                // 删除缓存
                this.clearCache(typeCodeList);
            }

        }
        return ret;
    }

    /**
     * 删除 - 多个
     * @param models 封装模型
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAll(Collection<DictDetailModel> models) {

        QueryBuilder<SysDictDetail> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysDictDetail> queryWrapper = queryBuilder.build();

        List<String> idList = Lists.newArrayListWithCapacity(models.size());
        for (DictDetailModel model : models) {
            idList.add(model.getId());
        }
        queryWrapper.in(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID),idList);

        List<SysDictDetail> list = this.findList(queryWrapper);

        boolean ret = super.deleteAll(models);

        if(ret){
            if(list != null && list.size() > 0){
                Set<String> typeCodes = new HashSet<>();
                // 封装数据
                for (SysDictDetail sysDictDetail : list) {
                    typeCodes.add(sysDictDetail.getTypeCode());
                }

                List<String> typeCodeList = Lists.newArrayListWithCapacity(typeCodes.size());

                // 删除缓存
                this.clearCache(typeCodeList);
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
        if(StringUtils.isEmpty(parentId)){
            return false;
        }

        String key = FieldUtil.humpToUnderline("typeId");
        QueryBuilder<SysDictDetail> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysDictDetail> queryWrapper = queryBuilder.build();
        queryWrapper.eq(key, parentId);
        boolean removeFlag = super.remove(queryWrapper);
        if(removeFlag){
            DictModel dictModel = iDictService.get(parentId);
            if(dictModel != null){
                // 删除缓存
                this.clearCache(Collections.singletonList(
                        dictModel.getTypeCode()
                ));
            }
        }
        return removeFlag;
    }

    /**
     * 根据字典编号 查询出所有字典
     *
     * @param typeCode 字典编号
     * @return List
     */
    @Override
    public List<DictDetailModel> findListByTypeCode(String typeCode) {
        if(StringUtils.isEmpty(typeCode)){
            return null;
        }

        String key = FieldUtil.humpToUnderline("typeCode");
        String deleted = FieldUtil.humpToUnderline("deleted");

        QueryBuilder<SysDictDetail> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysDictDetail> queryWrapper = queryBuilder.build();
        queryWrapper.eq(key, typeCode);
        queryWrapper.eq(deleted, '0');
        queryWrapper.orderByAsc("sort_no");
        List<SysDictDetail> list = this.findList(queryWrapper);
        // 转化对象
        return super.transformTs2Ms(list);
    }


    /**
     * 唯一验证 名称
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public boolean uniqueVerificationByNameOrValue(DictDetailModel model){
        if(model == null){
            return false;
        }
        QueryWrapper<SysDictDetail> wrapper = new QueryWrapper<>();

        // name 唯一
        wrapper.eq("type_code", model.getTypeCode());

        // 名称 或者 Val 重复
        wrapper.and(wra ->
                wra.eq("dict_name",
                        model.getDictName()).or().eq("dict_value", model.getDictValue()));

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        return super.count(wrapper) == 0;
    }

    // ================

    /**
     * 清除缓存
     * @param typeCodeList 类型编号集合
     */
    private void clearCache(List<String> typeCodeList) {
        // 删除缓存
        if (CollUtil.isNotEmpty(typeCodeList)) {
            int cacheCount = 0;
            for (String typeCode : typeCodeList) {
                cacheCount++;
                boolean tmp = DictUtil.delAll(typeCode);
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


