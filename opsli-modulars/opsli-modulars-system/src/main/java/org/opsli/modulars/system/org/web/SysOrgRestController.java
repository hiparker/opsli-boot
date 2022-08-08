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
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.org.SysOrgRestApi;
import org.opsli.api.wrapper.system.org.SysOrgModel;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.FieldUtil;
import org.opsli.common.utils.ListDistinctUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.log.annotation.OperateLogger;
import org.opsli.core.log.enums.ModuleEnum;
import org.opsli.core.log.enums.OperationTypeEnum;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.utils.OrgUtil;
import org.opsli.core.utils.TenantUtil;
import org.opsli.core.utils.TreeBuildUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.org.entity.SysOrg;
import org.opsli.modulars.system.org.service.ISysOrgService;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 组织机构 Controller
 *
 * @author Parker
 * @date 2021-02-07 18:24:38
 */
@Api(tags = SysOrgRestApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/system/org")
public class SysOrgRestController extends BaseRestController<SysOrg, SysOrgModel, ISysOrgService>
    implements SysOrgRestApi {

    /** 虚拟总节点 ID */
    private static final String VIRTUAL_TOTAL_NODE = "-1";
    /** 父节点ID */
    private static final String PARENT_ID = "0";
    /** 排序字段 */
    private static final String SORT_FIELD = "sortNo";
    /** 分割符 */
    private static final String DELIMITER = ",";

    /**
     * 获得当前用户下 组织
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得当前用户下 组织", notes = "获得当前用户下 组织")
    @Override
    public ResultWrapper<?> findTreeByDefWithUserToLike() {
        // 生成 全部/未分组
        String parentId = PARENT_ID;
        List<SysOrgModel> orgModelList = OrgUtil.createDefShowNodes(parentId, Lists.newArrayList());

        QueryBuilder<SysOrg> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysOrg> wrapper = queryBuilder.build();
        List<UserOrgRefModel> orgListByUserId = UserUtil.getOrgByCurrUser();
        if(!CollUtil.isEmpty(orgListByUserId)){
            List<String> parentIdList = Lists.newArrayListWithCapacity(orgListByUserId.size());

            // 处理ParentId数据
            for (UserOrgRefModel userOrgRefModel : orgListByUserId) {
                String orgId = userOrgRefModel.getOrgId();
                String orgIds = userOrgRefModel.getOrgIds();
                // 减掉 结尾自身 orgId  得到  org表中 parentIds
                String parentIds =
                        StrUtil.replace(orgIds, StrUtil.prependIfMissing(orgId, DELIMITER), "");
                parentIdList.add(parentIds);
            }

            // 去重
            parentIdList = ListDistinctUtil.distinct(parentIdList);

            List<String> finalParentIdList = parentIdList;
            wrapper.and(wra -> {
                // 增加右模糊 查询条件
                for (int i = 0; i < finalParentIdList.size(); i++) {
                    // 右模糊匹配
                    wra.likeRight(
                            FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_IDS),
                            finalParentIdList.get(i));

                    if(i < finalParentIdList.size() - 1){
                        wra.or();
                    }
                }
            });

            // 获得组织
            List<SysOrg> dataList = IService.findList(wrapper);
            if(CollUtil.isNotEmpty(dataList)){
                for (SysOrg sysOrg : dataList) {
                    // 如果父级ID 与 当前检索父级ID 一致 则默认初始化ID为主ID
                    if(!CollUtil.contains(parentIdList, sysOrg.getParentIds())){
                        continue;
                    }

                    sysOrg.setParentId(parentId);
                }
                orgModelList.addAll(
                        WrapperUtil.transformInstance(dataList, IService.getModelClass())
                );
            }
        }

        // 处理组织树
        return handleOrgTree(parentId, orgModelList, false);
    }

    /**
     * 获得组织树 懒加载
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得组织树 懒加载", notes = "获得组织树 懒加载")
    @Override
    public ResultWrapper<?> findTreeLazy(String parentId, String id) {
        List<SysOrgModel> orgModelList;
        if(StringUtils.isEmpty(parentId)){
            orgModelList = Lists.newArrayList();
            // 生成根节点组织
            SysOrgModel model = getGenOrgModel();
            parentId = model.getParentId();
            orgModelList.add(model);
        }else{
            QueryBuilder<SysOrg> queryBuilder = new GenQueryBuilder<>();
            QueryWrapper<SysOrg> wrapper = queryBuilder.build();
            wrapper.eq(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentId);

            // 如果传入ID 则不包含自身
            if(StringUtils.isNotEmpty(id)){
                wrapper.notIn(
                        FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID), id);

            }

            // 获得组织
            List<SysOrg> dataList = IService.findList(wrapper);
            if(CollUtil.isEmpty(dataList)){
                Set<String> genOrgIdSet = new HashSet<>();
                List<UserOrgRefModel> orgListByUserId = UserUtil.getOrgByCurrUser();
                for (UserOrgRefModel userOrgRefModel : orgListByUserId) {
                    List<String> orgIdList = StrUtil.split(userOrgRefModel.getOrgIds(), ',');
                    if(CollUtil.isEmpty(orgIdList)){
                        continue;
                    }
                    // 只取最后一位
                    genOrgIdSet.add(orgIdList.get(orgIdList.size()-1));
                }


                QueryWrapper<SysOrg> wrapperByEmpty = queryBuilder.build();
                wrapperByEmpty.in(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID), genOrgIdSet);
                // 如果传入ID 则不包含自身
                if(StringUtils.isNotEmpty(id)){
                    wrapperByEmpty.notIn(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID), id);
                }

                // 获得组织
                dataList = IService.findList(wrapperByEmpty);
            }

            orgModelList = WrapperUtil.transformInstance(dataList, IService.getModelClass());
        }

        // 处理组织树
        return handleOrgTree(parentId, orgModelList, true);
    }

    /**
     * 获得组织树
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得组织树", notes = "获得组织树")
    @Override
    public ResultWrapper<?> findTreeByDef(boolean isGen, String id) {
        List<SysOrgModel> orgModelList = Lists.newArrayList();
        String parentId = PARENT_ID;
        if(isGen){
            // 生成根节点组织
            SysOrgModel model = getGenOrgModel();
            parentId = model.getParentId();
            orgModelList.add(model);
        }

        QueryBuilder<SysOrg> queryBuilder = new GenQueryBuilder<>();


        // 获得最外层节点
        Set<String> genOrgIdSet = new HashSet<>();
        List<UserOrgRefModel> orgListByUserId = UserUtil.getOrgByCurrUser();
        for (UserOrgRefModel userOrgRefModel : orgListByUserId) {
            List<String> orgIdList = StrUtil.split(userOrgRefModel.getOrgIds(), ',');
            if(CollUtil.isEmpty(orgIdList)){
                continue;
            }
            // 只取最后一位
            genOrgIdSet.add(orgIdList.get(orgIdList.size()-1));
        }

        QueryWrapper<SysOrg> wrapperByEmpty = queryBuilder.build();
        if(CollUtil.isNotEmpty(genOrgIdSet)){
            wrapperByEmpty.in(
                    FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID), genOrgIdSet);
        }        // 如果传入ID 则不包含自身
        if(StringUtils.isNotEmpty(id)){
            wrapperByEmpty.notIn(
                    FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID), id);
        }

        // 获得父节点组织
        List<SysOrg> dataList = IService.findList(wrapperByEmpty);
        if(CollUtil.isEmpty(dataList)){
            dataList = Lists.newArrayList();
        }
        for (SysOrg sysOrg : dataList) {
            // 设置默认父节点
            sysOrg.setParentId(TreeBuildUtil.DEF_PARENT_ID);
        }


        QueryWrapper<SysOrg> wrapper = queryBuilder.build();
        // 如果传入ID 则不包含自身
        if(StringUtils.isNotEmpty(id)){
            wrapper.notIn(
                    FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID), id);

        }
        // 排除父节点ID
        if(CollUtil.isNotEmpty(genOrgIdSet)){
            wrapperByEmpty.in(
                    FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID), genOrgIdSet);
        }

        // 获得子节点组织
        List<SysOrg> childList = IService.findList(wrapper);
        if(CollUtil.isNotEmpty(childList)){
            dataList.addAll(childList);
        }

        if(CollUtil.isNotEmpty(dataList)){
            orgModelList.addAll(
                    WrapperUtil.transformInstance(dataList, IService.getModelClass())
            );
        }

        // 处理组织树
        return handleOrgTree(parentId, orgModelList, false);
    }




    // ==============

    /**
    * 组织机构 查一条
    * @param model 模型
    * @return ResultWrapper
    */
    @ApiOperation(value = "获得单条组织机构", notes = "获得单条组织机构 - ID")
    @PreAuthorize("hasAuthority('system_org_select')")
    @Override
    public ResultWrapper<SysOrgModel> get(SysOrgModel model) {
        if(model != null){
            if(StringUtils.equals(PARENT_ID, model.getId())){
                // 生成根节点组织
                model = getGenOrgModel();
            }else{
                model = IService.get(model);
            }
        }

        return ResultWrapper.getSuccessResultWrapper(model);
    }

    /**
    * 组织机构 新增
    * @param model 模型
    * @return ResultWrapper
    */
    @ApiOperation(value = "新增组织机构数据", notes = "新增组织机构数据")
    @PreAuthorize("hasAuthority('system_org_insert')")
    @OperateLogger(description = "新增组织机构数据",
            module = ModuleEnum.MODULE_ORG, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> insert(SysOrgModel model) {

        // 如果新增的是 根节点数据 则需要验证权限
        if(null != model && TreeBuildUtil.DEF_PARENT_ID.equals(model.getParentId())){
            UserModel currUser = UserUtil.getUser();

            // 如果不是超级管理员 和 租户管理员
            if(!StringUtils.equals(UserUtil.SUPER_ADMIN, currUser.getUsername())  &&
                    !TenantUtil.SUPER_ADMIN_TENANT_ID.equals(currUser.getTenantId()) ){

                RoleModel defRoleByUserId = UserUtil.getUserDefRoleByUserId(currUser.getId());
                if(null == defRoleByUserId ||
                        StringUtils.isEmpty(defRoleByUserId.getDataScope()) ||
                        !DictType.DATA_SCOPE_ALL.getValue().equals(defRoleByUserId.getDataScope())){
                    // 无组织机构新增权限
                    throw new ServiceException(SystemMsg.EXCEPTION_ORG_NOT_PERMISSION);
                }
            }
        }

        // 调用新增方法
        IService.insert(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("新增组织机构成功");
    }

    /**
    * 组织机构 修改
    * @param model 模型
    * @return ResultWrapper
    */
    @ApiOperation(value = "修改组织机构数据", notes = "修改组织机构数据")
    @PreAuthorize("hasAuthority('system_org_update')")
    @OperateLogger(description = "修改组织机构数据",
            module = ModuleEnum.MODULE_ORG, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> update(SysOrgModel model) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.update(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("修改组织机构成功");
    }


    /**
    * 组织机构 删除
    * @param id ID
    * @return ResultWrapper
    */
    @ApiOperation(value = "删除组织机构数据", notes = "删除组织机构数据")
    @PreAuthorize("hasAuthority('system_org_delete')")
    @OperateLogger(description = "删除组织机构数据",
            module = ModuleEnum.MODULE_ORG, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> del(String id){
        // 演示模式 不允许操作
        super.demoError();

        IService.delete(id);
        return ResultWrapper.getSuccessResultWrapperByMsg("删除组织机构成功");
    }

    /**
    * 组织机构 批量删除
    * @param ids ID 数组
    * @return ResultWrapper
    */
    @ApiOperation(value = "批量删除组织机构数据", notes = "批量删除组织机构数据")
    @PreAuthorize("hasAuthority('system_org_delete')")
    @OperateLogger(description = "批量删除组织机构数据",
            module = ModuleEnum.MODULE_ORG, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> delAll(String ids){
        // 演示模式 不允许操作
        super.demoError();

        String[] idArray = Convert.toStrArray(ids);
        IService.deleteAll(idArray);

        return ResultWrapper.getSuccessResultWrapperByMsg("批量删除组织机构成功");
    }


    // ==============================

    /**
     * 生成根节点ID
     * @return SysOrgModel
     */
    private SysOrgModel getGenOrgModel() {
        SysOrgModel model = new SysOrgModel();
        model.setId(PARENT_ID);
        model.setOrgName("组织架构");
        model.setSortNo(-1);
        model.setParentId(VIRTUAL_TOTAL_NODE);
        return model;
    }



    /**
     * 处理组织树
     * @param parentId 父级ID
     * @param orgModelList 组织集合
     * @param izLazy 是否懒加载
     * @return ResultWrapper
     */
    private ResultWrapper<?> handleOrgTree(String parentId, List<SysOrgModel> orgModelList, boolean izLazy) {
        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey(SORT_FIELD);
        // 最大递归深度 最多支持4层
        //treeNodeConfig.setDeep(4);

        //转换器
        List<Tree<Object>> treeNodes;

        // 是否懒加载
        if(izLazy){
            //转换器
            treeNodes = TreeBuildUtil.INSTANCE.buildByLazy(orgModelList, treeNodeConfig);

            // 处理是否包含子集
            super.handleTreeHasChildren(treeNodes,
                    (parentIds)-> IService.hasChildren(parentIds));
        }else{
            //转换器
            treeNodes = TreeBuildUtil.INSTANCE.build(orgModelList, parentId, treeNodeConfig);

            Set<String> parentIdSet = Sets.newHashSet();
            for (SysOrgModel sysOrgModel : orgModelList) {
                parentIdSet.add(sysOrgModel.getParentId());
            }

            // 处理是否包含子集
            super.handleTreeHasChildren(treeNodes,
                    (parentIds)-> IService.hasChildren(parentIdSet));
        }

        return ResultWrapper.getSuccessResultWrapper(treeNodes);
    }

}
