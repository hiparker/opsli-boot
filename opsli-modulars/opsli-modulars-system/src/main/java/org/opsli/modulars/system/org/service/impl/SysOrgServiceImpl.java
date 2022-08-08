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
package org.opsli.modulars.system.org.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.org.SysOrgModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.FieldUtil;
import org.opsli.common.utils.ListDistinctUtil;
import org.opsli.core.base.entity.HasChildren;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.chain.QueryTenantHandler;
import org.opsli.core.utils.TenantUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.org.entity.SysOrg;
import org.opsli.modulars.system.org.mapper.SysOrgMapper;
import org.opsli.modulars.system.org.service.ISysOrgService;
import org.opsli.modulars.system.user.service.IUserOrgRefService;
import org.opsli.modulars.system.user.service.IUserRoleRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * 组织机构 Service Impl
 *
 * @author Parker
 * @date 2021-02-07 18:24:38
 */
@Service
public class SysOrgServiceImpl extends CrudServiceImpl<SysOrgMapper, SysOrg, SysOrgModel>
        implements ISysOrgService {

    /** 顶级ID */
    private static final String TOP_PARENT_ID = "0";
    /** 分割符 */
    private static final String DELIMITER = ",";

    @Autowired(required = false)
    private SysOrgMapper mapper;
    @Autowired
    private IUserRoleRefService iUserRoleRefService;
    @Autowired
    private IUserOrgRefService iUserOrgRefService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysOrgModel insert(SysOrgModel model) {
        if(model == null){
            return null;
        }

        model.setParentIds(null);

        // 唯一验证
        boolean verificationByCode = this.uniqueVerificationByCode(model);
        if(!verificationByCode){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ORG_UNIQUE);
        }

        // 如果上级ID 为空 则默认为 0
        if(StringUtils.isEmpty(model.getParentId()) || TOP_PARENT_ID.equals(model.getParentId())){
            model.setParentId(TOP_PARENT_ID);
            model.setParentIds(TOP_PARENT_ID);
        }

        // 如果上级ID不为空 且 不等于顶级ID
        if(StringUtils.isNotEmpty(model.getParentId()) &&
                !TOP_PARENT_ID.equals(model.getParentId())
        ){

            SysOrgModel sysOrgModel = super.get(model.getParentId());

            // 下级沿用上级租户ID
            model.setTenantId(sysOrgModel.getTenantId());
            // 下级沿用上级ParentIds
            model.setParentIds(
                    StrUtil.appendIfMissing(
                            sysOrgModel.getParentIds(), DELIMITER) +
                            sysOrgModel.getId());
        }

        // 清理缓存 清除当前租户下 所有角色的数据范围为 全部数据的 用户缓存
        // 如果是超级管理员体系 下的用户 还需要清空超级管理员的 缓存
        if(TenantUtil.SUPER_ADMIN_TENANT_ID.equals(UserUtil.getTenantId())){
            UserModel superAdmin = UserUtil.getUserByUserName(UserUtil.SUPER_ADMIN);
            if(null != superAdmin){
                // 清除缓存
                this.clearCache(Collections.singletonList(superAdmin.getId()));
            }
        }

        // 更新 orgIds 字段
        SysOrgModel insertModel = super.insert(model);
        if(null != insertModel){
            String orgIds = StrUtil.appendIfMissing(
                    insertModel.getParentIds(), DELIMITER) +
                    insertModel.getId();
            UpdateWrapper<SysOrg> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq(
                    FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID), insertModel.getId());
            updateWrapper.set(
                    FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ORG_GROUP), orgIds);
            boolean updateFlag = this.update(updateWrapper);
            if(!updateFlag){
                // 手动触发回滚效果
                throw new RuntimeException("更新OrgIds失败");
            }

            // 获得当前租户下 数据权限为全部数据的 用户ID 集合
            List<String> userIdList = Lists.newArrayList();
            List<String> userIdListByTenantId =
                    iUserRoleRefService.getUserIdListByTenantIdAndAllData(UserUtil.getTenantId());
            // 获得当前租户下 ordIds 分组后 所有用户ID 集合
            List<String> userIdListByOrgIds = iUserOrgRefService.getUserIdListByOrgIds(orgIds);
            // 组合ID
            userIdList.addAll(userIdListByTenantId);
            userIdList.addAll(userIdListByOrgIds);

            // 清除缓存
            this.clearCache(userIdList);
        }

        return insertModel;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysOrgModel update(SysOrgModel model) {
        if(model == null){
            return null;
        }

        model.setParentIds(null);
        model.setOrgIds(null);

        // 唯一验证
        boolean verificationByCode = this.uniqueVerificationByCode(model);
        if(!verificationByCode){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_ORG_UNIQUE);
        }

        // 如果上级ID不为空 且 不等于顶级ID
        if(StringUtils.isNotEmpty(model.getParentId()) &&
                !TOP_PARENT_ID.equals(model.getParentId())
        ){

            SysOrgModel sysOrgModel = super.get(model.getParentId());

            // 下级沿用上级租户ID
            model.setTenantId(sysOrgModel.getTenantId());
            // 下级沿用上级ParentIds
            model.setParentIds(
                    StrUtil.appendIfMissing(
                            sysOrgModel.getParentIds(), DELIMITER) +
                            sysOrgModel.getId());
            // 下级沿用上级OrgIds
            model.setOrgIds(
                    StrUtil.appendIfMissing(
                            model.getParentIds(), DELIMITER) +
                            model.getId());
        }

        SysOrgModel sysOrgModel = super.get(model);
        SysOrgModel updateRet = super.update(model);

        // 如果 TenantId 发生变化 则需要更改 下级数据 租户ID
        if(sysOrgModel != null && !sysOrgModel.getTenantId().equals(
                model.getTenantId())){
            // 如果有组织还在被引用 则不允许操作该组织
            this.validationUsedByDel(Collections.singletonList(sysOrgModel.getId()));

            // 如果没有被引用 则逐级修改
            this.updateTenantByParentId(sysOrgModel.getId(), model.getTenantId());
        }

        // 如果 parentId 发生变化 则需要更改 下级数据 租户ID
        if(sysOrgModel != null && !sysOrgModel.getParentId().equals(
                model.getParentId())){
            // 如果有组织还在被引用 则不允许操作该组织
            this.validationUsedByDel(Collections.singletonList(sysOrgModel.getId()));

            // 如果没有被引用 则逐级修改
            this.updateChildrenParentIdsByParentId(sysOrgModel.getId());
        }

        // 获得当前租户下 数据权限为全部数据的 用户ID 集合
        List<String> userIdList =
                iUserRoleRefService.getUserIdListByTenantIdAndAllData(UserUtil.getTenantId());
        // 获得当前租户下 ordIds 分组后 所有用户ID 集合
        List<String> userIdListByOrgIds = iUserOrgRefService.getUserIdListByOrgIds(updateRet.getOrgIds());
        // 组合ID
        userIdList.addAll(userIdListByOrgIds);

        // 清除缓存
        this.clearCache(userIdList);

        // 修改
        return updateRet;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        if(StringUtils.isEmpty(id)){
            return false;
        }

        // 如果有组织还在被引用 则不允许操作该组织
        this.validationUsedByDel(Collections.singletonList(id));

        // 先删除子数据
        this.deleteByParentId(id);

        return super.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAll(String[] ids) {
        if(ArrayUtils.isEmpty(ids)){
            return false;
        }

        // 如果有组织还在被引用 则不允许操作该组织
        this.validationUsedByDel(Convert.toList(String.class, ids));

        // 先删除子数据
        for (String id : ids) {
            this.deleteByParentId(id);
        }

        return super.deleteAll(ids);
    }

    /**
     * 逐级修改机构下租户
     * @param parentId 父级ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateTenantByParentId(String parentId, String tenantId) {
        QueryBuilder<SysOrg> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysOrg> queryWrapper = queryBuilder.build();
        queryWrapper.eq(
                FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentId);
        List<SysOrg> entityList = super.findList(queryWrapper);
        for (SysOrg sysOrg : entityList) {
            sysOrg.setTenantId(tenantId);
            super.updateById(sysOrg);
            // 逐级删除子数据
            this.updateTenantByParentId(sysOrg.getId(), tenantId);
        }
    }

    /**
     * 逐级修改机构下租户
     * @param parentId 父级ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateChildrenParentIdsByParentId(String parentId) {
        QueryBuilder<SysOrg> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysOrg> queryWrapper = queryBuilder.build();
        queryWrapper.eq(
                FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentId);
        List<SysOrg> entityList = super.findList(queryWrapper);
        for (SysOrg sysOrg : entityList) {
            SysOrgModel sysOrgModel = super.get(parentId);

            // 下级沿用上级ParentIds
            sysOrg.setParentIds(
                    StrUtil.appendIfMissing(
                            sysOrgModel.getParentIds(), DELIMITER) +
                            sysOrgModel.getId());

            // 下级沿用上级OrgIds
            sysOrg.setOrgIds(
                    StrUtil.appendIfMissing(
                            sysOrg.getParentIds(), DELIMITER) +
                            sysOrg.getId());

            super.updateById(sysOrg);
            // 逐级删除子数据
            this.updateChildrenParentIdsByParentId(sysOrg.getId());
        }
    }

    /**
     * 逐级删除子数据
     * @param parentId 父级ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByParentId(String parentId) {
        QueryBuilder<SysOrg> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysOrg> queryWrapper = queryBuilder.build();
        queryWrapper.eq(
                FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentId);
        List<SysOrg> entityList = super.findList(queryWrapper);
        for (SysOrg sysOrg : entityList) {
            super.delete(sysOrg.getId());
            // 逐级删除子数据
            this.deleteByParentId(sysOrg.getId());
        }
    }

    /**
     * 唯一验证
     * @param model 模型
     * @return Integer
     */
    @Transactional(readOnly = true)
    public boolean uniqueVerificationByCode(SysOrgModel model){
        if(model == null){
            return false;
        }
        QueryWrapper<SysOrg> wrapper = new QueryWrapper<>();
        wrapper.eq("org_code", model.getOrgCode());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        // 租户检测
        wrapper = new QueryTenantHandler().handler(this.getEntityClass(), wrapper);

        return super.count(wrapper) == 0;
    }


    /**
     * 是否有下级
     * @param parentIds 父级ID 集合
     * @return List
     */
    @Override
    @Transactional(readOnly = true)
    public List<HasChildren> hasChildren(Set<String> parentIds){
        if(CollUtil.isEmpty(parentIds)){
            return null;
        }

        // 添加 数据权限过滤器
        QueryWrapper<SysOrg> wrapper = super.addHandler(SysOrg.class);

        wrapper.in(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentIds)
                .eq(MyBatisConstants.FIELD_DELETE_LOGIC,  DictType.NO_YES_NO.getValue())
                .groupBy(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID));

        return mapper.hasChildren(wrapper);
    }


    /**
     * 删除验证该组织是否被引用
     * @param orgIdList 组织ID
     */
    public void validationUsedByDel(List<String> orgIdList){
        if(CollUtil.isEmpty(orgIdList)){
            return;
        }

        // 如果有租户还在被引用 则不允许删除该租户
        QueryWrapper<SysOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("u.deleted", DictType.NO_YES_NO.getValue());
        queryWrapper.in("ref.org_id",
                orgIdList
        );
        Integer count = mapper.hasUse(queryWrapper);
        if(count !=null && count > 0){
            // 该组织正在被其他用户绑定，无法操作
            throw new ServiceException(SystemMsg.EXCEPTION_ORG_USE);
        }
    }

    /**
     * 清除缓存
     * @param userIdList 用户ID 集合
     */
    private void clearCache(List<String> userIdList){
        if(CollUtil.isNotEmpty(userIdList)){
            // 去重
            List<String> distinctUserIdList = ListDistinctUtil.distinct(userIdList);

            int cacheCount = 0;
            for (String userId : distinctUserIdList) {
                cacheCount += 2;
                boolean tmp;
                // 清空当期用户缓存 组织
                tmp = UserUtil.refreshUserOrgs(userId);
                if(tmp){
                    cacheCount--;
                }
                tmp = UserUtil.refreshUserDefOrg(userId);
                if(tmp){
                    cacheCount--;
                }
            }
            // 判断删除状态
            if(cacheCount != 0){
                // 删除缓存失败
                throw new ServiceException(CoreMsg.CACHE_DEL_EXCEPTION);
            }
        }
    }
}
