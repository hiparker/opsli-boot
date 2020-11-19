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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.role.entity.SysRoleMenuRef;
import org.opsli.modulars.system.role.mapper.RoleMenuRefMapper;
import org.opsli.modulars.system.role.service.IRoleMenuRefService;
import org.opsli.modulars.system.user.service.IUserRoleRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.service
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:34
 * @Description: 角色 接口实现类
 */
@Service
public class RoleMenuRefServiceImpl extends ServiceImpl<RoleMenuRefMapper,SysRoleMenuRef> implements IRoleMenuRefService {

    @Autowired(required = false)
    private RoleMenuRefMapper mapper;
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

        // 删除已有权限
        String roleIdField = "role_id";
        QueryWrapper<SysRoleMenuRef> wrapper = new QueryWrapper<>();
        wrapper.eq(roleIdField, roleId);
        super.remove(wrapper);

        if(permsIds != null && permsIds.length > 0){
            List<SysRoleMenuRef> list = Lists.newArrayListWithCapacity(permsIds.length);
            for (String permsId : permsIds) {
                SysRoleMenuRef entity = new SysRoleMenuRef();
                entity.setRoleId(roleId);
                entity.setMenuId(permsId);
                list.add(entity);
            }
            super.saveBatch(list);
        }

        // 清空该角色下 用户缓存
        List<String> userIdList = iUserRoleRefService.getUserIdListByRoleId(roleId);
        if(userIdList != null && !userIdList.isEmpty()){
            for (String userId : userIdList) {
                // 清空当期用户缓存角色、权限、菜单
                UserUtil.refreshUserRoles(userId);
                UserUtil.refreshUserAllPerms(userId);
                UserUtil.refreshUserMenus(userId);
            }
        }

        return true;
    }
}


