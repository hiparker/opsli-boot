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

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.excel.util.CollectionUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.user.UserApi;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserInfo;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserPassword;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.common.exception.TokenException;
import org.opsli.common.utils.IPUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.concroller.BaseRestController;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


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

    @Value("${opsli.web.upload-path}")
    private String basedir;


    /**
     * 当前登陆用户信息
     * @return ResultVo
     */
    @ApiOperation(value = "当前登陆用户信息", notes = "当前登陆用户信息")
    @Override
    public ResultVo<UserInfo> getInfo(HttpServletRequest request) {
        UserModel user = UserUtil.getUser();

        // 保存用户最后登录IP
        String clientIpAddress = IPUtil.getClientIpAddress(request);
        user.setLoginIp(clientIpAddress);
        IService.updateLoginIp(user);
        UserUtil.refreshUser(user);

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
        if(UserUtil.SUPER_ADMIN.equals(userInfo.getUsername())){
            userInfo.setIzSuperAdmin(true);
        }

        return ResultVo.success(userInfo);
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
            String packageName = basedir + staticPath+"/"+date;
            String fileName = dateTime+"-"+ RandomUtil.simpleUUID() +".jpg";
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
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<SysUser> queryBuilder = new WebQueryBuilder<>(SysUser.class, request.getParameterMap());
        Page<SysUser, UserModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);
        // 密码防止分页泄露处理
        for (UserModel userModel : page.getList()) {
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
    public ResultVo<?> delAll(String[] ids){
        // 演示模式 不允许操作
        super.demoError();

        IService.deleteAll(ids);
        return ResultVo.success("批量删除用户信息成功");
    }


    /**
     * 用户信息 Excel 导出
     * @param request request
     * @param response response
     * @return ResultVo
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @RequiresPermissions("system_user_export")
    @EnableLog
    @Override
    public ResultVo<?> exportExcel(HttpServletRequest request, HttpServletResponse response) {
        QueryBuilder<SysUser> queryBuilder = new WebQueryBuilder<>(SysUser.class, request.getParameterMap());
        return super.excelExport(UserApi.TITLE, queryBuilder.build(), response);
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
    public ResultVo<?> excelImport(MultipartHttpServletRequest request) {
        return super.excelImport(request);
    }

    /**
     * 用户信息 Excel 下载导入模版
     * @param response response
     * @return ResultVo
     */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissions("system_user_import")
    @Override
    public ResultVo<?> importTemplate(HttpServletResponse response) {
        return super.importTemplate(UserApi.TITLE, response);
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
        if(userModel != null){
            return ResultVo.success(userModel);
        }
        return ResultVo.error("没有该用户");
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
}
