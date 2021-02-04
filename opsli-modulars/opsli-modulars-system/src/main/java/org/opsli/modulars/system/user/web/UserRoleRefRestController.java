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

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.user.UserRoleRefApi;
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


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 用户角色
 */
@Api(tags = "用户角色")
@Slf4j
@ApiRestController("/sys/user/roles")
public class UserRoleRefRestController implements UserRoleRefApi {

    /** 配置类 */
    @Autowired
    protected GlobalProperties globalProperties;

    @Autowired
    private IUserRoleRefService iUserRoleRefService;


    /**
     * 设置角色
     * @param model userId 用户Id
     * @param model roleIds 角色Id 数组
     * @return ResultVo
     */
    @Override
    @RequiresPermissions("system_user_setRole")
    public ResultVo<?> setRoles(UserRoleRefModel model) {
        // 演示模式 不允许操作
        this.demoError();

        boolean ret = iUserRoleRefService.setRoles(model.getUserId(),
                model.getRoleIds());
        if(ret){
            return ResultVo.success();
        }
        // 权限设置失败
        return ResultVo.error(SystemMsg.EXCEPTION_USER_ROLES_ERROR.getCode(),
                SystemMsg.EXCEPTION_USER_ROLES_ERROR.getMessage()
                );
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
