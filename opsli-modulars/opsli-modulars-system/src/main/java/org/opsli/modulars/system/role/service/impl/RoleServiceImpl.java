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
package org.opsli.modulars.system.role.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.FieldUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.role.entity.SysRole;
import org.opsli.modulars.system.role.mapper.RoleMapper;
import org.opsli.modulars.system.role.service.IRoleMenuRefService;
import org.opsli.modulars.system.role.service.IRoleService;
import org.opsli.modulars.system.user.service.IUserRoleRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


/**
 * 角色 Service Impl
 *
 * @author Parker
 * @date 2020-09-17 13:07
 */
@Service
public class RoleServiceImpl extends CrudServiceImpl<RoleMapper, SysRole, RoleModel> implements IRoleService {

    @Autowired(required = false)
    private RoleMapper mapper;
    @Autowired
    private IRoleMenuRefService iRoleMenuRefService;
    @Autowired
    private IUserRoleRefService iUserRoleRefService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleModel insert(RoleModel model) {
        if(model == null){
            return null;
        }

        // 判断用户是否有 修改租户的能力 (超级管理员除外)
        if(StringUtils.isNotEmpty(model.getTenantId())){
            // 如果没有租户修改能力 则清空对应字段
            if(!UserUtil.isHasUpdateTenantPerms(UserUtil.getUser())){
                model.setTenantId(null);
            }
        }

        // 唯一验证
        boolean verificationByCode = this.uniqueVerificationByCode(model);
        boolean verificationByName = this.uniqueVerificationByName(model);
        if(!verificationByCode || !verificationByName){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_UNIQUE);
        }

        return super.insert(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleModel update(RoleModel model) {
        if(model == null){
            return null;
        }

        // 判断用户是否有 修改租户的能力 (超级管理员除外)
        if(StringUtils.isNotEmpty(model.getTenantId())){
            // 如果没有租户修改能力 则清空对应字段
            if(!UserUtil.isHasUpdateTenantPerms(UserUtil.getUser())){
                model.setTenantId(null);
            }
        }

        // 唯一验证
        boolean verificationByCode = this.uniqueVerificationByCode(model);
        boolean verificationByName = this.uniqueVerificationByName(model);
        if(!verificationByCode || !verificationByName){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_UNIQUE);
        }

        model = super.update(model);
        // 清除缓存
        if(null != model){
            clearCache(Convert.toStrArray(model.getId()));
        }
        return model;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        boolean roleUsed = iUserRoleRefService.isRoleUsed(id);
        if(roleUsed){
            // 角色删除失败, 改角色正在被其他用户使用
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_USED);
        }

        // 删除角色下 权限
        iRoleMenuRefService.delPermsByRoleIds(Convert.toList(String.class, id));
        return super.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAll(String[] ids) {
        boolean roleUsed = iUserRoleRefService.isRoleUsed(ids);
        if(roleUsed){
            // 角色删除失败, 改角色正在被其他用户使用
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_USED);
        }

        // 删除角色下 权限
        iRoleMenuRefService.delPermsByRoleIds(Convert.toList(String.class, ids));
        return super.deleteAll(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(RoleModel model) {
        if(model == null){
            return false;
        }

        boolean roleUsed = iUserRoleRefService.isRoleUsed(model.getId());
        if(roleUsed){
            // 角色删除失败, 改角色正在被其他用户使用
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_USED);
        }

        // 删除角色下 权限
        iRoleMenuRefService.delPermsByRoleIds(Convert.toList(String.class, model.getId()));
        return super.delete(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAll(Collection<RoleModel> models) {
        List<String> roleIds = Lists.newArrayList();
        for (RoleModel model : models) {
            roleIds.add(model.getId());
        }

        // 删除角色下 权限
        iRoleMenuRefService.delPermsByRoleIds(roleIds);
        return super.deleteAll(models);
    }

    /**
     * 唯一验证
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public boolean uniqueVerificationByCode(RoleModel model){
        if(model == null){
            return false;
        }
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();

        // code 唯一
        wrapper.eq("role_code", model.getRoleCode());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        // 如果租户ID 不为空 则直接判断租户
        if(StringUtils.isNotBlank(model.getTenantId())){
            wrapper.eq(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_TENANT), model.getTenantId());
        }else {
            // 租户检测
            // 数据处理责任链
            wrapper = super.addHandler(this.getEntityClass(), wrapper);
        }

        return super.count(wrapper) == 0;
    }


    /**
     * 唯一验证
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public boolean uniqueVerificationByName(RoleModel model){
        if(model == null){
            return false;
        }
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();

        // code 唯一
        wrapper.eq("role_name", model.getRoleName());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        // 如果租户ID 不为空 则直接判断租户
        if(StringUtils.isNotBlank(model.getTenantId())){
            wrapper.eq(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_TENANT), model.getTenantId());
        }else {
            // 租户检测
            // 数据处理责任链
            wrapper = super.addHandler(this.getEntityClass(), wrapper);
        }

        return super.count(wrapper) == 0;
    }


    /**
     * 清除缓存
     * @param roleIds 角色ID
     */
    private void clearCache(String[] roleIds){
        // 清空该角色下 用户缓存
        List<String> userIdList = iUserRoleRefService.getUserIdListByRoleIds(roleIds);
        if(CollUtil.isNotEmpty(userIdList)){
            int cacheCount = 0;
            for (String userId : userIdList) {
                cacheCount += 4;
                boolean tmp;
                // 清空当期用户缓存角色、权限、菜单
                tmp = UserUtil.refreshUserRoles(userId);
                if(tmp){
                    cacheCount--;
                }
                tmp = UserUtil.refreshUserAllPerms(userId);
                if(tmp){
                    cacheCount--;
                }
                tmp = UserUtil.refreshUserMenus(userId);
                if(tmp){
                    cacheCount--;
                }
                tmp = UserUtil.refreshUserDefRole(userId);
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


