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
package org.opsli.modulars.system.area.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.area.SysAreaModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.FieldUtil;
import org.opsli.core.base.entity.HasChildren;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.area.entity.SysArea;
import org.opsli.modulars.system.area.mapper.SysAreaMapper;
import org.opsli.modulars.system.area.service.ISysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


/**
* @BelongsProject: opsli-boot
* @BelongsPackage: org.opsli.modulars.system.area.service.impl
* @Author: Parker
* @CreateTime: 2020-11-28 18:59:59
* @Description: 地域表 Service Impl
*/

/**
 * 地域表 Service Impl
 *
 * @author Parker
 * @date 2020-11-28 18:59:59
 */
@Service
public class SysAreaServiceImpl extends CrudServiceImpl<SysAreaMapper, SysArea, SysAreaModel>
    implements ISysAreaService {

    @Autowired(required = false)
    private SysAreaMapper mapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysAreaModel insert(SysAreaModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        boolean verificationByCode = this.uniqueVerificationByCode(model);
        if(!verificationByCode){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_AREA_UNIQUE);
        }

        // 如果上级ID 为空 则默认为 0
        if(StringUtils.isEmpty(model.getParentId())){
            model.setParentId("0");
        }

        return super.insert(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysAreaModel update(SysAreaModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        boolean verificationByCode = this.uniqueVerificationByCode(model);
        if(!verificationByCode){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_AREA_UNIQUE);
        }

        return super.update(model);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        // 先删除子数据
        this.deleteByParentId(id);

        return super.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAll(String[] ids) {
        // 先删除子数据
        for (String id : ids) {
            this.deleteByParentId(id);
        }

        return super.deleteAll(ids);
    }

    /**
     * 逐级删除子数据
     * @param parentId 父级节点
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByParentId(String parentId) {
        boolean ret = false;
        QueryBuilder<SysArea> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysArea> queryWrapper = queryBuilder.build();
        queryWrapper.eq(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentId);
        List<SysArea> childList = super.findList(queryWrapper);
        for (SysArea child : childList) {
            super.delete(child.getId());
            // 逐级删除子数据
            ret = this.deleteByParentId(child.getId());
        }
        return ret;
    }


    /**
     * 唯一验证
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public boolean uniqueVerificationByCode(SysAreaModel model){
        if(model == null){
            return false;
        }
        QueryWrapper<SysArea> wrapper = new QueryWrapper<>();

        // code 唯一
        wrapper.eq("area_code", model.getAreaCode());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        return super.count(wrapper) == 0;
    }


    /**
     * 是否有下级
     * @param parentIds 父级节点集合
     * @return List
     */
    @Override
    @Transactional(readOnly = true)
    public List<HasChildren> hasChildren(Set<String> parentIds){
        if(CollUtil.isEmpty(parentIds)){
            return null;
        }
        QueryWrapper<SysArea> wrapper = new QueryWrapper<>();

        wrapper.in(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentIds)
                .eq(MyBatisConstants.FIELD_DELETE_LOGIC,  DictType.NO_YES_NO.getValue())
                .groupBy(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID));

        return mapper.hasChildren(wrapper);
    }
}
