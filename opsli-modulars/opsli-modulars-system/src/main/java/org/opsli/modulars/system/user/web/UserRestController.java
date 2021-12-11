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
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.user.UserApi;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.options.OptionsModel;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.api.wrapper.system.tenant.TenantModel;
import org.opsli.api.wrapper.system.user.*;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.common.annotation.RequiresPermissionsCus;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.exception.TokenException;
import org.opsli.common.utils.FieldUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.core.persistence.querybuilder.conf.WebQueryConf;
import org.opsli.core.utils.OptionsUtil;
import org.opsli.core.utils.OrgUtil;
import org.opsli.core.utils.TenantUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.entity.SysUserWeb;
import org.opsli.modulars.system.user.service.IUserRoleRefService;
import org.opsli.modulars.system.user.service.IUserService;
import org.opsli.plugins.oss.OssStorageFactory;
import org.opsli.plugins.oss.service.BaseOssStorageService;
import org.opsli.plugins.oss.service.OssStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;


/**
 * 用户管理 Controller
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Api(tags = UserApi.TITLE)
@Slf4j
@ApiRestController("/system/user/{ver}")
public class UserRestController extends BaseRestController<SysUser, UserModel, IUserService>
        implements UserApi {

    @Autowired
    private IUserRoleRefService iUserRoleRefService;

    /**
     * 当前登陆用户信息
     * @return ResultVo
     */
    @ApiOperation(value = "当前登陆用户信息", notes = "当前登陆用户信息")
    @Override
    public ResultVo<UserInfo> getInfo(HttpServletRequest request) {
        UserModel currUser = UserUtil.getUserBySource();
        return this.getInfoById(currUser.getId());
    }

    /**
     * 当前登陆用户信息 By Id
     * @return ResultVo
     */
    @ApiOperation(value = "当前登陆用户信息 By Id", notes = "当前登陆用户信息 By Id")
    @Override
    public ResultVo<UserInfo> getInfoById(String userId) {
        UserModel currUser = UserUtil.getUserBySource(userId);
        if(currUser == null){
            throw new TokenException(TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY);
        }

        // 判断是否是 切换租户状态
        String userIdTemp = currUser.getId();
        if(StringUtils.isNotBlank(currUser.getSwitchTenantUserId())){
            UserModel switchUser = UserUtil.getUser(currUser.getSwitchTenantUserId());
            if(switchUser == null){
                // 不允许切换租户
                throw new ServiceException(SystemMsg.EXCEPTION_USER_SWITCH_NOT_ALLOWED);
            }
            userIdTemp = switchUser.getId();
        }


        List<String> userRolesByUserId = UserUtil.getUserRolesByUserId(userIdTemp);
        List<String> userAllPermsByUserId = UserUtil.getUserAllPermsByUserId(userIdTemp);

        UserInfo userInfo = WrapperUtil.transformInstance(currUser, UserInfo.class);
        userInfo.setRoles(userRolesByUserId);
        userInfo.setPerms(userAllPermsByUserId);

        // 数据范围
        RoleModel defRole = UserUtil.getUserDefRoleByUserId(userIdTemp);
        if(null != defRole){
            userInfo.setDataScope(defRole.getDataScope());
        }

        // 判断是否是超级管理员
        if(StringUtils.equals(UserUtil.SUPER_ADMIN, currUser.getUsername())){
            userInfo.setIzSuperAdmin(true);
        }

        return ResultVo.success(userInfo);
    }


    /**
     * 当前登陆用户组织机构
     * @return ResultVo
     */
    @ApiOperation(value = "当前登陆用户组织机构", notes = "当前登陆用户组织机构")
    @Override
    public ResultVo<?> getOrg() {
        UserModel user = UserUtil.getUser();
        return this.getOrgByUserId(user.getId());
    }

    /**
     * 用户组织机构
     * @param userId 用户ID
     * @return ResultVo
     */
    @ApiOperation(value = "用户组织机构", notes = "用户组织机构")
    @Override
    public ResultVo<?> getOrgByUserId(String userId) {
        List<UserOrgRefModel> orgListByUserId = UserUtil.getOrgListByUserId(userId);
        return ResultVo.success(orgListByUserId);
    }

    /**
     * 根据 userId 获得用户角色Id集合
     * @param userId 用户Id
     * @return ResultVo
     */
    @ApiOperation(value = "根据 userId 获得用户角色Id集合", notes = "根据 userId 获得用户角色Id集合")
    @Override
    public ResultVo<List<String>> getRoleIdsByUserId(String userId) {
        List<String> roleIdList = iUserRoleRefService.getRoleIdList(userId);
        return ResultVo.success(roleIdList);
    }



    /**
     * 修改密码
     * @return ResultVo
     */
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @Override
    public ResultVo<?> updatePassword(UserPassword userPassword) {
        // 演示模式 不允许操作
        super.demoError();

        UserModel user = UserUtil.getUserBySource();
        userPassword.setUserId(user.getId());
        IService.updatePasswordByCheckOld(userPassword);
        return ResultVo.success();
    }

    /**
     * 上传头像
     * @param request 文件流 request
     * @return ResultVo
     */
    @ApiOperation(value = "上传头像", notes = "上传头像")
    @Override
    public ResultVo<?> updateAvatar(MultipartHttpServletRequest request) {
        Iterator<String> itr = request.getFileNames();
        String uploadedFile = itr.next();
        List<MultipartFile> files = request.getFiles(uploadedFile);
        if (CollectionUtils.isEmpty(files)) {
            // 请选择文件
            return ResultVo.error(SystemMsg.EXCEPTION_USER_FILE_NULL.getCode(),
                    SystemMsg.EXCEPTION_USER_FILE_NULL.getMessage());
        }

        try {
            MultipartFile multipartFile = files.get(0);
            Resource resource = multipartFile.getResource();
            String filename = resource.getFilename();

            // 调用OSS 服务保存头像
            OssStorageService ossStorageService = OssStorageFactory.INSTANCE.getHandle();
            BaseOssStorageService.FileAttr fileAttr = ossStorageService.upload(
                    multipartFile.getInputStream(), FileUtil.extName(filename));

            UserModel user = UserUtil.getUserBySource();
            // 更新头像至数据库
            UserModel userModel = new UserModel();
            userModel.setId(user.getId());
            userModel.setAvatar(fileAttr.getFileStoragePath());
            IService.updateAvatar(userModel);
            // 刷新用户信息
            UserUtil.refreshUser(user);
        }catch (IOException e){
            log.error(e.getMessage(), e);
            return ResultVo.error("更新头像失败，请稍后再试");
        }

        return ResultVo.success();
    }

    // ==================================================


    /**
     * 修改密码
     * @return ResultVo
     */
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @RequiresPermissions("system_user_updatePassword")
    @EnableLog
    @Override
    public ResultVo<?> updatePasswordById(ToUserPassword userPassword) {
        // 演示模式 不允许操作
        super.demoError();

        IService.updatePasswordByNotCheckOld(userPassword);
        return ResultVo.success();
    }

    /**
     * 重置密码
     * @return ResultVo
     */
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @RequiresPermissions("system_user_resetPassword")
    @EnableLog
    @Override
    public ResultVo<?> resetPasswordById(String userId) {
        // 演示模式 不允许操作
        super.demoError();

        // 配置文件默认密码
        String defPass = globalProperties.getAuth().getDefaultPass();

        // 缓存默认密码 优先缓存
        OptionsModel optionsModel = OptionsUtil.getOptionByCode("def_pass");
        if(optionsModel != null){
            defPass = optionsModel.getOptionValue();
        }

        UserPassword userPassword = new UserPassword();
        userPassword.setNewPassword(defPass);
        userPassword.setUserId(userId);

        boolean resetPasswordFlag = IService.resetPassword(userPassword);
        if(!resetPasswordFlag){
            return ResultVo.error("重置密码失败");
        }

        return ResultVo.success("重置密码成功！默认密码为：" + defPass);
    }

    /**
     * 变更账户状态
     * @return ResultVo
     */
    @ApiOperation(value = "锁定账户", notes = "锁定账户")
    @RequiresPermissions("system_user_enable")
    @EnableLog
    @Override
    public ResultVo<?> enableAccount(String userId, String enable) {
        // 演示模式 不允许操作
        super.demoError();

        // 变更账户状态
        boolean lockAccountFlag = IService.enableAccount(userId, enable);
        if(!lockAccountFlag){
            return ResultVo.error("变更用户状态失败");
        }
        return ResultVo.success();
    }


    /**
     * 用户信息 查一条
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "获得单条用户信息", notes = "获得单条用户信息 - ID")
    // 因为工具类 使用到该方法 不做权限验证
    //@RequiresPermissions("system_user_select")
    @Override
    public ResultVo<UserModel> get(UserModel model) {
        // 如果系统内部调用 则直接查数据库
        if(model != null && model.getIzApi() != null && model.getIzApi()){
            model = IService.get(model);
        }
        return ResultVo.success(model);
    }

    /**
     * 用户信息 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param orgIdGroup 组织ID组
     * @param request request
     * @return ResultVo
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @RequiresPermissions("system_user_select")
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize,
                                 String orgIdGroup,
                                 HttpServletRequest request) {
        QueryBuilder<SysUserWeb> queryBuilder = new WebQueryBuilder<>(
                SysUserWeb.class, request.getParameterMap(), "a.update_time");
        Page<SysUserWeb, UserWebModel> page = new Page<>(pageNo, pageSize);
        QueryWrapper<SysUserWeb> queryWrapper = queryBuilder.build();

        // 不查看 为租户管理员的用户
        queryWrapper.notIn("iz_tenant_admin", DictType.NO_YES_YES.getValue());

        // 处理组织权限
        OrgUtil.handleOrgIdGroupCondition(orgIdGroup, queryWrapper);

        page.setQueryWrapper(queryWrapper);
        page = IService.findPageByCus(page);
        // 密码防止分页泄露处理
        for (UserWebModel userModel : page.getList()) {
            userModel.setSecretKey(null);
            userModel.setPassword(null);
            userModel.setPasswordLevel(null);
        }
        return ResultVo.success(page.getPageData());
    }

    /**
     * 租户管理员信息 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultVo
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @RequiresPermissions("system_set_tenant_admin")
    @Override
    public ResultVo<?> findPageByTenant(Integer pageNo, Integer pageSize,
                                HttpServletRequest request) {
        // 转换字段
        WebQueryConf conf = new WebQueryConf();
        conf.pub(SysUserWeb::getTenantId, "a.tenant_id");

        QueryBuilder<SysUserWeb> queryBuilder = new WebQueryBuilder<>(
                SysUserWeb.class, request.getParameterMap(),"a.update_time", conf);

        Page<SysUserWeb, UserWebModel> page = new Page<>(pageNo, pageSize);

        page.setQueryWrapper(queryBuilder.build());

        page = IService.findPageByTenant(page);
        // 密码防止分页泄露处理
        for (UserWebModel userModel : page.getList()) {
            userModel.setSecretKey(null);
            userModel.setPassword(null);
            userModel.setPasswordLevel(null);
        }
        return ResultVo.success(page.getPageData());
    }

    /**
     * 用户信息 新增
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "新增用户信息", notes = "新增用户信息")
    @RequiresPermissions("system_user_insert")
    @EnableLog
    @Override
    public ResultVo<?> insert(UserModel model) {
        // 调用新增方法
        UserModel userModel = IService.insert(model);
        return ResultVo.success("新增用户信息成功", userModel);
    }

    /**
     * 用户信息 修改
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @RequiresPermissions("system_user_update")
    @EnableLog
    @Override
    public ResultVo<?> update(UserModel model) {
        // 演示模式 不允许操作
        super.demoError();
        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改用户信息成功");
    }

    /**
     * 用户信息 自身修改
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "修改自身用户信息", notes = "修改自身用户信息")
    @EnableLog
    @Override
    public ResultVo<?> updateSelf(UserModel model) {
        UserModel currUser = UserUtil.getUserBySource();
        if(!StringUtils.equals(currUser.getId(), model.getId())){
            // 非法参数 防止其他用户 通过该接口 修改非自身用户数据
            throw new ServiceException(SystemMsg.EXCEPTION_USER_ILLEGAL_PARAMETER);
        }
        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改用户信息成功");
    }

    /**
     * 用户信息 删除
     * @param id ID
     * @return ResultVo
     */
    @ApiOperation(value = "删除用户信息数据", notes = "删除用户信息数据")
    @RequiresPermissions("system_user_delete")
    @EnableLog
    @Override
    public ResultVo<?> del(String id){
        // 演示模式 不允许操作
        super.demoError();

        IService.delete(id);

        return ResultVo.success("删除用户信息成功");
    }


    /**
     * 用户信息 批量删除
     * @param ids ID 数组
     * @return ResultVo
     */
    @ApiOperation(value = "批量删除用户信息数据", notes = "批量删除用户信息数据")
    @RequiresPermissions("system_user_delete")
    @EnableLog
    @Override
    public ResultVo<?> delAll(String ids){
        // 演示模式 不允许操作
        super.demoError();

        String[] idArray = Convert.toStrArray(ids);
        IService.deleteAll(idArray);

        return ResultVo.success("批量删除用户信息成功");
    }


    /**
     * 用户信息 Excel 导出
     * @param request request
     * @param response response
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @RequiresPermissionsCus("system_user_export")
    @EnableLog
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "exportExcel");
        QueryBuilder<SysUser> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        super.excelExport(UserApi.SUB_TITLE, queryBuilder.build(), response, method);
    }

    /**
     * 用户信息 Excel 导入
     * @param request 文件流 request
     * @return ResultVo
     */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @RequiresPermissions("system_user_import")
    @EnableLog
    @Override
    public ResultVo<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }

    /**
     * 用户信息 Excel 下载导入模版
     * @param response response
     */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissionsCus("system_user_import")
    @Override
    public void importTemplate(HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "importTemplate");
        super.importTemplate(UserApi.SUB_TITLE, response, method);
    }

    /**
     * 根据 username 获得用户
     * @param username 用户名
     * @return ResultVo
     */
    @ApiOperation(value = "根据 username 获得用户", notes = "根据 username 获得用户")
    @Override
    public ResultVo<UserModel> getUserByUsername(String username) {
        UserModel userModel = IService.queryByUserName(username);
        if(userModel == null){
            // 暂无该用户
            throw new ServiceException(SystemMsg.EXCEPTION_USER_NULL.getCode(),
                    StrUtil.format(SystemMsg.EXCEPTION_USER_NULL.getMessage(), username)
            );
        }
        return ResultVo.success(userModel);
    }


    /**
     * 切换租户
     * @param tenantId 租户ID
     * @return ResultVo
     */
    @ApiOperation(value = "切换租户", notes = "切换租户")
    @Override
    public ResultVo<?> switchTenant(String tenantId) {
        UserModel currUser = UserUtil.getUserBySource();
        if (!DictType.NO_YES_YES.getValue().equals(currUser.getEnableSwitchTenant())){
            // 不允许切换租户
            throw new ServiceException(SystemMsg.EXCEPTION_USER_SWITCH_NOT_ALLOWED);
        }

        // 验证租户是否生效
        TenantModel tenant = TenantUtil.getTenant(tenantId);
        if(tenant == null){
            throw new ServiceException(TokenMsg.EXCEPTION_LOGIN_TENANT_NOT_USABLE);
        }

        // 被切换租户的 管理员用户 取一个
        SysUser isSwitchedUser = IService.getOne(new QueryWrapper<SysUser>()
                .eq("iz_tenant_admin", DictType.NO_YES_YES.getValue())
                .eq(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_DELETE_LOGIC), DictType.NO_YES_NO.getValue())
                .eq(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_TENANT), tenantId)
                .last("limit 1")
        );

        if (isSwitchedUser == null){
            // 此租户不存在管理员，不能切换
            throw new ServiceException(SystemMsg.EXCEPTION_USER_SWITCH_TENANT_NOT_HAS_ADMIN);
        }

        // 检测用户是否有角色
        List<String> roleModelList = UserUtil.getUserRolesByUserId(isSwitchedUser.getId());
        if(CollUtil.isEmpty(roleModelList)){
            // 用户暂无角色，请设置后登录
            throw new ServiceException(TokenMsg.EXCEPTION_USER_ROLE_NOT_NULL);
        }

        // 检测用户是否有角色菜单
        List<MenuModel> menuModelList = UserUtil.getMenuListByUserId(isSwitchedUser.getId());
        if(CollUtil.isEmpty(menuModelList)){
            // 用户暂无角色菜单，请设置后登录
            throw new ServiceException(TokenMsg.EXCEPTION_USER_MENU_NOT_NULL);
        }

        // 检测用户是否有角色权限
        List<String> userAllPermsList = UserUtil.getUserAllPermsByUserId(isSwitchedUser.getId());
        if(CollUtil.isEmpty(userAllPermsList)){
            // 用户暂无角色菜单，请设置后登录
            throw new ServiceException(TokenMsg.EXCEPTION_USER_PERMS_NOT_NULL);
        }

        // 更新 当前用户缓存
        currUser.setSwitchTenantId(tenantId);
        currUser.setSwitchTenantUserId(isSwitchedUser.getId());

        return UserUtil.updateUser(currUser)
                ? ResultVo.success("切换租户成功")
                : ResultVo.error("切换租户失败");
    }


    /**
     * 切换回自己账户
     * @return ResultVo
     */
    @ApiOperation(value = "切换回自己账户", notes = "切换回自己账户")
    @Override
    public ResultVo<?> switchOneself() {
        UserModel currUser = UserUtil.getUserBySource();
        if (!DictType.NO_YES_YES.getValue().equals(currUser.getEnableSwitchTenant())){
            // 不允许切换租户
            throw new ServiceException(SystemMsg.EXCEPTION_USER_SWITCH_NOT_ALLOWED);
        }

        currUser.setSwitchTenantId(null);
        currUser.setSwitchTenantUserId(null);

        return UserUtil.updateUser(currUser)
                ? ResultVo.success("切换成功")
                : ResultVo.error("切换失败");
    }

}
