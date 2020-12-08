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
package org.opsli.modulars.system.org.web;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.org.SysOrgRestApi;
import org.opsli.api.wrapper.system.org.SysOrgModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.utils.HumpUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.concroller.BaseRestController;
import org.opsli.core.base.entity.HasChildren;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.system.org.entity.SysOrg;
import org.opsli.modulars.system.org.service.ISysOrgService;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @BelongsProject: opsli-boot
* @BelongsPackage: org.opsli.modulars.system.org.web
* @Author: Parker
* @CreateTime: 2020-11-28 18:59:59
* @Description: 组织机构表 Controller
*/
@Slf4j
@ApiRestController("/sys/org")
public class SysOrgRestController extends BaseRestController<SysOrg, SysOrgModel, ISysOrgService>
    implements SysOrgRestApi {

    /** 显示全部 */
    public static final String ORG_ALL = "all";
    /** 未分组 */
    public static final String ORG_NULL = "org_null";

    /**
     * 获得组织树 懒加载
     * @return ResultVo
     */
    @ApiOperation(value = "获得组织树 懒加载", notes = "获得组织树 懒加载")
    @Override
    public ResultVo<?> findTreeLazyByUser(String parentId) {

        QueryBuilder<SysOrg> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysOrg> wrapper = queryBuilder.build();
        wrapper.eq(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentId);

        // 获得用户 对应菜单
        List<SysOrg> dataList = IService.findList(wrapper);
        List<SysOrgModel> orgModelList = WrapperUtil.transformInstance(dataList, SysOrgModel.class);
        // 0 为初始值
        if("0".equals(parentId)){
            // 显示全部
            SysOrgModel orgAll = new SysOrgModel();
            orgAll.setId(ORG_ALL);
            orgAll.setOrgCode("-2");
            orgAll.setOrgName("全部");
            orgAll.setOrgType("-2");
            orgAll.setParentId("0");
            orgAll.setSortNo(-2);
            orgModelList.add(orgAll);

            // 未分组
            SysOrgModel orgNull = new SysOrgModel();
            orgNull.setId(ORG_NULL);
            orgNull.setOrgCode("-1");
            orgNull.setOrgName("未分组");
            orgNull.setOrgType("-1");
            orgNull.setParentId("0");
            orgNull.setSortNo(-1);
            orgModelList.add(orgNull);
        }

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey("sortNo");
        treeNodeConfig.setNameKey("orgName");
        // 最大递归深度 最多支持4层菜单
        treeNodeConfig.setDeep(3);

        //转换器
        List<Tree<String>> treeNodes = TreeUtil.build(orgModelList, parentId, treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(treeNode.getSortNo());
                    tree.setName(treeNode.getOrgName());
                    // 扩展属性 ...
                    // 不是外链 则处理组件
                    tree.putExtra("orgCode", treeNode.getOrgCode());
                    tree.putExtra("orgType", treeNode.getOrgType());
                    tree.putExtra("version", treeNode.getVersion());
                    tree.putExtra("tenantId", treeNode.getTenantId());
                });

        Set<String> parentIds = Sets.newHashSet();
        for (Tree<String> treeNode : treeNodes) {
            parentIds.add(treeNode.getId());
        }

        // 数据排查是否存在下级
        List<HasChildren> hasChildrenList = IService.hasChildren(parentIds);
        if(CollUtil.isNotEmpty(hasChildrenList)){
            Map<String, Boolean> tmp = Maps.newHashMap();
            for (HasChildren hasChildren : hasChildrenList) {
                if(hasChildren.getCount() != null && hasChildren.getCount() > 0){
                    tmp.put(hasChildren.getParentId(), true);
                }
            }

            for (Tree<String> treeNode : treeNodes) {
                Boolean tmpFlag = tmp.get(treeNode.getId());
                if(tmpFlag != null && tmpFlag){
                    treeNode.putExtra("hasChildren", true);
                }
            }
        }

        return ResultVo.success(treeNodes);
    }


    /**
     * 获得组织树 懒加载
     * @return ResultVo
     */
    @ApiOperation(value = "获得组织树 懒加载", notes = "获得组织树 懒加载")
    @Override
    public ResultVo<?> findTreeLazy(String parentId) {

        QueryBuilder<SysOrg> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysOrg> wrapper = queryBuilder.build();
        wrapper.eq(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentId);

        // 获得用户 对应菜单
        List<SysOrg> dataList = IService.findList(wrapper);
        List<SysOrgModel> orgModelList = WrapperUtil.transformInstance(dataList, SysOrgModel.class);


        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey("sortNo");
        treeNodeConfig.setNameKey("orgName");
        // 最大递归深度 最多支持4层菜单
        treeNodeConfig.setDeep(3);

        //转换器
        List<Tree<String>> treeNodes = TreeUtil.build(orgModelList, parentId, treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(treeNode.getSortNo());
                    tree.setName(treeNode.getOrgName());
                    // 扩展属性 ...
                    // 不是外链 则处理组件
                    tree.putExtra("orgCode", treeNode.getOrgCode());
                    tree.putExtra("orgType", treeNode.getOrgType());
                    tree.putExtra("version", treeNode.getVersion());
                    tree.putExtra("tenantId", treeNode.getTenantId());
                });

        Set<String> parentIds = Sets.newHashSet();
        for (Tree<String> treeNode : treeNodes) {
            parentIds.add(treeNode.getId());
        }

        // 数据排查是否存在下级
        List<HasChildren> hasChildrenList = IService.hasChildren(parentIds);
        if(CollUtil.isNotEmpty(hasChildrenList)){
            Map<String, Boolean> tmp = Maps.newHashMap();
            for (HasChildren hasChildren : hasChildrenList) {
                if(hasChildren.getCount() != null && hasChildren.getCount() > 0){
                    tmp.put(hasChildren.getParentId(), true);
                }
            }

            for (Tree<String> treeNode : treeNodes) {
                Boolean tmpFlag = tmp.get(treeNode.getId());
                if(tmpFlag != null && tmpFlag){
                    treeNode.putExtra("hasChildren", true);
                }
            }
        }

        return ResultVo.success(treeNodes);
    }

    /**
     * 获得组织树树
     * @return ResultVo
     */
    @ApiOperation(value = "获得组织树", notes = "获得组织树")
    @Override
    public ResultVo<?> findGridTree(String parentId) {

        QueryBuilder<SysOrg> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysOrg> wrapper = queryBuilder.build();
        wrapper.eq(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentId);

        // 获得用户 对应菜单
        List<SysOrg> dataList = IService.findList(wrapper);
        List<SysOrgModel> orgModelList = WrapperUtil.transformInstance(dataList, SysOrgModel.class);


        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey("sortNo");
        treeNodeConfig.setNameKey("orgName");
        // 最大递归深度 最多支持4层菜单
        treeNodeConfig.setDeep(3);

        //转换器
        List<Tree<String>> treeNodes = TreeUtil.build(orgModelList, parentId, treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(treeNode.getSortNo());
                    tree.setName(treeNode.getOrgName());
                    // 扩展属性 ...
                    // 不是外链 则处理组件
                    tree.putExtra("orgCode", treeNode.getOrgCode());
                    tree.putExtra("orgType", treeNode.getOrgType());
                    tree.putExtra("version", treeNode.getVersion());
                    tree.putExtra("tenantId", treeNode.getTenantId());
                });

        return ResultVo.success(treeNodes);
    }

    // ==============

    /**
    * 组织机构 查一条
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "获得单条组织机构", notes = "获得单条组织机构 - ID")
    @RequiresPermissions("system_org_select")
    @Override
    public ResultVo<SysOrgModel> get(SysOrgModel model) {
        // 如果系统内部调用 则直接查数据库
        if(model != null && model.getIzApi() != null && model.getIzApi()){
            model = IService.get(model);
        }
        return ResultVo.success(model);
    }

    /**
     * 获得组织树树
     * @return ResultVo
     */
    @ApiOperation(value = "获得菜单树", notes = "获得菜单树")
    @RequiresPermissions("system_org_select")
    @Override
    public ResultVo<?> findTree(HttpServletRequest request) {

        QueryBuilder<SysOrg> queryBuilder = new WebQueryBuilder<>(SysOrg.class,
                request.getParameterMap());


        // 获得用户 对应菜单
        List<SysOrg> dataList = IService.findList(queryBuilder.build());

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey("sortNo");
        treeNodeConfig.setNameKey("orgName");
        // 最大递归深度 最多支持4层菜单
        treeNodeConfig.setDeep(3);

        //转换器
        List<Tree<String>> treeNodes = TreeUtil.build(dataList, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(treeNode.getSortNo());
                    tree.setName(treeNode.getOrgName());
                    // 扩展属性 ...
                    // 不是外链 则处理组件
                    tree.putExtra("orgCode", treeNode.getOrgCode());
                    tree.putExtra("orgType", treeNode.getOrgType());
                    tree.putExtra("version", treeNode.getVersion());
                    tree.putExtra("tenantId", treeNode.getTenantId());
                });

        return ResultVo.success(treeNodes);
    }

    /**
    * 组织机构 新增
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "新增组织机构数据", notes = "新增组织机构数据")
    @RequiresPermissions("system_org_insert")
    @EnableLog
    @Override
    public ResultVo<?> insert(SysOrgModel model) {
        // 演示模式 不允许操作
        //super.demoError();

        // 调用新增方法
        IService.insert(model);
        return ResultVo.success("新增组织机构成功");
    }

    /**
    * 组织机构 修改
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "修改组织机构数据", notes = "修改组织机构数据")
    @RequiresPermissions("system_org_update")
    @EnableLog
    @Override
    public ResultVo<?> update(SysOrgModel model) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改组织机构成功");
    }


    /**
    * 组织机构 删除
    * @param id ID
    * @return ResultVo
    */
    @ApiOperation(value = "删除组织机构数据", notes = "删除组织机构数据")
    @RequiresPermissions("system_org_update")
    @EnableLog
    @Override
    public ResultVo<?> del(String id){
        // 演示模式 不允许操作
        super.demoError();

        IService.delete(id);
        return ResultVo.success("删除组织机构成功");
    }

    /**
    * 组织机构 批量删除
    * @param ids ID 数组
    * @return ResultVo
    */
    @ApiOperation(value = "批量删除组织机构数据", notes = "批量删除组织机构数据")
    @RequiresPermissions("system_org_update")
    @EnableLog
    @Override
    public ResultVo<?> delAll(String[] ids){
        // 演示模式 不允许操作
        super.demoError();

        IService.deleteAll(ids);
        return ResultVo.success("批量删除组织机构成功");
    }


    /**
    * 组织机构 Excel 导出
    * @param request request
    * @param response response
    * @return ResultVo
    */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @RequiresPermissions("system_org_export")
    @EnableLog
    @Override
    public ResultVo<?> exportExcel(HttpServletRequest request, HttpServletResponse response) {
        QueryBuilder<SysOrg> queryBuilder = new WebQueryBuilder<>(SysOrg.class, request.getParameterMap());
        return super.excelExport(SysOrgRestApi.TITLE, queryBuilder.build(), response);
    }

    /**
    * 组织机构 Excel 导入
    * @param request 文件流 request
    * @return ResultVo
    */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @RequiresPermissions("system_org_import")
    @EnableLog
    @Override
    public ResultVo<?> excelImport(MultipartHttpServletRequest request) {
        return super.excelImport(request);
    }

    /**
    * 组织机构 Excel 下载导入模版
    * @param response response
    * @return ResultVo
    */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissions("system_org_import")
    @Override
    public ResultVo<?> importTemplate(HttpServletResponse response) {
        return super.importTemplate(SysOrgRestApi.TITLE, response);
    }

}
