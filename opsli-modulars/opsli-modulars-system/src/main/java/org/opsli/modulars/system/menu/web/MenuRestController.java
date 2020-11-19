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
package org.opsli.modulars.system.menu.web;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.menu.MenuApi;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.concroller.BaseRestController;
import org.opsli.core.general.StartPrint;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.menu.service.IMenuService;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 菜单
 */
@Slf4j
@ApiRestController("/sys/menu")
public class MenuRestController extends BaseRestController<SysMenu, MenuModel, IMenuService>
        implements MenuApi {
    /**
     * 根据 获得用户 菜单 - 权限
     * @return ResultVo
     */
    @ApiOperation(value = "根据 获得用户 菜单 - 权限", notes = "根据 获得用户 菜单 - 权限")
    @Override
    public ResultVo<?> getMenuAndPermsTree() {
        // 菜单集合
        List<MenuModel> menuModelList = null;
        QueryBuilder<SysMenu> queryBuilder = new GenQueryBuilder<>();
        // 判断是否是超级管理员，如果是 则显示全部菜单 否则显示有权限菜单
        UserModel user = UserUtil.getUser();

        // 获得全量数据
        if(UserUtil.SUPER_ADMIN.equals(user.getUsername())){
            List<SysMenu> menuList = IService.findList(queryBuilder.build());
            menuModelList = WrapperUtil.transformInstance(menuList, MenuModel.class);
        }else {
            List<MenuModel> menuListByUserId = UserUtil.getMenuListByUserId(user.getId());
            if(menuListByUserId != null){
                // 这里有 ehcache的坑 需要深克隆再操作
                menuModelList = WrapperUtil.cloneTransformInstance(menuListByUserId
                        ,MenuModel.class);
            }

            List<String> perms = UserUtil.getUserAllPermsByUserId(user.getId());
            if(perms != null){
                QueryWrapper<SysMenu> wrapper = queryBuilder.build();
                wrapper.in("menu_code", perms);
                List<SysMenu> sysMenus = IService.findList(wrapper);
                List<MenuModel> menuModels = WrapperUtil.transformInstance(sysMenus, MenuModel.class);
                if(menuModelList != null){
                    menuModelList.addAll(menuModels);
                }
            }
        }
        if(menuModelList == null){
            return ResultVo.error("菜单为空");
        }

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey("order");
        // 最大递归深度 最多支持4层菜单
        treeNodeConfig.setDeep(4);

        //转换器
        List<Tree<String>> treeNodes = TreeUtil.build(menuModelList, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(treeNode.getSortNo());
                    tree.setName(treeNode.getMenuName());
                    // 扩展属性 ...
                    // 不是外链 则处理组件
                    if(!"3".equals(treeNode.getType())){
                        tree.putExtra("component", treeNode.getComponent());
                    }
                    tree.putExtra("type", treeNode.getType());
                    tree.putExtra("path", treeNode.getUrl());
                    tree.putExtra("redirect", treeNode.getRedirect());
                    // 处理 meta
                    Map<String,String> metaMap = Maps.newHashMapWithExpectedSize(3);
                    metaMap.put("title", treeNode.getMenuName());
                    metaMap.put("icon", treeNode.getIcon());
                    // 外链处理
                    if("3".equals(treeNode.getType())){
                        metaMap.put("target", "_blank");
                        metaMap.put("badge", "New");
                    }
                    tree.putExtra("meta", metaMap);
                });

        return ResultVo.success(treeNodes);
    }

    /**
     * 当前登陆用户菜单
     * @return ResultVo
     */
    @ApiOperation(value = "当前登陆用户菜单", notes = "当前登陆用户菜单")
    @Override
    public ResultVo<?> findMenuTree() {
        UserModel user = UserUtil.getUser();

        // 获得用户 对应菜单
        List<MenuModel> menuList = UserUtil.getMenuListByUserId(user.getId());

        if(menuList == null){
            return ResultVo.error(
                    SystemMsg.EXCEPTION_USER_MENU_NOT_NULL.getCode(),
                    SystemMsg.EXCEPTION_USER_MENU_NOT_NULL.getMessage());
        }

        // 这里有坑 如果 为 菜单数据 且 组件(Component)地址为空 不会跳转到主页 也不报错
        // 修复菜单问题导致无法跳转主页
        menuList.removeIf(menuModel -> "1".equals(menuModel.getType()) &&
                (StringUtils.isEmpty(menuModel.getComponent()) ||
                 StringUtils.isEmpty(menuModel.getMenuCode()) ||
                StringUtils.isEmpty(menuModel.getUrl())
                        ));

        // 处理系统根路径外链
        for (MenuModel menuModel : menuList) {
            if(StringUtils.isNotEmpty(menuModel.getUrl())){

            }
        }

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey("order");
        // 最大递归深度 最多支持4层菜单
        treeNodeConfig.setDeep(4);

        //转换器
        List<Tree<String>> treeNodes = TreeUtil.build(menuList, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(treeNode.getSortNo());
                    tree.setName(treeNode.getMenuCode());
                    // 扩展属性 ...
                    // 不是外链 则处理组件
                    if(!"3".equals(treeNode.getType())){
                        tree.putExtra("component", treeNode.getComponent());
                    }else{
                        // 如果是外链 则判断是否存在 BASE_PATH
                        // 设置BASE_PATH
                        if(StringUtils.isNotEmpty(treeNode.getUrl())){
                            treeNode.setUrl(treeNode.getUrl().replace("${BASE_PATH}",
                                    StartPrint.INSTANCE.getBasePath()
                            ));
                        }
                    }
                    tree.putExtra("path", treeNode.getUrl());
                    tree.putExtra("type", treeNode.getType());
                    tree.putExtra("redirect", treeNode.getRedirect());
                    // 处理 meta
                    Map<String,String> metaMap = Maps.newHashMapWithExpectedSize(3);
                    metaMap.put("title", treeNode.getMenuName());
                    metaMap.put("icon", treeNode.getIcon());
                    // 外链处理
                    if("3".equals(treeNode.getType())){
                        metaMap.put("target", "_blank");
                    }
                    tree.putExtra("meta", metaMap);
                });

        return ResultVo.success(treeNodes);
    }

    /**
     * 获得菜单树
     * @return ResultVo
     */
    @ApiOperation(value = "获得菜单树", notes = "获得菜单树")
    @RequiresPermissions("system_menu_select")
    @Override
    public ResultVo<?> findMenuTreePage(HttpServletRequest request) {

        QueryBuilder<SysMenu> queryBuilder = new WebQueryBuilder<>(SysMenu.class,
                request.getParameterMap());


        // 获得用户 对应菜单
        List<SysMenu> menuList = IService.findList(queryBuilder.build());

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey("sortNo");
        treeNodeConfig.setNameKey("menuName");
        // 最大递归深度 最多支持4层菜单
        treeNodeConfig.setDeep(4);

        //转换器
        List<Tree<String>> treeNodes = TreeUtil.build(menuList, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(treeNode.getSortNo());
                    tree.setName(treeNode.getMenuName());
                    // 扩展属性 ...
                    // 不是外链 则处理组件
                    tree.putExtra("menuCode", treeNode.getMenuCode());
                    tree.putExtra("component", treeNode.getComponent());
                    tree.putExtra("type", treeNode.getType());
                    tree.putExtra("url", treeNode.getUrl());
                    tree.putExtra("redirect", treeNode.getRedirect());
                    tree.putExtra("icon", treeNode.getIcon());
                    tree.putExtra("hidden", treeNode.getHidden());
                    tree.putExtra("version", treeNode.getVersion());
                });

        return ResultVo.success(treeNodes);
    }

    /**
     * 获得菜单List
     * @return ResultVo
     */
    @ApiOperation(value = "获得菜单List", notes = "获得菜单List")
    @RequiresPermissions("system_menu_select")
    @Override
    public ResultVo<List<MenuModel>> findList() {
        QueryBuilder<SysMenu> queryBuilder = new GenQueryBuilder<>();
        // 菜单集合
        List<SysMenu> menuList = IService.findList(queryBuilder.build());
        List<MenuModel> menuModelList = WrapperUtil.transformInstance(menuList, MenuModel.class);
        return ResultVo.success(menuModelList);
    }

    /**
     * 菜单 查一条
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "获得单条菜单", notes = "获得单条菜单 - ID")
    @RequiresPermissions("system_menu_select")
    @Override
    public ResultVo<MenuModel> get(MenuModel model) {
        // 如果系统内部调用 则直接查数据库
        if(model != null && model.getIzApi() != null && model.getIzApi()){
            model = IService.get(model);
        }
        return ResultVo.success(model);
    }

    /**
     * 菜单 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultVo
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @RequiresPermissions("system_menu_select")
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<SysMenu> queryBuilder = new WebQueryBuilder<>(SysMenu.class, request.getParameterMap());
        Page<SysMenu, MenuModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultVo.success(page.getBootstrapData());
    }

    /**
     * 菜单 新增
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "新增菜单", notes = "新增菜单")
    @RequiresPermissions("system_menu_insert")
    @EnableLog
    @Override
    public ResultVo<?> insert(MenuModel model) {
        // 调用新增方法
        IService.insert(model);
        return ResultVo.success("新增菜单成功");
    }

    /**
     * 菜单 修改
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "修改菜单", notes = "修改菜单")
    @RequiresPermissions("system_menu_update")
    @EnableLog
    @Override
    public ResultVo<?> update(MenuModel model) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改菜单成功");
    }


    /**
     * 菜单 删除
     * @param id ID
     * @return ResultVo
     */
    @ApiOperation(value = "删除菜单数据", notes = "删除菜单数据")
    @RequiresPermissions("system_menu_delete")
    @EnableLog
    @Override
    public ResultVo<?> del(String id){
        // 演示模式 不允许操作
        super.demoError();

        IService.delete(id);
        return ResultVo.success("删除菜单成功");
    }


    /**
     * 菜单 批量删除
     * @param ids ID 数组
     * @return ResultVo
     */
    @ApiOperation(value = "批量删除菜单数据", notes = "批量删除菜单数据")
    @RequiresPermissions("system_menu_delete")
    @EnableLog
    @Override
    public ResultVo<?> delAll(String[] ids){
        // 演示模式 不允许操作
        super.demoError();

        IService.deleteAll(ids);
        return ResultVo.success("批量删除菜单成功");
    }


    /**
     * 菜单 Excel 导出
     * @param request request
     * @param response response
     * @return ResultVo
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @RequiresPermissions("system_menu_export")
    @EnableLog
    @Override
    public ResultVo<?> exportExcel(HttpServletRequest request, HttpServletResponse response) {
        QueryBuilder<SysMenu> queryBuilder = new WebQueryBuilder<>(SysMenu.class, request.getParameterMap());
        return super.excelExport(MenuApi.TITLE, queryBuilder.build(), response);
    }

    /**
     * 菜单 Excel 导入
     * @param request 文件流 request
     * @return ResultVo
     */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @RequiresPermissions("system_menu_import")
    @EnableLog
    @Override
    public ResultVo<?> excelImport(MultipartHttpServletRequest request) {
        return super.excelImport(request);
    }

    /**
     * 菜单 Excel 下载导入模版
     * @param response response
     * @return ResultVo
     */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissions("system_menu_import")
    @EnableLog
    @Override
    public ResultVo<?> importTemplate(HttpServletResponse response) {
        return super.importTemplate(MenuApi.TITLE, response);
    }


    // ==================

    /**
     * 根据菜单编号 获得菜单
     * @param menuCode 菜单编号
     * @return ResultVo
     */
    @Override
    public ResultVo<MenuModel> getByCode(String menuCode) {
        MenuModel menu = IService.getByCode(menuCode);
        if(menu == null){
            ResultVo.error("暂无数据");
        }
        return ResultVo.success(menu);
    }
}
