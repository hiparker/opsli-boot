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
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.user.UserApi;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.*;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.common.annotation.RequiresPermissionsCus;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.exception.TokenException;
import org.opsli.common.utils.HumpUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.core.utils.OrgUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.org.entity.SysOrg;
import org.opsli.modulars.system.org.service.ISysOrgService;
import org.opsli.modulars.system.org.web.SysOrgRestController;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.entity.SysUserAndOrg;
import org.opsli.modulars.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 用户信息
 */
@Slf4j
@ApiRestController("/sys/user")
public class UserRestController extends BaseRestController<SysUser, UserModel, IUserService>
        implements UserApi {

    @Autowired
    private ISysOrgService iSysOrgService;

    /**
     * 当前登陆用户信息
     * @return ResultVo
     */
    @ApiOperation(value = "当前登陆用户信息", notes = "当前登陆用户信息")
    @Override
    public ResultVo<UserInfo> getInfo(HttpServletRequest request) {
        UserModel user = UserUtil.getUser();
        return this.getInfoById(user.getId());
    }

    /**
     * 当前登陆用户信息 By Id
     * @return ResultVo
     */
    @ApiOperation(value = "当前登陆用户信息 By Id", notes = "当前登陆用户信息 By Id")
    @Override
    public ResultVo<UserInfo> getInfoById(String userId) {
        UserModel user = UserUtil.getUser(userId);
        if(user == null){
            throw new TokenException(TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY);
        }

        // 个人信息 清除敏感信息
        user.setPassword(null);
        List<String> userRolesByUserId = UserUtil.getUserRolesByUserId(user.getId());
        List<String> userAllPermsByUserId = UserUtil.getUserAllPermsByUserId(user.getId());
        UserInfo userInfo = WrapperUtil.transformInstance(user, UserInfo.class);
        userInfo.setRoles(userRolesByUserId);
        userInfo.setPerms(userAllPermsByUserId);

        // 判断是否是超级管理员
        if(StringUtils.equals(UserUtil.SUPER_ADMIN, user.getUsername())){
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
    public ResultVo<UserOrgRefModel> getOrg() {
        UserModel user = UserUtil.getUser();
        return this.getOrgByUserId(user.getId());
    }

    /**
     * 用户组织机构
     * @param userId
     * @return ResultVo
     */
    @ApiOperation(value = "用户组织机构", notes = "用户组织机构")
    @Override
    public ResultVo<UserOrgRefModel> getOrgByUserId(String userId) {
        UserOrgRefModel orgRef = OrgUtil.getOrgByUserId(userId);
        return ResultVo.success(orgRef);
    }

    /**
     * 根据 userId 获得用户角色Id集合
     * @param userId 用户Id
     * @return ResultVo
     */
    @ApiOperation(value = "根据 userId 获得用户角色Id集合", notes = "根据 userId 获得用户角色Id集合")
    @Override
    public ResultVo<List<String>> getRoleIdsByUserId(String userId) {
        List<String> roleIdList = IService.getRoleIdList(userId);
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

        UserModel user = UserUtil.getUser();
        userPassword.setUserId(user.getId());
        IService.updatePassword(userPassword);
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
            return ResultVo.error(CoreMsg.EXCEL_FILE_NULL.getCode(),
                    CoreMsg.EXCEL_FILE_NULL.getMessage());
        }

        try {
            String staticPath = "/static/file";
            UserModel user = UserUtil.getUser();

            String date = DateUtil.format(DateUtil.date(), "yyyyMMdd");
            String dateTime = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
            String packageName = globalProperties.getWeb().getUploadPath() + staticPath+"/"+date;
            String fileName = dateTime+"-"+ IdUtil.simpleUUID() +".jpg";
            File file = new File(packageName+"/"+fileName);
            MultipartFile multipartFile = files.get(0);
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
            FileUtil.mkdir(packageName);
            FileUtil.touch(file);

            // 更新头像至数据库
            UserModel userModel = new UserModel();
            userModel.setId(user.getId());
            userModel.setAvatar(staticPath + "/" +date+"/"+fileName);
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
    public ResultVo<?> updatePasswordById(UserPassword userPassword) {
        // 演示模式 不允许操作
        super.demoError();

        IService.updatePassword(userPassword);
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

        UserPassword userPassword = new UserPassword();
        userPassword.setNewPassword(globalProperties.getAuth().getDefaultPass());
        userPassword.setUserId(userId);

        boolean resetPasswordFlag = IService.resetPassword(userPassword);
        if(!resetPasswordFlag){
            return ResultVo.error("重置密码失败");
        }

        return ResultVo.success("重置密码成功！默认密码为：" + globalProperties.getAuth().getDefaultPass());
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
     * @param request request
     * @return ResultVo
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @RequiresPermissions("system_user_select")
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, UserOrgRefModel org, HttpServletRequest request) {
        QueryBuilder<SysUserAndOrg> queryBuilder = new WebQueryBuilder<>(SysUserAndOrg.class, request.getParameterMap());
        Page<SysUserAndOrg, UserAndOrgModel> page = new Page<>(pageNo, pageSize);
        QueryWrapper<SysUserAndOrg> queryWrapper = queryBuilder.build();
        if(org != null){
            // 公司ID
            if(StringUtils.isNotBlank(org.getCompanyId())){
                // 未分组判断
                if(SysOrgRestController.ORG_NULL.equals(org.getCompanyId())){
                    queryWrapper.isNull("b.org_id");

                }else{
                    queryWrapper.eq("b.org_id", org.getCompanyId());
                }
            }

            // 部门ID
            if(StringUtils.isNotBlank(org.getDepartmentId())){
                queryWrapper.eq("c.org_id", org.getDepartmentId());
            }

            // 岗位ID
            if(StringUtils.isNotBlank(org.getPostId())){
                queryWrapper.eq("d.org_id", org.getPostId());
            }
        }
        page.setQueryWrapper(queryWrapper);
        page = IService.findPageByCus(page);
        // 密码防止分页泄露处理
        for (UserAndOrgModel userModel : page.getList()) {
            userModel.setSecretkey(null);
            userModel.setPassword(null);
        }
        return ResultVo.success(page.getBootstrapData());
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
        IService.insert(model);
        return ResultVo.success("新增用户信息成功");
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
     * @return ResultVo
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @RequiresPermissionsCus("system_user_export")
    @EnableLog
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "exportExcel");
        QueryBuilder<SysUser> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        super.excelExport(UserApi.TITLE, queryBuilder.build(), response, method);
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
     * @return ResultVo
     */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissionsCus("system_user_import")
    @Override
    public void importTemplate(HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "importTemplate");
        super.importTemplate(UserApi.TITLE, response, method);
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
            throw new ServiceException(SystemMsg.EXCEPTION_USER_NULL);
        }
        return ResultVo.success(userModel);
    }

    /**
     * 根据 userId 获得用户角色
     * @param userId 用户Id
     * @return ResultVo
     */
    @Override
    public ResultVo<List<String>> getRolesByUserId(String userId) {
        List<String> roleCodeList = IService.getRoleCodeList(userId);
        return ResultVo.success(roleCodeList);
    }

    /**
     * 根据 userId 获得用户权限
     * @param userId 用户Id
     * @return ResultVo
     */
    @Override
    public ResultVo<List<String>> getAllPerms(String userId) {
        List<String> allPerms = IService.getAllPerms(userId);
        return ResultVo.success(allPerms);
    }


    /**
     * 根据 userId 获得用户菜单
     * @param userId 用户Id
     * @return ResultVo
     */
    @Override
    public ResultVo<List<MenuModel>> getMenuListByUserId(String userId) {
        List<MenuModel> menuModelList = IService.getMenuListByUserId(userId);
        return ResultVo.success(menuModelList);
    }


    /**
     * 用户组织机构
     * @param userId
     * @return ResultVo
     */
    @ApiOperation(value = "用户组织机构", notes = "用户组织机构")
    @Override
    public ResultVo<UserOrgRefModel> getOrgInfoByUserId(String userId) {
        UserOrgRefModel org = null;
        // 不写SQL了 直接分页 第一页 取第一条
        QueryBuilder<SysUserAndOrg> queryBuilder = new GenQueryBuilder<>();
        Page<SysUserAndOrg, UserAndOrgModel> page = new Page<>(1, 1);
        QueryWrapper<SysUserAndOrg> queryWrapper = queryBuilder.build();
        queryWrapper.eq(
                "a.id",
                userId
        );
        page.setQueryWrapper(queryWrapper);
        page = IService.findPageByCus(page);
        List<UserAndOrgModel> list = page.getList();
        if(CollUtil.isNotEmpty(list)){
            UserAndOrgModel userAndOrgModel = list.get(0);
            if(userAndOrgModel != null){
                org  = userAndOrgModel.getOrg();
                if(org != null){

                    org.setUserId(userId);

                    List<String> orgIds = Lists.newArrayListWithCapacity(3);
                    orgIds.add(org.getCompanyId());
                    orgIds.add(org.getDepartmentId());
                    orgIds.add(org.getPostId());
                    QueryWrapper<SysOrg> orgQueryWrapper = new QueryWrapper<>();
                    orgQueryWrapper.in(
                            HumpUtil.humpToUnderline(MyBatisConstants.FIELD_ID),
                            orgIds);
                    List<SysOrg> orgList = iSysOrgService.findList(orgQueryWrapper);
                    if(CollUtil.isNotEmpty(orgList)){
                        Map<String, SysOrg> tmp = Maps.newHashMap();
                        for (SysOrg sysOrg : orgList) {
                            tmp.put(sysOrg.getId(), sysOrg);
                        }

                        // 设置 名称
                        SysOrg company = tmp.get(org.getCompanyId());
                        if(company != null){
                            org.setCompanyName(company.getOrgName());
                        }

                        SysOrg department = tmp.get(org.getDepartmentId());
                        if(department != null){
                            org.setDepartmentName(department.getOrgName());
                        }

                        SysOrg post = tmp.get(org.getPostId());
                        if(post != null){
                            org.setPostName(post.getOrgName());
                        }
                    }

                }
            }
        }
        return ResultVo.success(org);
    }
}
