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

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.user.UserOrgRefApi;
import org.opsli.api.wrapper.system.org.SysOrgModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.api.wrapper.system.user.UserOrgRefWebModel;
import org.opsli.api.wrapper.system.user.UserWebModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.user.entity.SysUserOrgRef;
import org.opsli.modulars.system.user.entity.SysUserWeb;
import org.opsli.modulars.system.user.service.IUserOrgRefService;
import org.opsli.modulars.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 用户-组织 Controller
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Api(tags = UserOrgRefApi.TITLE)
@Slf4j
@ApiRestController("/system/user/org")
public class UserOrgRefRestController implements UserOrgRefApi {

    /** 配置类 */
    @Autowired
    protected GlobalProperties globalProperties;

    @Autowired
    private IUserOrgRefService iUserOrgRefService;

    @Autowired
    private IUserService iUserService;

    @Override
    public ResultVo<List<UserOrgRefModel>> findListByUserId(String userId) {
        List<UserOrgRefModel> listByUserId = iUserOrgRefService.findListByUserId(userId);
        return ResultVo.success(listByUserId);
    }

    /**
     * 设置组织
     * @param model 模型
     * @return ResultVo
     */
    @Override
    @RequiresPermissions("system_user_setOrg")
    public ResultVo<?> setOrg(UserOrgRefWebModel model) {
        // 演示模式 不允许操作
        this.demoError();

        boolean ret = iUserOrgRefService.setOrg(model);
        if(!ret){
            // 权限设置失败
            throw new ServiceException(SystemMsg.EXCEPTION_USER_ORG_ERROR);
        }
        return ResultVo.success();
    }

    /**
     * 根据 userId 获得用户默认组织
     * @param userId 用户Id
     * @return ResultVo
     */
    @Override
    public ResultVo<UserOrgRefModel> getDefOrgByUserId(String userId) {
        UserOrgRefModel userOrgRefModel = iUserOrgRefService.getDefOrgByUserId(userId);
        return ResultVo.success(userOrgRefModel);
    }

    /**
     * 用户组织机构
     * @param userId 用户ID
     * @return ResultVo
     */
    @ApiOperation(value = "用户组织机构", notes = "用户组织机构")
    @Override
    public ResultVo<UserOrgRefWebModel> getOrgInfoByUserId(String userId) {
        UserOrgRefWebModel org = null;
        // 不写SQL了 直接分页 第一页 取第一条
        QueryBuilder<SysUserWeb> queryBuilder = new GenQueryBuilder<>();
        Page<SysUserWeb, UserWebModel> page = new Page<>(1, 1);
        QueryWrapper<SysUserWeb> queryWrapper = queryBuilder.build();
        queryWrapper.eq(
                "a.id",
                userId
        );
        page.setQueryWrapper(queryWrapper);
        page = iUserService.findPageByCus(page);
        List<UserWebModel> list = page.getList();
        if(CollUtil.isNotEmpty(list)){
            UserWebModel userWebModel = list.get(0);
            if(userWebModel != null){
//                org  = userAndOrgModel.getOrg();
//                if(org != null){
//
//                    org.setUserId(userId);

//                    List<String> orgIds = Lists.newArrayListWithCapacity(3);
//                    orgIds.add(org.getCompanyId());
//                    orgIds.add(org.getDepartmentId());
//                    orgIds.add(org.getPostId());
//                    QueryWrapper<SysOrg> orgQueryWrapper = new QueryWrapper<>();
//                    orgQueryWrapper.in(
//                            FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID),
//                            orgIds);
//                    List<SysOrg> orgList = iSysOrgService.findList(orgQueryWrapper);
//                    if(CollUtil.isNotEmpty(orgList)){
//                        Map<String, SysOrg> tmp = Maps.newHashMap();
//                        for (SysOrg sysOrg : orgList) {
//                            tmp.put(sysOrg.getId(), sysOrg);
//                        }
//
//                        // 设置 名称
//                        SysOrg company = tmp.get(org.getCompanyId());
//                        if(company != null){
//                            org.setCompanyName(company.getOrgName());
//                        }
//
//                        SysOrg department = tmp.get(org.getDepartmentId());
//                        if(department != null){
//                            org.setDepartmentName(department.getOrgName());
//                        }
//
//                        SysOrg post = tmp.get(org.getPostId());
//                        if(post != null){
//                            org.setPostName(post.getOrgName());
//                        }
//                    }

//                }
            }
        }
        return ResultVo.success(org);
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
