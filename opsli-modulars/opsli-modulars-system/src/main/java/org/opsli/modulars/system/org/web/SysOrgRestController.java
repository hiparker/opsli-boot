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
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.org.SysOrgRestApi;
import org.opsli.api.wrapper.system.org.SysOrgModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.common.annotation.RequiresPermissionsCus;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.utils.HumpUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.base.entity.HasChildren;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.core.utils.TreeBuildUtil;
import org.opsli.modulars.system.org.entity.SysOrg;
import org.opsli.modulars.system.org.service.ISysOrgService;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
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
@Api(tags = SysOrgRestApi.TITLE)
@Slf4j
@ApiRestController("/sys/org")
public class SysOrgRestController extends BaseRestController<SysOrg, SysOrgModel, ISysOrgService>
    implements SysOrgRestApi {

    /** 父节点ID */
    private static final String PARENT_ID = "0";
    /** 显示全部 */
    public static final String ORG_ALL = "all";
    /** 未分组 */
    public static final String ORG_NULL = "org_null";
    /** 排序字段 */
    private static final String SORT_FIELD = "sortNo";
    /** 是否包含子集 */
    private static final String HAS_CHILDREN = "hasChildren";

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
        List<SysOrgModel> orgModelList = WrapperUtil.transformInstance(dataList, modelClazz);

        // 处理展示节点
        this.handleShowNodes(parentId, orgModelList);

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey(SORT_FIELD);
        // 最大递归深度 最多支持4层菜单
        treeNodeConfig.setDeep(3);

        //转换器
        List<Tree<Object>> treeNodes = TreeBuildUtil.INSTANCE.build(orgModelList, parentId, treeNodeConfig);

        // 处理是否包含子集
        this.handleTreeHasChildren(treeNodes);

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
        List<SysOrgModel> orgModelList = WrapperUtil.transformInstance(dataList, modelClazz);


        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey(SORT_FIELD);
        // 最大递归深度 最多支持4层菜单
        treeNodeConfig.setDeep(3);

        //转换器
        List<Tree<Object>> treeNodes = TreeBuildUtil.INSTANCE.build(orgModelList, parentId, treeNodeConfig);

        // 处理是否包含子集
        this.handleTreeHasChildren(treeNodes);

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
        List<SysOrgModel> orgModelList = WrapperUtil.transformInstance(dataList, modelClazz);


        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey(SORT_FIELD);
        // 最大递归深度 最多支持4层菜单
        treeNodeConfig.setDeep(3);

        //转换器
        List<Tree<Object>> treeNodes = TreeBuildUtil.INSTANCE.build(orgModelList, parentId, treeNodeConfig);

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

        QueryBuilder<SysOrg> queryBuilder = new WebQueryBuilder<>(entityClazz,
                request.getParameterMap());


        // 获得用户 对应菜单
        List<SysOrg> dataList = IService.findList(queryBuilder.build());

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey(SORT_FIELD);
        // 最大递归深度 最多支持4层菜单
        treeNodeConfig.setDeep(3);

        //转换器
        List<Tree<Object>> treeNodes = TreeBuildUtil.INSTANCE.build(dataList, treeNodeConfig);

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
    public ResultVo<?> delAll(String ids){
        // 演示模式 不允许操作
        super.demoError();

        String[] idArray = Convert.toStrArray(ids);
        IService.deleteAll(idArray);

        return ResultVo.success("批量删除组织机构成功");
    }


    /**
    * 组织机构 Excel 导出
    * @param request request
    * @param response response
    * @return ResultVo
    */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @RequiresPermissionsCus("system_org_export")
    @EnableLog
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "exportExcel");
        QueryBuilder<SysOrg> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        super.excelExport(SysOrgRestApi.SUB_TITLE, queryBuilder.build(), response, method);
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
    public ResultVo<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }

    /**
    * 组织机构 Excel 下载导入模版
    * @param response response
    * @return ResultVo
    */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissionsCus("system_org_import")
    @Override
    public void importTemplate(HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "importTemplate");
        super.importTemplate(SysOrgRestApi.SUB_TITLE, response, method);
    }

    // ==============================


    /**
     * 处理展示节点
     * @param parentId 父节点ID
     * @param orgModelList 组织集合
     */
    private void handleShowNodes(String parentId, List<SysOrgModel> orgModelList) {
        // 0 为初始值
        if(PARENT_ID.equals(parentId)){
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
    }

    /**
     * 处理是否包含子集
     * @param treeNodes 树节点
     */
    private void handleTreeHasChildren(List<Tree<Object>> treeNodes) {
        if(CollUtil.isEmpty(treeNodes)){
            return;
        }

        Set<String> parentIds = Sets.newHashSet();
        for (Tree<Object> treeNode : treeNodes) {
            parentIds.add(Convert.toStr(treeNode.getId()));
        }

        // 数据排查是否存在下级
        List<HasChildren> hasChildrenList = IService.hasChildren(parentIds);
        if (CollUtil.isNotEmpty(hasChildrenList)) {
            Map<String, Boolean> tmp = Maps.newHashMap();
            for (HasChildren hasChildren : hasChildrenList) {
                if (hasChildren.getCount() != null && hasChildren.getCount() > 0) {
                    tmp.put(hasChildren.getParentId(), true);
                }
            }

            for (Tree<Object> treeNode : treeNodes) {
                Boolean tmpFlag = tmp.get(Convert.toStr(treeNode.getId()));
                if (tmpFlag != null && tmpFlag) {
                    treeNode.putExtra(HAS_CHILDREN, true);
                }
            }
        }
    }

}
