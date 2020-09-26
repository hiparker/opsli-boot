package org.opsli.modulars.system.user.web;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.user.UserApi;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.core.base.concroller.BaseRestController;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.service.IUserService;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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


    /**
     * 用户信息 查一条
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "获得单条用户信息", notes = "获得单条用户信息 - ID")
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
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<SysUser> queryBuilder = new WebQueryBuilder<>(SysUser.class, request.getParameterMap());
        Page<SysUser, UserModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultVo.success(page.getBootstrapData());
    }

    /**
     * 用户信息 新增
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "新增用户信息", notes = "新增用户信息")
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
    @Override
    public ResultVo<?> update(UserModel model) {
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
    @Override
    public ResultVo<?> del(String id){
        IService.delete(id);
        return ResultVo.success("删除用户信息成功");
    }


    /**
     * 用户信息 批量删除
     * @param ids ID 数组
     * @return ResultVo
     */
    @ApiOperation(value = "批量删除用户信息数据", notes = "批量删除用户信息数据")
    @Override
    public ResultVo<?> delAll(String[] ids){
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
