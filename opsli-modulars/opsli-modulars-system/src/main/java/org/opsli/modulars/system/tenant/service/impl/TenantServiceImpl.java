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
package org.opsli.modulars.system.tenant.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.tenant.TenantModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.utils.TenantUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.tenant.entity.SysTenant;
import org.opsli.modulars.system.tenant.mapper.TenantMapper;
import org.opsli.modulars.system.tenant.service.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.system.service
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:34
 * @Description: 租户 接口实现类
 */
@Service
public class TenantServiceImpl extends CrudServiceImpl<TenantMapper, SysTenant, TenantModel> implements ITenantService {

    @Autowired(required = false)
    private TenantMapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TenantModel insert(TenantModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        Integer count = this.uniqueVerificationByName(model);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_TENANT_UNIQUE);
        }

        return super.insert(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TenantModel update(TenantModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        Integer count = this.uniqueVerificationByName(model);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_TENANT_UNIQUE);
        }

        TenantModel tenantModel = super.update(model);
        if(tenantModel != null){
            // 清除缓存
            this.clearCache(Collections.singletonList(model.getId()));
        }
        return tenantModel;
    }


    /**
     * 删除
     * @param id ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        TenantModel tenantModel = this.get(id);
        if(tenantModel == null){
            return false;
        }

        boolean ret = super.delete(id);

        if(ret){
            // 清除缓存
            this.clearCache(Collections.singletonList(tenantModel.getId()));
        }

        return ret;
    }

    /**
     * 删除
     * @param model 数据模型
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(TenantModel model) {
        TenantModel tenantModel = this.get(model);
        if(tenantModel == null){
            return false;
        }

        boolean ret = super.delete(model);

        if(ret){
            // 清除缓存
            this.clearCache(Collections.singletonList(tenantModel.getId()));
        }

        return ret;
    }

    /**
     * 删除 - 多个
     * @param ids id数组
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAll(String[] ids) {
        List<String> idList = Convert.toList(String.class, ids);

        boolean ret = super.deleteAll(ids);
        if(ret){
            // 清除缓存
            this.clearCache(idList);
        }
        return ret;
    }


    /**
     * 删除 - 多个
     * @param models 封装模型
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAll(Collection<TenantModel> models) {
        List<String> idList = Lists.newArrayListWithCapacity(models.size());
        for (TenantModel model : models) {
            idList.add(model.getId());
        }

        boolean ret = super.deleteAll(models);
        if(ret){
            // 清除缓存
            this.clearCache(idList);
        }
        return ret;
    }


    /**
     * 唯一验证
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public Integer uniqueVerificationByName(TenantModel model){
        if(model == null){
            return null;
        }
        QueryWrapper<SysTenant> wrapper = new QueryWrapper<>();

        // name 唯一
        wrapper.eq(MyBatisConstants.FIELD_DELETE_LOGIC, "0")
                .eq("tenant_name", model.getTenantName());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        return super.count(wrapper);
    }

    // ============


    /**
     * 清除缓存
     * @param tenantIds
     */
    private void clearCache(List<String> tenantIds){
        // 清空缓存
        if(CollUtil.isNotEmpty(tenantIds)){
            int cacheCount = 0;
            for (String tenantId : tenantIds) {
                cacheCount++;
                boolean tmp = TenantUtil.refreshTenant(tenantId);
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


