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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.role.entity.SysRoleMenuRef;
import org.opsli.modulars.system.role.mapper.RoleMenuRefMapper;
import org.opsli.modulars.system.role.service.IRoleMenuRefService;
import org.opsli.modulars.system.user.service.IUserRoleRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 角色 - 菜单 Service Impl
 *
 * @author Parker
 * @date 2020-09-17 13:07
 */
@Service
public class RoleMenuRefServiceImpl extends ServiceImpl<RoleMenuRefMapper,SysRoleMenuRef> implements IRoleMenuRefService {

    @Autowired(required = false)
    private RoleMenuRefMapper mapper;

    @Lazy
    @Autowired
    private IUserRoleRefService iUserRoleRefService;

    @Override
    public List<SysMenu> getPerms(String roleId) {
        return mapper.queryAllPerms(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setPerms(String roleId, String[] permsIds) {
        if(StringUtils.isEmpty(roleId)){
            throw new ServiceException(SystemMsg.EXCEPTION_ROLE_ID_NOT_NULL);
        }

        if(permsIds != null && permsIds.length > 0){
            // 删除已有权限
            this.delPermsByRoleIds(Convert.toList(String.class, roleId));

            List<SysRoleMenuRef> list = Lists.newArrayListWithCapacity(permsIds.length);
            for (String permsId : permsIds) {
                SysRoleMenuRef entity = new SysRoleMenuRef();
                entity.setRoleId(roleId);
                entity.setMenuId(permsId);
                list.add(entity);
            }
            boolean ret = super.saveBatch(list);
            if(ret){
                // 清除缓存
                this.clearCache(roleId);
            }
            return ret;
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delPermsByMenuIds(List<String> menuIds){
        QueryWrapper<SysRoleMenuRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("menu_id", menuIds);
        return this.remove(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delPermsByRoleIds(List<String> menuIds){
        QueryWrapper<SysRoleMenuRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", menuIds);
        return this.remove(queryWrapper);
    }

    // =========================

    /**
     * 清除缓存
     * @param roleId 角色ID
     */
    private void clearCache(String roleId){
        // 清空该角色下 用户缓存
        List<String> userIdList = iUserRoleRefService.getUserIdListByRoleId(roleId);
        if(CollUtil.isNotEmpty(userIdList)){
            int cacheCount = 0;
            for (String userId : userIdList) {
                cacheCount += 6;
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
                tmp = UserUtil.refreshUserOrgs(userId);
                if(tmp){
                    cacheCount--;
                }
                tmp = UserUtil.refreshUserDefRole(userId);
                if(tmp){
                    cacheCount--;
                }
                tmp = UserUtil.refreshUserDefOrg(userId);
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


