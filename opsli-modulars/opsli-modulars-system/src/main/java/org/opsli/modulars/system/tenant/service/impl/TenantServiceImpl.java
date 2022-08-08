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
package org.opsli.modulars.system.tenant.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.tenant.TenantModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.FieldUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.utils.TenantUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.tenant.entity.SysTenant;
import org.opsli.modulars.system.tenant.mapper.TenantMapper;
import org.opsli.modulars.system.tenant.service.ITenantService;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 租户 Service Impl
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Service
public class TenantServiceImpl extends CrudServiceImpl<TenantMapper, SysTenant, TenantModel> implements ITenantService {

    @Autowired(required = false)
    private TenantMapper mapper;

    @Autowired
    private IUserService iUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableTenant(String tenantId, String enable) {
        if(!DictType.hasDict(DictType.NO_YES_YES.getType(), enable)){
            // 非法参数
            throw new ServiceException(SystemMsg.EXCEPTION_USER_ILLEGAL_PARAMETER);
        }

        TenantModel model = this.get(tenantId);
        if(model == null){
            return false;
        }

        String currTenantId = UserUtil.getRealTenantId();
        if(StringUtils.equals(currTenantId, tenantId)){
            // 不可操作自身
            throw new ServiceException(SystemMsg.EXCEPTION_TENANT_HANDLE_SELF);
        }

        // 超级管理员
        UserModel superAdmin = UserUtil.getUserByUserName(UserUtil.SUPER_ADMIN);
        if(superAdmin != null){
            String superAdminTenantId = superAdmin.getTenantId();
            if(StringUtils.equals(superAdminTenantId, tenantId)){
                // 不可操作超管租户
                throw new ServiceException(SystemMsg.EXCEPTION_TENANT_HANDLE_SUPER_ADMIN);
            }
        }

        UpdateWrapper<SysTenant> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("enable", enable)
                .eq(
                    FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID), tenantId);
        if(this.update(updateWrapper)){
            // 清除缓存
            this.clearCache(Collections.singletonList(tenantId));
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TenantModel insert(TenantModel model) {
        if(model == null){
            return null;
        }

        // 默认为未启用
        model.setEnable(DictType.NO_YES_NO.getValue());

        // 唯一验证
        boolean verificationByName = this.uniqueVerificationByName(model);
        if(!verificationByName){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_TENANT_UNIQUE);
        }

        return super.insert(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TenantModel update(TenantModel model) {
        if(model == null){
            return null;
        }

        model.setEnable(null);

        // 唯一验证
        boolean verificationByName = this.uniqueVerificationByName(model);
        if(!verificationByName){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_TENANT_UNIQUE);
        }

        TenantModel tenantModel = super.update(model);
        if(tenantModel != null){
            // 清除缓存
            this.clearCache(Collections.singletonList(model.getId()));
        }
        return tenantModel;
    }


    /**
     * 删除
     * @param id ID
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        TenantModel tenantModel = this.get(id);
        if(tenantModel == null){
            return false;
        }

        String currTenantId = UserUtil.getRealTenantId();
        if(StringUtils.equals(currTenantId, id)){
            // 不可操作自身
            throw new ServiceException(SystemMsg.EXCEPTION_TENANT_HANDLE_SELF);
        }

        // 超级管理员
        UserModel superAdmin = UserUtil.getUserByUserName(UserUtil.SUPER_ADMIN);
        if(superAdmin != null){
            String superAdminTenantId = superAdmin.getTenantId();
            if(StringUtils.equals(superAdminTenantId, id)){
                // 不可操作超管租户
                throw new ServiceException(SystemMsg.EXCEPTION_TENANT_HANDLE_SUPER_ADMIN);
            }
        }

        // 如果有租户还在被引用 则不允许删除该租户
        this.validationUsedByDel(Collections.singletonList(id));

        boolean ret = super.delete(id);

        if(ret){
            // 清除缓存
            this.clearCache(Collections.singletonList(tenantModel.getId()));
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
    public boolean delete(TenantModel model) {
        TenantModel tenantModel = this.get(model);
        if(tenantModel == null){
            return false;
        }

        String currTenantId = UserUtil.getRealTenantId();
        if(StringUtils.equals(currTenantId, model.getId())){
            // 不可操作自身
            throw new ServiceException(SystemMsg.EXCEPTION_TENANT_HANDLE_SELF);
        }

        // 超级管理员
        UserModel superAdmin = UserUtil.getUserByUserName(UserUtil.SUPER_ADMIN);
        if(superAdmin != null){
            String superAdminTenantId = superAdmin.getTenantId();
            if(StringUtils.equals(superAdminTenantId, model.getId())){
                // 不可操作超管租户
                throw new ServiceException(SystemMsg.EXCEPTION_TENANT_HANDLE_SUPER_ADMIN);
            }
        }

        // 如果有租户还在被引用 则不允许删除该租户
        this.validationUsedByDel(Collections.singletonList(model.getId()));

        boolean ret = super.delete(model);

        if(ret){
            // 清除缓存
            this.clearCache(Collections.singletonList(tenantModel.getId()));
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
        List<String> idList = Convert.toList(String.class, ids);


        String currTenantId = UserUtil.getRealTenantId();
        if(CollUtil.isNotEmpty(idList)){
            if(idList.contains(currTenantId)){
                // 不可操作自身
                throw new ServiceException(SystemMsg.EXCEPTION_TENANT_HANDLE_SELF);
            }

            // 超级管理员
            UserModel superAdmin = UserUtil.getUserByUserName(UserUtil.SUPER_ADMIN);
            if(superAdmin != null){
                String superAdminTenantId = superAdmin.getTenantId();
                if(idList.contains(superAdminTenantId)){
                    // 不可操作超管租户
                    throw new ServiceException(SystemMsg.EXCEPTION_TENANT_HANDLE_SUPER_ADMIN);
                }
            }
        }

        // 如果有租户还在被引用 则不允许删除该租户
        this.validationUsedByDel(idList);

        boolean ret = super.deleteAll(ids);
        if(ret){
            // 清除缓存
            this.clearCache(idList);
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
    public boolean deleteAll(Collection<TenantModel> models) {
        List<String> idList = Lists.newArrayListWithCapacity(models.size());
        for (TenantModel model : models) {
            idList.add(model.getId());
        }

        String currTenantId = UserUtil.getRealTenantId();
        if(CollUtil.isNotEmpty(idList)){
            if(idList.contains(currTenantId)){
                // 不可操作自身
                throw new ServiceException(SystemMsg.EXCEPTION_TENANT_HANDLE_SELF);
            }

            // 超级管理员
            UserModel superAdmin = UserUtil.getUserByUserName(UserUtil.SUPER_ADMIN);
            if(superAdmin != null){
                String superAdminTenantId = superAdmin.getTenantId();
                if(idList.contains(superAdminTenantId)){
                    // 不可操作超管租户
                    throw new ServiceException(SystemMsg.EXCEPTION_TENANT_HANDLE_SUPER_ADMIN);
                }
            }
        }

        // 如果有租户还在被引用 则不允许删除该租户
        this.validationUsedByDel(idList);

        boolean ret = super.deleteAll(models);
        if(ret){
            // 清除缓存
            this.clearCache(idList);
        }
        return ret;
    }


    /**
     * 唯一验证
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public boolean uniqueVerificationByName(TenantModel model){
        if(model == null){
            return false;
        }
        QueryWrapper<SysTenant> wrapper = new QueryWrapper<>();

        // name 唯一
        wrapper.eq(MyBatisConstants.FIELD_DELETE_LOGIC, DictType.NO_YES_NO.getValue())
                .eq("tenant_name", model.getTenantName());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        return super.count(wrapper) == 0;
    }

    /**
     * 删除验证该租户是否被引用
     * @param tenantIdList 租户ID
     */
    public void validationUsedByDel(List<String> tenantIdList){
        if(CollUtil.isEmpty(tenantIdList)){
            return;
        }

        // 如果有租户还在被引用 则不允许删除该租户
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_TENANT),
                tenantIdList
        );
        long count = iUserService.count(queryWrapper);
        if(count > 0){
            // 该租户正在被其他用户绑定，无法删除
            throw new ServiceException(SystemMsg.EXCEPTION_TENANT_USED_DEL);
        }
    }

    // ============


    /**
     * 清除缓存
     * @param tenantIds 租户ID集合
     */
    private void clearCache(List<String> tenantIds){
        // 清空缓存
        if(CollUtil.isNotEmpty(tenantIds)){
            int cacheCount = 0;
            for (String tenantId : tenantIds) {
                cacheCount++;
                boolean tmp = TenantUtil.refreshTenant(tenantId);
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


