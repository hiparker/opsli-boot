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
package org.opsli.modulars.system.dict.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.dict.DictDetailModel;
import org.opsli.api.wrapper.system.dict.DictModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.enums.DictType;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.dict.entity.SysDict;
import org.opsli.modulars.system.dict.mapper.DictMapper;
import org.opsli.modulars.system.dict.service.IDictDetailService;
import org.opsli.modulars.system.dict.service.IDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


/**
 * 数据字典 接口实现类
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Service
public class DictServiceImpl extends CrudServiceImpl<DictMapper, SysDict, DictModel> implements IDictService {

    @Autowired(required = false)
    private DictMapper mapper;
    @Autowired
    private IDictDetailService iDictDetailService;

    @Override
    public DictModel insert(DictModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        boolean verificationByCode = this.uniqueVerificationByCode(model);
        if(!verificationByCode){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_DICT_UNIQUE);
        }

        return super.insert(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DictModel update(DictModel model) {
        if(model == null){
            return null;
        }

        // 唯一验证
        boolean verificationByCode = this.uniqueVerificationByCode(model);
        if(!verificationByCode){
            // 重复
            throw new ServiceException(SystemMsg.EXCEPTION_DICT_UNIQUE);
        }

        DictModel dictModel = super.get(model.getId());
        DictModel updateRet = super.update(model);

        if(updateRet != null){
            // 字典主表修改 子表跟着联动 （验证是否改了编号）/ 或者修改不允许改编号
            List<DictDetailModel> listByTypeCode = null;
            if(StringUtils.isNotEmpty(model.getTypeCode())){
                listByTypeCode = iDictDetailService.findListByTypeCode(dictModel.getTypeCode());
            }
            if(listByTypeCode != null && listByTypeCode.size() > 0){
                for (DictDetailModel dictDetailModel : listByTypeCode) {
                    dictDetailModel.setTypeCode(updateRet.getTypeCode());
                    iDictDetailService.update(dictDetailModel);
                }
            }
        }

        return updateRet;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(String id) {
        // 删除字典明细表
        iDictDetailService.delByParent(id);

        // 删除自身数据
        return super.delete(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(DictModel model) {
        if(model == null || StringUtils.isEmpty(model.getId())){
            return false;
        }

        // 删除字典明细表
        iDictDetailService.delByParent(model.getId());

        // 删除自身数据
        return super.delete(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteAll(String[] ids) {
        if(ids == null){
            return false;
        }

        // 删除字典明细表
        for (String id : ids) {
            iDictDetailService.delByParent(id);
        }
        // 删除自身数据
        return super.deleteAll(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteAll(Collection<DictModel> models) {
        if(models == null || models.isEmpty()){
            return false;
        }

        // 删除字典明细表
        for (DictModel model : models) {
            if(model == null || StringUtils.isEmpty(model.getId())){
                continue;
            }
            iDictDetailService.delByParent(model.getId());
        }
        // 删除自身数据
        return super.deleteAll(models);
    }

    // ==============================

    /**
     * 唯一验证
     * @param model model
     * @return Integer
     */
    @Transactional(readOnly = true)
    public boolean uniqueVerificationByCode(DictModel model){
        if(model == null){
            return false;
        }
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();

        // code 唯一
        wrapper.eq(MyBatisConstants.FIELD_DELETE_LOGIC, DictType.NO_YES_NO.getValue())
                .eq("type_code", model.getTypeCode());

        // 重复校验排除自身
        if(StringUtils.isNotEmpty(model.getId())){
            wrapper.notIn(MyBatisConstants.FIELD_ID, model.getId());
        }

        return super.count(wrapper) == 0;
    }

}


