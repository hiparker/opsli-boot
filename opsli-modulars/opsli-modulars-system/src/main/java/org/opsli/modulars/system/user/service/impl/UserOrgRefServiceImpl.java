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
package org.opsli.modulars.system.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.api.wrapper.system.org.SysOrgModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.api.wrapper.system.user.UserOrgRefWebModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.FieldUtil;
import org.opsli.common.utils.ListDistinctUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.utils.OrgUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.org.entity.SysOrg;
import org.opsli.modulars.system.org.service.ISysOrgService;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.entity.SysUserOrgRef;
import org.opsli.modulars.system.user.mapper.UserOrgRefMapper;
import org.opsli.modulars.system.user.service.IUserOrgRefService;
import org.opsli.modulars.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 用户-组织 Service Impl
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Service
public class UserOrgRefServiceImpl extends ServiceImpl<UserOrgRefMapper, SysUserOrgRef> implements IUserOrgRefService {

    /** 分割符 */
    private static final String DELIMITER = ",";

    /** 父节点ID */
    private static final String PARENT_ID = "0";

    @Autowired(required = false)
    private UserOrgRefMapper mapper;

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ISysOrgService iSysOrgService;

    @Override
    public List<UserOrgRefModel> findListByUserId(String userId) {
        if(StrUtil.isEmpty(userId)){
            return ListUtil.empty();
        }

        UserModel userModel = iUserService.get(userId);
        if(userModel == null){
            return ListUtil.empty();
        }

        String userIdField = "user_id";
        QueryWrapper<SysUserOrgRef> wrapper = new QueryWrapper<>();
        wrapper.eq(userIdField, userId);

        List<SysUserOrgRef> orgRefList = super.list(wrapper);

        if(CollUtil.isEmpty(orgRefList)){

            // 判断是否是超级管理员 如果是超级管理员 则默认享有全部权限
            if(StringUtils.equals(UserUtil.SUPER_ADMIN, userModel.getUsername())){
                QueryWrapper<SysOrg> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(
                        FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), PARENT_ID
                );
                List<SysOrg> orgList = iSysOrgService.findList(queryWrapper);
                List<SysOrgModel> sysOrgModels = WrapperUtil.transformInstance(orgList, SysOrgModel.class);
                for (SysOrgModel orgModel : sysOrgModels) {
                    SysUserOrgRef orgRef = this.createOrgRef(userId, orgModel, DictType.NO_YES_NO.getValue());
                    orgRefList.add(orgRef);
                }
            }
        }

        return WrapperUtil.transformInstance(
                orgRefList, UserOrgRefModel.class
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setOrg(UserOrgRefWebModel model) {
        // 非法验证 组织不可为空
        if(model == null){
            throw new ServiceException(SystemMsg.EXCEPTION_ORG_NOT_NULL);
        }

        // 删除已有组织
        String userIdField = "user_id";
        QueryWrapper<SysUserOrgRef> wrapper = new QueryWrapper<>();
        wrapper.eq(userIdField, model.getUserId());
        boolean removeFlag = super.remove(wrapper);

        // 设置组织
        List<SysUserOrgRef> orgRefList = Lists.newArrayList();

        SysOrgModel defModel = model.getDefModel();
        if(defModel != null){
            SysUserOrgRef orgRef = createOrgRef(
                    model.getUserId(), defModel, DictType.NO_YES_YES.getValue());
            orgRefList.add(orgRef);
        }
        List<SysOrgModel> orgModelList = model.getOrgModelList();
        if(!CollUtil.isEmpty(orgModelList)){
            for (SysOrgModel orgModel : orgModelList) {
                SysUserOrgRef orgRef = createOrgRef(
                        model.getUserId(), orgModel, DictType.NO_YES_NO.getValue());
                orgRefList.add(orgRef);
            }
        }

        boolean izExistOrg = false;

        if(!CollUtil.isEmpty(orgRefList)){
            // 去重
            orgRefList = ListDistinctUtil.distinct(
                    orgRefList, Comparator.comparing(SysUserOrgRef::getId));

            // 批量保存
            izExistOrg = super.saveBatch(orgRefList);
        }

        // 修改用户组织状态
        this.updateUserOrgFlag(model.getUserId(), izExistOrg);

        // 清空缓存
        if(removeFlag){
            // 刷新用户缓存
            this.clearCache(Collections.singletonList(model.getUserId()));
        }

        return true;
    }


    /**
     * 修改用户组织状态
     * @param userId 用户ID
     * @param flag 状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserOrgFlag(String userId, boolean flag) {
        String flagVal = DictType.NO_YES_NO.getValue();
        if(flag){
            flagVal = DictType.NO_YES_YES.getValue();
        }
        // 修改用户组织状态
        UpdateWrapper<SysUser> updateUserWrapper = new UpdateWrapper<>();
        updateUserWrapper.set("iz_exist_org", flagVal);
        updateUserWrapper.eq(
                FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ID), userId);
        iUserService.update(updateUserWrapper);
    }

    /**
     * 生成Org关联对象
     * @param userId 用户ID
     * @param orgModel 组织
     * @return ref
     */
    private SysUserOrgRef createOrgRef(String userId, SysOrgModel orgModel, String izDef) {
        String orgGroup =
                StrUtil.appendIfMissing(
                        orgModel.getParentIds(), DELIMITER) +
                        orgModel.getId();

        SysUserOrgRef orgRef = new SysUserOrgRef();
        orgRef.setUserId(userId);
        orgRef.setOrgId(orgModel.getId());
        orgRef.setOrgIds(orgGroup);
        orgRef.setIzDef(izDef);
        return orgRef;
    }

    // ============

    /**
     * 清除缓存
     * @param userIds 用户ID 集合
     */
    private void clearCache(List<String> userIds){
        // 清空缓存
        if(CollUtil.isNotEmpty(userIds)){
            int cacheCount = 0;
            for (String userId : userIds) {
                cacheCount++;
                boolean tmp = OrgUtil.refreshOrg(userId);
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


