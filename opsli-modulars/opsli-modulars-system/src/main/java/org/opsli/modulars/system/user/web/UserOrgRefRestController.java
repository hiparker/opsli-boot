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
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.user.UserOrgRefApi;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.api.wrapper.system.user.UserOrgRefWebModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.user.service.IUserOrgRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * 用户-组织 Controller
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Api(tags = UserOrgRefApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/system/user/org")
public class UserOrgRefRestController implements UserOrgRefApi {

    /** 配置类 */
    @Autowired
    protected GlobalProperties globalProperties;

    @Autowired
    private IUserOrgRefService iUserOrgRefService;

    @Override
    public ResultWrapper<List<UserOrgRefModel>> findListByUserId(String userId) {
        List<UserOrgRefModel> listByUserId = iUserOrgRefService.findListByUserId(userId);
        return ResultWrapper.getSuccessResultWrapper(listByUserId);
    }

    /**
     * 设置组织
     * @param model 模型
     * @return ResultWrapper
     */
    @Override
    @PreAuthorize("hasAuthority('system_user_setOrg')")
    public ResultWrapper<?> setOrg(UserOrgRefWebModel model) {
        // 演示模式 不允许操作
        this.demoError();

        boolean ret = iUserOrgRefService.setOrg(model);
        if(!ret){
            // 权限设置失败
            throw new ServiceException(SystemMsg.EXCEPTION_USER_ORG_ERROR);
        }
        return ResultWrapper.getSuccessResultWrapper();
    }

    /**
     * 根据 userId 获得用户默认组织
     * @param userId 用户Id
     * @return ResultWrapper
     */
    @Override
    public ResultWrapper<UserOrgRefModel> getDefOrgByUserId(String userId) {
        UserOrgRefModel userOrgRefModel = iUserOrgRefService.getDefOrgByUserId(userId);
        return ResultWrapper.getSuccessResultWrapper(userOrgRefModel);
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
