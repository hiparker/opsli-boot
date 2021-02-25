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
package org.opsli.modulars.system.org.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.org.SysOrgModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.HumpUtil;
import org.opsli.core.base.entity.HasChildren;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.chain.TenantHandler;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.org.entity.SysOrg;
import org.opsli.modulars.system.org.mapper.SysOrgMapper;
import org.opsli.modulars.system.org.service.ISysOrgService;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.service.IUserOrgRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
* @BelongsProject: opsli-boot
* @BelongsPackage: org.opsli.modulars.system.org.service.impl
* @Author: Parker
* @CreateTime: 2020-11-28 18:59:59
* @Description: 组织机构表 Service Impl
*/
@Service
public class SysOrgServiceImpl extends CrudServiceImpl<SysOrgMapper, SysOrg, SysOrgModel>
    implements ISysOrgService {

    /** 顶级ID */
    private static final String TOP_PARENT_ID = "0";

    @Autowired(required = false)
    private SysOrgMapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysOrgModel insert(SysOrgModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        Integer count = this.uniqueVerificationByCode(model);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ORG_UNIQUE);
        }

        // 如果上级ID 为空 则默认为 0
        if(StringUtils.isEmpty(model.getParentId())){
            model.setParentId(TOP_PARENT_ID);
        }

        // 如果上级ID不为空 且 不等于顶级ID
        if(StringUtils.isNotEmpty(model.getParentId()) &&
                !TOP_PARENT_ID.equals(model.getParentId())
            ){
            // 下级沿用上级租户ID
            SysOrgModel sysOrgModel = super.get(model.getParentId());
            model.setTenantId(sysOrgModel.getTenantId());
        }

        return super.insert(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysOrgModel update(SysOrgModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        Integer count = this.uniqueVerificationByCode(model);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ORG_UNIQUE);
        }

        // 如果上级ID不为空 且 不等于顶级ID
        if(StringUtils.isNotEmpty(model.getParentId()) &&
                !TOP_PARENT_ID.equals(model.getParentId())
            ){
            // 下级沿用上级租户ID
            SysOrgModel sysOrgModel = super.get(model.getParentId());
            model.setTenantId(sysOrgModel.getTenantId());
        }

        SysOrgModel sysOrgModel = super.get(model);
        // 如果 TenantId 发生变化 则需要更改 下级数据 租户ID
        if(sysOrgModel != null && !sysOrgModel.getTenantId().equals(
                model.getTenantId())){
            // 如果有组织还在被引用 则不允许操作该组织
            this.validationUsedByDel(Collections.singletonList(sysOrgModel.getId()));

            // 如果没有被引用 则逐级修改
            this.updateTenantByParentId(sysOrgModel.getId(), model.getTenantId());
        }

        // 修改
        return super.update(model);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        boolean ret;
        if(StringUtils.isEmpty(id)){
            return false;
        }

        // 如果有组织还在被引用 则不允许操作该组织
        this.validationUsedByDel(Collections.singletonList(id));

        ret = super.delete(id);
        // 删除子数据
        this.deleteByParentId(id);
        return ret;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAll(String[] ids) {
        boolean ret;
        if(ArrayUtils.isEmpty(ids)){
            return false;
        }

        // 如果有组织还在被引用 则不允许操作该组织
        this.validationUsedByDel(Convert.toList(String.class, ids));

        ret = super.deleteAll(ids);
        // 删除子数据
        for (String id : ids) {
            this.deleteByParentId(id);
        }

        return ret;
    }

    /**
     * 逐级修改机构下租户
     * @param parentId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateTenantByParentId(String parentId, String tenantId) {
        QueryBuilder<SysOrg> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysOrg> queryWrapper = queryBuilder.build();
        queryWrapper.eq(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentId);
        List<SysOrg> entityList = super.findList(queryWrapper);
        for (SysOrg sysOrg : entityList) {
            sysOrg.setTenantId(tenantId);
            super.updateById(sysOrg);
            // 逐级删除子数据
            this.updateTenantByParentId(sysOrg.getId(), tenantId);
        }
    }

    /**
     * 逐级删除子数据
     * @param parentId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByParentId(String parentId) {
        QueryBuilder<SysOrg> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysOrg> queryWrapper = queryBuilder.build();
        queryWrapper.eq(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentId);
        List<SysOrg> entityList = super.findList(queryWrapper);
        for (SysOrg sysOrg : entityList) {
            super.delete(sysOrg.getId());
            // 逐级删除子数据
            this.deleteByParentId(sysOrg.getId());
        }
    }

    /**
     * 唯一验证
     * @param model
     * @return
     */
    @Transactional(readOnly = true)
    public Integer uniqueVerificationByCode(SysOrgModel model){
        if(model == null){
            return null;
        }
        QueryWrapper<SysOrg> wrapper = new QueryWrapper<>();
        wrapper.eq(MyBatisConstants.FIELD_DELETE_LOGIC,  DictType.NO_YES_NO.getCode())
                .eq("org_code", model.getOrgCode());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        // 租户检测
        wrapper = new TenantHandler().handler(super.entityClazz, wrapper);

        return super.count(wrapper);
    }


    /**
     * 是否有下级
     * @param parentIds
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<HasChildren> hasChildren(Set<String> parentIds){
        if(CollUtil.isEmpty(parentIds)){
            return null;
        }
        QueryWrapper<SysOrg> wrapper = new QueryWrapper<>();

        wrapper.in(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentIds)
                .eq(MyBatisConstants.FIELD_DELETE_LOGIC,  DictType.NO_YES_NO.getCode())
                .groupBy(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID));

        return mapper.hasChildren(wrapper);
    }


    /**
     * 删除验证该组织是否被引用
     * @param orgIdList 组织ID
     */
    public void validationUsedByDel(List<String> orgIdList){
        if(CollUtil.isEmpty(orgIdList)){
            return;
        }

        // 如果有租户还在被引用 则不允许删除该租户
        QueryWrapper<SysOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("org_id",
                orgIdList
        );
        Integer count = mapper.hasUse(queryWrapper);
        if(count !=null && count > 0){
            // 该租户正在被其他用户绑定，无法操作
            throw new ServiceException(SystemMsg.EXCEPTION_TENANT_USED_DEL);
        }
    }
}
