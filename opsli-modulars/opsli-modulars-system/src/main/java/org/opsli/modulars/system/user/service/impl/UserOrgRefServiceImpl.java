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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.utils.OrgUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.user.entity.SysUserOrgRef;
import org.opsli.modulars.system.user.mapper.UserOrgRefMapper;
import org.opsli.modulars.system.user.service.IUserOrgRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.service
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:34
 * @Description: 角色 接口实现类
 */
@Service
public class UserOrgRefServiceImpl extends ServiceImpl<UserOrgRefMapper, SysUserOrgRef> implements IUserOrgRefService {

    @Autowired(required = false)
    private UserOrgRefMapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setOrg(UserOrgRefModel model) {
        // 非法验证 组织不可为空
        if(model == null){
            throw new ServiceException(SystemMsg.EXCEPTION_ORG_NOT_NULL);
        }

        // 删除已有组织
        String userIdField = "user_id";
        QueryWrapper<SysUserOrgRef> wrapper = new QueryWrapper<>();
        wrapper.eq(userIdField, model.getUserId());
        super.remove(wrapper);

        List<SysUserOrgRef> orgRefs = Lists.newArrayList();

        // 设置公司
        if(StringUtils.isNotEmpty(model.getCompanyId())){
            SysUserOrgRef tmp = new SysUserOrgRef();
            tmp.setUserId(model.getUserId());
            tmp.setOrgId(model.getCompanyId());
            tmp.setOrgType("1");
            orgRefs.add(tmp);
        }

        // 设置部门
        if(StringUtils.isNotEmpty(model.getDepartmentId())){
            SysUserOrgRef tmp = new SysUserOrgRef();
            tmp.setUserId(model.getUserId());
            tmp.setOrgId(model.getDepartmentId());
            tmp.setOrgType("2");
            orgRefs.add(tmp);
        }

        // 设置岗位
        if(StringUtils.isNotEmpty(model.getPostId())){
            SysUserOrgRef tmp = new SysUserOrgRef();
            tmp.setUserId(model.getUserId());
            tmp.setOrgId(model.getPostId());
            tmp.setOrgType("3");
            orgRefs.add(tmp);
        }

        boolean saveBatchFlag = super.saveBatch(orgRefs);

        // 清空缓存
        if(saveBatchFlag){
            // 刷新用户缓存
            this.clearCache(Collections.singletonList(model.getUserId()));
        }

        return true;
    }

    // ============

    /**
     * 清除缓存
     * @param userIds
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


