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
package org.opsli.modulars.system.role.web;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.role.RoleMenuRefApi;
import org.opsli.api.wrapper.system.role.RoleMenuRefModel;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.role.service.IRoleMenuRefService;
import org.opsli.modulars.system.role.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 角色
 */
@Slf4j
@ApiRestController("/sys/role/perms")
public class RoleMenuRefRestController implements RoleMenuRefApi {

    /** 内置数据 */
    private static final String LOCK_DATA = "1";

    @Value("${opsli.enable-demo}")
    private boolean enableDemo;

    @Autowired
    private IRoleService iRoleService;
    @Autowired
    private IRoleMenuRefService iRoleMenuRefService;

    /**
     * 获得当前已有权限
     * @param model roleId 角色Id
     * @return ResultVo
     */
    @RequiresPermissions("system_role_setPerms")
    @Override
    public ResultVo<?> getPerms(RoleMenuRefModel model) {
        if(model == null){
            return ResultVo.error(SystemMsg.EXCEPTION_ROLE_ID_NOT_NULL.getCode(),
                    SystemMsg.EXCEPTION_ROLE_ID_NOT_NULL.getMessage());
        }

        List<SysMenu> perms = iRoleMenuRefService.getPerms(model.getRoleId());
        List<String> permsIds = Lists.newArrayListWithCapacity(perms.size());
        if(!perms.isEmpty()){
            for (SysMenu perm : perms) {
                permsIds.add(perm.getId());
            }

            // 按照parentId分组
            Map<String, List<SysMenu>> groupMap = perms.stream()
                    .collect(Collectors.groupingBy(SysMenu::getParentId));

            // 获得分组key 根据分组key 删除分组父ID
            for (String key : groupMap.keySet()) {
                permsIds.remove(key);
            }
        }

        return ResultVo.success(permsIds);
    }

    /**
     * 设置权限
     * @param model roleId 角色Id
     * @param model permsIds 权限Id 数组
     * @return ResultVo
     */
    @RequiresPermissions("system_role_setPerms")
    @EnableLog
    @Override
    public ResultVo<?> setPerms(RoleMenuRefModel model) {
        // 演示模式 不允许操作
        this.demoError();

        if(model == null){
            return ResultVo.error("设置权限失败");
        }

        RoleModel roleModel = iRoleService.get(model.getRoleId());
        // 内置数据 只有超级管理员可以修改
        if(roleModel != null && LOCK_DATA.equals(roleModel.getIzLock()) ){
            UserModel user = UserUtil.getUser();
            if(!UserUtil.SUPER_ADMIN.equals(user.getUsername())){
                throw new ServiceException(SystemMsg.EXCEPTION_LOCK_DATA);
            }
        }


        boolean ret = iRoleMenuRefService.setPerms(model.getRoleId(),
                model.getPermsIds());
        if(ret){
            return ResultVo.success();
        }
        // 权限设置失败
        return ResultVo.error(SystemMsg.EXCEPTION_ROLE_PERMS_ERROR.getCode(),
                SystemMsg.EXCEPTION_ROLE_PERMS_ERROR.getMessage()
                );
    }


    /**
     * 演示模式
     */
    private void demoError(){
        UserModel user = UserUtil.getUser();
        // 演示模式 不允许操作 （超级管理员可以操作）
        if(enableDemo && !UserUtil.SUPER_ADMIN.equals(user.getUsername())){
            throw new ServiceException(CoreMsg.EXCEPTION_ENABLE_DEMO);
        }
    }

}
