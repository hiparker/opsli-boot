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

package org.opsli.modulars.system.other.crypto.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.other.crypto.OtherCryptoAsymmetricModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.CryptoAsymmetricType;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.utils.CryptoAsymmetricUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.other.crypto.entity.OtherCryptoAsymmetric;
import org.opsli.modulars.system.other.crypto.mapper.OtherCryptoAsymmetricMapper;
import org.opsli.modulars.system.other.crypto.service.IOtherCryptoAsymmetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


/**
* @BelongsProject: opsli-boot
* @BelongsPackage: org.opsli.modulars.system.other.crypto.service.impl
* @Author: Parker
* @CreateTime: 2021-02-10 17:09:34
* @Description: 非对称加密 Service Impl
*/
@Service
public class OtherCryptoAsymmetricServiceImpl extends CrudServiceImpl<OtherCryptoAsymmetricMapper, OtherCryptoAsymmetric, OtherCryptoAsymmetricModel>
    implements IOtherCryptoAsymmetricService {

    @Autowired(required = false)
    private OtherCryptoAsymmetricMapper mapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public OtherCryptoAsymmetricModel insert(OtherCryptoAsymmetricModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        Integer count = this.uniqueVerificationByCode(model);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_OTHER_CRYPTO_UNIQUE);
        }

        return super.insert(model);
    }

    /***
     * 重置数据
     * @param cryptoAsymmetricType 枚举
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public OtherCryptoAsymmetricModel reset(CryptoAsymmetricType cryptoAsymmetricType) {
        if(cryptoAsymmetricType == null){
            return null;
        }

        // 唯一验证
        OtherCryptoAsymmetricModel model = new OtherCryptoAsymmetricModel();
        model.setCryptoType(cryptoAsymmetricType.getCode());
        Integer count = this.uniqueVerificationByCode(model);

        // 修改
        if(count > 0){

        }

        // 新增



        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_OTHER_CRYPTO_UNIQUE);
        }

        model = super.update(model);
        if(model != null){
            // 清除缓存
            this.clearCache(Collections.singletonList(model));
        }

        return model;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        OtherCryptoAsymmetricModel model = super.get(id);
        boolean ret = super.delete(id);

        if(ret){
            // 清除缓存
            this.clearCache(Collections.singletonList(model));
        }
        return ret;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAll(String[] ids) {
        QueryWrapper<OtherCryptoAsymmetric> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(MyBatisConstants.FIELD_ID, Convert.toList(String.class, ids));
        List<OtherCryptoAsymmetricModel> modelList = super.transformTs2Ms(
                super.findList(queryWrapper)
        );

        // 清除缓存
        this.clearCache(modelList);

        return super.deleteAll(ids);
    }

    // =======================

    /**
     * 唯一验证
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public Integer uniqueVerificationByCode(OtherCryptoAsymmetricModel model){
        if(model == null){
            return null;
        }
        QueryWrapper<OtherCryptoAsymmetric> wrapper = new QueryWrapper<>();
        wrapper.eq("crypto_type", model.getCryptoType());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        return super.count(wrapper);
    }

    /**
     * 清除缓存
     * @param modelList
     */
    private void clearCache(List<OtherCryptoAsymmetricModel> modelList){
        // 清空缓存
        if(CollUtil.isNotEmpty(modelList)){
            int cacheCount = 0;
            for (OtherCryptoAsymmetricModel model : modelList) {
                cacheCount++;
                boolean tmp = CryptoAsymmetricUtil.refresh(model);
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
