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
package org.opsli.modulars.system.user.web;

import cn.hutool.core.convert.Convert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.user.UserRoleRefApi;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserRoleRefModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.user.service.IUserRoleRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;


/**
 * 用户-角色 Controller
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Api(tags = UserRoleRefApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/system/user/roles")
public class UserRoleRefRestController implements UserRoleRefApi {

    /** 配置类 */
    @Autowired
    protected GlobalProperties globalProperties;

    @Autowired
    private IUserRoleRefService iUserRoleRefService;


    /**
     * 根据 userId 获得用户角色Id集合
     * @param userId 用户Id
     * @return ResultWrapper
     */
    @ApiOperation(value = "根据 userId 获得用户角色Id集合", notes = "根据 userId 获得用户角色Id集合")
    @Override
    public ResultWrapper<UserRoleRefModel> getRoles(String userId) {

        List<String> roleIdList = iUserRoleRefService.getRoleIdList(userId);
        String defRoleId = iUserRoleRefService.getDefRoleId(userId);

        UserRoleRefModel userRoleRefModel = UserRoleRefModel.builder()
                .userId(userId)
                .roleIds(Convert.toStrArray(roleIdList))
                .defRoleId(defRoleId)
                .build();

        return ResultWrapper.getSuccessResultWrapper(userRoleRefModel);
    }

    /**
     * 设置角色
     * @param model 模型
     * @return ResultWrapper
     */
    @Override
    @PreAuthorize("hasAuthority('system_user_setRole')")
    public ResultWrapper<?> setRoles(UserRoleRefModel model) {
        // 演示模式 不允许操作
        this.demoError();

        boolean ret = iUserRoleRefService.setRoles(model);
        if(ret){
            return ResultWrapper.getSuccessResultWrapper();
        }
        // 权限设置失败
        return ResultWrapper.getCustomResultWrapper(SystemMsg.EXCEPTION_USER_ROLES_ERROR);
    }

    /**
     * 根据 userId 获得用户角色
     * @param userId 用户Id
     * @return ResultWrapper
     */
    @Override
    public ResultWrapper<List<String>> getRolesByUserId(String userId) {
        List<String> roleCodeList = iUserRoleRefService.getRoleCodeList(userId);
        return ResultWrapper.getSuccessResultWrapper(roleCodeList);
    }

    /**
     * 根据 userId 获得用户默认角色
     * @param userId 用户Id
     * @return ResultWrapper
     */
    @Override
    public ResultWrapper<RoleModel> getDefRoleByUserId(String userId) {
        RoleModel defRoleByUserId = iUserRoleRefService.getDefRoleByUserId(userId);
        return ResultWrapper.getSuccessResultWrapper(defRoleByUserId);
    }

    /**
     * 根据 userId 获得用户权限
     * @param userId 用户Id
     * @return ResultWrapper
     */
    @Override
    public ResultWrapper<List<String>> getAllPerms(String userId) {
        List<String> allPerms = iUserRoleRefService.getAllPerms(userId);
        return ResultWrapper.getSuccessResultWrapper(allPerms);
    }


    /**
     * 根据 userId 获得用户菜单
     * @param userId 用户Id
     * @return ResultWrapper
     */
    @Override
    public ResultWrapper<List<MenuModel>> getMenuListByUserId(String userId) {
        List<MenuModel> menuModelList = iUserRoleRefService.getMenuListByUserId(userId);
        return ResultWrapper.getSuccessResultWrapper(menuModelList);
    }









    /**
     * 演示模式
     */
    private void demoError(){
        UserModel user = UserUtil.getUser();
        // 演示模式 不允许操作 （超级管理员可以操作）
        if(globalProperties.isEnableDemo() &&
                !StringUtils.equals(UserUtil.SUPER_ADMIN, user.getUsername())){
            throw new ServiceException(CoreMsg.EXCEPTION_ENABLE_DEMO);
        }
    }
}
