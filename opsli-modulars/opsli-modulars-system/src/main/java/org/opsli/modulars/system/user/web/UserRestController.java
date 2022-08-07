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
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.encrypt.EncryptModel;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.user.UserApi;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.options.OptionsModel;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.api.wrapper.system.tenant.TenantModel;
import org.opsli.api.wrapper.system.user.*;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.enums.VerificationTypeEnum;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.exception.TokenException;
import org.opsli.common.utils.FieldUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.log.annotation.OperateLogger;
import org.opsli.core.log.enums.ModuleEnum;
import org.opsli.core.log.enums.OperationTypeEnum;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.core.persistence.querybuilder.conf.WebQueryConf;
import org.opsli.core.utils.*;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.entity.SysUserWeb;
import org.opsli.modulars.system.user.service.IUserRoleRefService;
import org.opsli.modulars.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;


/**
 * 用户管理 Controller
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Api(tags = UserApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/system/user")
public class UserRestController extends BaseRestController<SysUser, UserModel, IUserService>
        implements UserApi {

    @Autowired
    private IUserRoleRefService iUserRoleRefService;

    /**
     * 当前登陆用户信息
     * @return ResultWrapper
     */
    @ApiOperation(value = "当前登陆用户信息", notes = "当前登陆用户信息")
    @Override
    public ResultWrapper<UserInfo> getInfo(HttpServletRequest request) {
        UserModel currUser = UserUtil.getUserBySource();
        return this.getInfoById(currUser.getId());
    }

    /**
     * 当前登陆用户信息 By Id
     * @return ResultWrapper
     */
    @ApiOperation(value = "当前登陆用户信息 By Id", notes = "当前登陆用户信息 By Id")
    @Override
    public ResultWrapper<UserInfo> getInfoById(String userId) {
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

        // 脱敏
        userInfo.setEmail(DesensitizedUtil.email(userInfo.getEmail()));
        userInfo.setMobile(DesensitizedUtil.mobilePhone(userInfo.getMobile()));

        return ResultWrapper.getSuccessResultWrapper(userInfo);
    }


    /**
     * 当前登陆用户组织机构
     * @return ResultWrapper
     */
    @ApiOperation(value = "当前登陆用户组织机构", notes = "当前登陆用户组织机构")
    @Override
    public ResultWrapper<?> getOrg() {
        UserModel user = UserUtil.getUser();
        return this.getOrgByUserId(user.getId());
    }

    /**
     * 用户组织机构
     * @param userId 用户ID
     * @return ResultWrapper
     */
    @ApiOperation(value = "用户组织机构", notes = "用户组织机构")
    @Override
    public ResultWrapper<?> getOrgByUserId(String userId) {
        List<UserOrgRefModel> orgListByUserId = UserUtil.getOrgListByUserId(userId);
        return ResultWrapper.getSuccessResultWrapper(orgListByUserId);
    }

    /**
     * 根据 userId 获得用户角色Id集合
     * @param userId 用户Id
     * @return ResultWrapper
     */
    @ApiOperation(value = "根据 userId 获得用户角色Id集合", notes = "根据 userId 获得用户角色Id集合")
    @Override
    public ResultWrapper<List<String>> getRoleIdsByUserId(String userId) {
        List<String> roleIdList = iUserRoleRefService.getRoleIdList(userId);
        return ResultWrapper.getSuccessResultWrapper(roleIdList);
    }



    /**
     * 修改密码
     * @return ResultWrapper
     */
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @Override
    public ResultWrapper<?> updatePassword(EncryptModel encryptModel) {
        // 演示模式 不允许操作
        super.demoError();

        Object asymmetricDecryptToObj = CryptoUtil.asymmetricDecryptToObj(encryptModel.getEncryptData());
        // 转换模型
        UserPassword userPassword = WrapperUtil.transformInstance(
                                        asymmetricDecryptToObj, UserPassword.class);

        // 验证对象
        ValidatorUtil.verify(userPassword);

        UserModel user = UserUtil.getUserBySource();
        userPassword.setUserId(user.getId());
        IService.updatePasswordByCheckOld(userPassword);
        return ResultWrapper.getSuccessResultWrapper();
    }

    /**
     * 修改密码 忘记密码
     * @return ResultWrapper
     */
    @Override
    public ResultWrapper<?> updatePasswordByForget(EncryptModel encryptModel) {
        // 演示模式 不允许操作
        super.demoError();

        Object asymmetricDecryptToObj = CryptoUtil.asymmetricDecryptToObj(encryptModel.getEncryptData());
        // 转换模型
        UpdateUserPasswordByForgetModel updateUserPasswordByForgetModel = WrapperUtil.transformInstance(
                asymmetricDecryptToObj, UpdateUserPasswordByForgetModel.class);

        // 验证授权是否正确
        VerificationCodeUtil.checkCertificate(
                VerificationTypeEnum.AUTH.getType(), updateUserPasswordByForgetModel.getCertificate());

        UserModel userBySource = UserUtil.getUserBySource();

        // 转换模型
        ToUserPassword userPassword = new ToUserPassword();
        userPassword.setUserId(userBySource.getId());
        userPassword.setNewPassword(updateUserPasswordByForgetModel.getNewPassword());

        IService.updatePasswordByNotCheckOld(userPassword);
        return ResultWrapper.getSuccessResultWrapper();
    }

    /**
     * 修改邮箱
     * @return ResultWrapper
     */
    @ApiOperation(value = "修改邮箱", notes = "修改邮箱")
    @Override
    public ResultWrapper<?> updateEmail(EncryptModel encryptModel) {
        // 演示模式 不允许操作
        super.demoError();

        Object asymmetricDecryptToObj = CryptoUtil.asymmetricDecryptToObj(encryptModel.getEncryptData());
        // 转换模型
        UpdateUserEmailModel updateUserEmailModel = WrapperUtil.transformInstance(
                asymmetricDecryptToObj, UpdateUserEmailModel.class);

        // 验证对象
        ValidatorUtil.verify(updateUserEmailModel);

        // 修改用户邮箱
        IService.updateUserEmail(updateUserEmailModel);
        return ResultWrapper.getSuccessResultWrapper();
    }

    /**
     * 修改手机
     * @return ResultWrapper
     */
    @ApiOperation(value = "修改手机", notes = "修改手机")
    @Override
    public ResultWrapper<?> updateMobile(EncryptModel encryptModel) {
        // 演示模式 不允许操作
        super.demoError();

        Object asymmetricDecryptToObj = CryptoUtil.asymmetricDecryptToObj(encryptModel.getEncryptData());
        // 转换模型
        UpdateUserMobileModel updateUserMobileModel = WrapperUtil.transformInstance(
                asymmetricDecryptToObj, UpdateUserMobileModel.class);

        // 验证对象
        ValidatorUtil.verify(updateUserMobileModel);

        // 修改用户邮箱
        IService.updateUserMobile(updateUserMobileModel);
        return ResultWrapper.getSuccessResultWrapper();
    }

    /**
     * 上传头像
     * @param userAvatarModel 图片地址
     * @return ResultWrapper
     */
    @ApiOperation(value = "上传头像", notes = "上传头像")
    @Override
    public ResultWrapper<?> updateAvatar(UserAvatarModel userAvatarModel) {
        UserModel user = UserUtil.getUserBySource();
        // 更新头像至数据库
        UserModel userModel = new UserModel();
        userModel.setId(user.getId());
        userModel.setAvatar(userAvatarModel.getImgUrl());
        IService.updateAvatar(userModel);
        // 刷新用户信息
        UserUtil.refreshUser(user);
        return ResultWrapper.getSuccessResultWrapper();
    }

    // ==================================================

    /**
     * 修改密码
     * @return ResultVo
     */
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @PreAuthorize("hasAuthority('system_user_updatePassword')")
    @OperateLogger(description = "修改密码",
            module = ModuleEnum.MODULE_USER, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> updatePasswordById(EncryptModel encryptModel) {
        // 演示模式 不允许操作
        super.demoError();

        Object asymmetricDecryptToObj = CryptoUtil.asymmetricDecryptToObj(encryptModel.getEncryptData());
        // 转换模型
        ToUserPassword userPassword = WrapperUtil.transformInstance(
                asymmetricDecryptToObj, ToUserPassword.class);

        // 验证对象
        ValidatorUtil.verify(userPassword);

        IService.updatePasswordByNotCheckOld(userPassword);
        return ResultWrapper.getSuccessResultWrapper();
    }

    /**
     * 重置密码
     * @return ResultWrapper
     */
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @PreAuthorize("hasAuthority('system_user_resetPassword')")
    @OperateLogger(description = "重置密码",
            module = ModuleEnum.MODULE_USER, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> resetPasswordById(EncryptModel encryptModel) {
        // 演示模式 不允许操作
        super.demoError();

        Object asymmetricDecryptToObj = CryptoUtil.asymmetricDecryptToObj(encryptModel.getEncryptData());
        // 转换模型
        String userId = JSONUtil.parseObj(asymmetricDecryptToObj).getStr("userId");
        if(StrUtil.isBlank(userId)){
            throw new ServiceException(SystemMsg.EXCEPTION_USER_ILLEGAL_PARAMETER);
        }

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
            // 重制密码失败
            return ResultWrapper.getCustomResultWrapper(SystemMsg.EXCEPTION_RESET_PASSWORD);
        }

        return ResultWrapper.getSuccessResultWrapperByMsg("重置密码成功！默认密码为：" + defPass);
    }

    /**
     * 变更账户状态
     * @return ResultWrapper
     */
    @ApiOperation(value = "锁定账户", notes = "锁定账户")
    @PreAuthorize("hasAuthority('system_user_enable')")
    @OperateLogger(description = "锁定账户",
            module = ModuleEnum.MODULE_USER, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> enableAccount(EncryptModel encryptModel) {
        // 演示模式 不允许操作
        super.demoError();

        Object asymmetricDecryptToObj = CryptoUtil.asymmetricDecryptToObj(encryptModel.getEncryptData());
        // 转换模型
        EnableUserModel enableUserModel = WrapperUtil.transformInstance(
                asymmetricDecryptToObj, EnableUserModel.class);

        // 验证对象
        ValidatorUtil.verify(enableUserModel);

        // 变更账户状态
        boolean lockAccountFlag = IService.enableAccount(
                enableUserModel.getUserId(), enableUserModel.getEnabled());
        if(!lockAccountFlag){
            // 变更状态失败
            return ResultWrapper.getCustomResultWrapper(SystemMsg.EXCEPTION_CHANGE_STATUS);
        }
        return ResultWrapper.getSuccessResultWrapper();
    }


    /**
     * 用户信息 查一条
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得单条用户信息", notes = "获得单条用户信息 - ID")
    // 因为工具类 使用到该方法 不做权限验证
    //@PreAuthorize("hasAuthority('system_user_select')")
    @Override
    public ResultWrapper<UserModel> get(UserModel model) {
        model = IService.get(model);
        return ResultWrapper.getSuccessResultWrapper(model);
    }

    /**
     * 用户信息 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param orgIdGroup 组织ID组
     * @param request request
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @PreAuthorize("hasAuthority('system_user_select')")
    @Override
    public ResultWrapper<?> findPage(Integer pageNo, Integer pageSize,
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
            userModel.setPassword(null);
            userModel.setPasswordLevel(null);
        }
        return ResultWrapper.getSuccessResultWrapper(page.getPageData());
    }

    /**
     * 租户管理员信息 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @PreAuthorize("hasAuthority('system_set_tenant_admin')")
    @Override
    public ResultWrapper<?> findPageByTenant(Integer pageNo, Integer pageSize,
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
            userModel.setPassword(null);
            userModel.setPasswordLevel(null);
        }
        return ResultWrapper.getSuccessResultWrapper(page.getPageData());
    }

    /**
     * 用户信息 新增
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "新增用户信息", notes = "新增用户信息")
    @PreAuthorize("hasAuthority('system_user_insert')")
    @OperateLogger(description = "新增用户信息",
            module = ModuleEnum.MODULE_USER, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> insert(UserModel model) {
        // 调用新增方法
        UserModel userModel = IService.insert(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("新增用户信息成功")
                .setData(userModel);
    }

    /**
     * 用户信息 修改
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @PreAuthorize("hasAuthority('system_user_update')")
    @OperateLogger(description = "修改用户信息",
            module = ModuleEnum.MODULE_USER, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> update(UserModel model) {
        // 演示模式 不允许操作
        super.demoError();
        // 调用修改方法
        IService.update(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("修改用户信息成功");
    }

    /**
     * 用户信息 自身修改
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "修改自身用户信息", notes = "修改自身用户信息")
    @OperateLogger(description = "修改自身用户信息",
            module = ModuleEnum.MODULE_USER, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> updateSelf(UserModel model) {
        UserModel currUser = UserUtil.getUserBySource();
        if(!StringUtils.equals(currUser.getId(), model.getId())){
            // 非法参数 防止其他用户 通过该接口 修改非自身用户数据
            throw new ServiceException(SystemMsg.EXCEPTION_USER_ILLEGAL_PARAMETER);
        }

        // 防止篡改手机号、邮箱号
        model.setMobile(null);
        model.setEmail(null);

        // 调用修改方法
        IService.update(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("修改用户信息成功");
    }

    /**
     * 用户信息 删除
     * @param encryptModel 加密 id ID
     * @return ResultWrapper
     */
    @ApiOperation(value = "删除用户信息数据", notes = "删除用户信息数据")
    @PreAuthorize("hasAuthority('system_user_delete')")
    @OperateLogger(description = "删除用户信息数据",
            module = ModuleEnum.MODULE_USER, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> del(EncryptModel encryptModel) {
        // 演示模式 不允许操作
        super.demoError();

        Object asymmetricDecryptToObj = CryptoUtil.asymmetricDecryptToObj(encryptModel.getEncryptData());
        // 转换模型
        String id = JSONUtil.parseObj(asymmetricDecryptToObj).getStr("id");

        IService.delete(id);

        return ResultWrapper.getSuccessResultWrapperByMsg("删除用户信息成功");
    }


    /**
     * 用户信息 批量删除
     * @param encryptModel 加密 ID 数组
     * @return ResultWrapper
     */
    @ApiOperation(value = "批量删除用户信息数据", notes = "批量删除用户信息数据")
    @PreAuthorize("hasAuthority('system_user_delete')")
    @OperateLogger(description = "批量删除用户信息数据",
            module = ModuleEnum.MODULE_USER, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> delAll(EncryptModel encryptModel) {
        // 演示模式 不允许操作
        super.demoError();

        Object asymmetricDecryptToObj = CryptoUtil.asymmetricDecryptToObj(encryptModel.getEncryptData());
        // 转换模型
        String ids = JSONUtil.parseObj(asymmetricDecryptToObj).getStr("ids");

        String[] idArray = Convert.toStrArray(ids);
        IService.deleteAll(idArray);

        return ResultWrapper.getSuccessResultWrapperByMsg("批量删除用户信息成功");
    }


    /**
     * 用户信息 Excel 导出认证
     *
     * @param type 类型
     * @param request request
     */
    @ApiOperation(value = "Excel 导出认证", notes = "Excel 导出认证")
    @PreAuthorize("hasAnyAuthority('system_user_export', 'system_user_import')")
    @Override
    public ResultWrapper<String> exportExcelAuth(String type, HttpServletRequest request) {
        Optional<String> certificateOptional =
                super.excelExportAuth(type, UserApi.SUB_TITLE, request);
        if(!certificateOptional.isPresent()){
            return ResultWrapper.getErrorResultWrapper();
        }
        return ResultWrapper.getSuccessResultWrapper(certificateOptional.get());
    }


    /**
     * 用户信息 Excel 导出
     * @param response response
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @PreAuthorize("hasAuthority('system_user_export')")
    @OperateLogger(description = "导出Excel",
            module = ModuleEnum.MODULE_USER, operationType = OperationTypeEnum.SELECT, db = true)
    @Override
    public void exportExcel(String certificate, HttpServletResponse response) {
        // 导出Excel
        super.excelExport(certificate, response);
    }

    /**
     * 用户信息 Excel 导入
     * @param request 文件流 request
     * @return ResultWrapper
     */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @PreAuthorize("hasAuthority('system_user_import')")
    @OperateLogger(description = "用户信息 Excel 导入",
            module = ModuleEnum.MODULE_USER, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }


    /**
     * 根据 username 获得用户
     * @param username 用户名
     * @return ResultWrapper
     */
    @ApiOperation(value = "根据 username 获得用户", notes = "根据 username 获得用户")
    @Override
    public ResultWrapper<UserModel> getUserByUsername(String username) {
        UserModel userModel = IService.queryByUserName(username);
        if(userModel == null){
            // 暂无该用户
            throw new ServiceException(SystemMsg.EXCEPTION_USER_NULL.getCode(),
                    StrUtil.format(SystemMsg.EXCEPTION_USER_NULL.getMessage(), username)
            );
        }
        return ResultWrapper.getSuccessResultWrapper(userModel);
    }

    @ApiOperation(value = "根据 手机号 获得用户", notes = "根据 手机号 获得用户")
    @Override
    public ResultWrapper<UserModel> getUserByMobile(String mobile) {
        UserModel userModel = IService.queryByMobile(mobile);
        if(userModel == null){
            // 暂无该用户
            throw new ServiceException(SystemMsg.EXCEPTION_USER_NULL.getCode(),
                    StrUtil.format(SystemMsg.EXCEPTION_USER_NULL.getMessage(), mobile)
            );
        }
        return ResultWrapper.getSuccessResultWrapper(userModel);
    }

    @ApiOperation(value = "根据 邮箱 获得用户", notes = "根据 邮箱 获得用户")
    @Override
    public ResultWrapper<UserModel> getUserByEmail(String email) {
        UserModel userModel = IService.queryByEmail(email);
        if(userModel == null){
            // 暂无该用户
            throw new ServiceException(SystemMsg.EXCEPTION_USER_NULL.getCode(),
                    StrUtil.format(SystemMsg.EXCEPTION_USER_NULL.getMessage(), email)
            );
        }
        return ResultWrapper.getSuccessResultWrapper(userModel);
    }

    /**
     * 切换租户
     * @param tenantId 租户ID
     * @return ResultWrapper
     */
    @ApiOperation(value = "切换租户", notes = "切换租户")
    @Override
    public ResultWrapper<?> switchTenant(String tenantId) {
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
                ? ResultWrapper.getSuccessResultWrapperByMsg("切换租户成功")
                : ResultWrapper.getErrorResultWrapper().setMsg("切换租户失败");
    }


    /**
     * 切换回自己账户
     * @return ResultWrapper
     */
    @ApiOperation(value = "切换回自己账户", notes = "切换回自己账户")
    @Override
    public ResultWrapper<?> switchOneself() {
        UserModel currUser = UserUtil.getUserBySource();
        if (!DictType.NO_YES_YES.getValue().equals(currUser.getEnableSwitchTenant())){
            // 不允许切换租户
            throw new ServiceException(SystemMsg.EXCEPTION_USER_SWITCH_NOT_ALLOWED);
        }

        currUser.setSwitchTenantId(null);
        currUser.setSwitchTenantUserId(null);

        return UserUtil.updateUser(currUser)
                ? ResultWrapper.getSuccessResultWrapperByMsg("切换成功")
                : ResultWrapper.getErrorResultWrapper().setMsg("切换失败");
    }

}
