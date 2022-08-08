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
package org.opsli.modulars.system.dict.web;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.dict.DictDetailApi;
import org.opsli.api.wrapper.system.dict.DictDetailModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.log.annotation.OperateLogger;
import org.opsli.core.log.enums.ModuleEnum;
import org.opsli.core.log.enums.OperationTypeEnum;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.dict.entity.SysDictDetail;
import org.opsli.modulars.system.dict.service.IDictDetailService;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 数据字典明细 Controller
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Api(tags = DictDetailApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/system/dict/detail")
public class DictDetailRestController extends BaseRestController<SysDictDetail, DictDetailModel, IDictDetailService>
        implements DictDetailApi {


    /** 内置数据 */
    private static final String LOCK_DATA = "1";

    /**
     * 数据字典 查一条
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得单条字典明细数据", notes = "获得单条字典明细数据 - ID")
    @Override
    public ResultWrapper<DictDetailModel> get(DictDetailModel model) {
        model = IService.get(model);
        return ResultWrapper.getSuccessResultWrapper(model);
    }

    /**
     * 数据字典 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @Override
    public ResultWrapper<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<SysDictDetail> queryBuilder = new WebQueryBuilder<>(IService.getEntityClass(), request.getParameterMap());
        Page<SysDictDetail, DictDetailModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultWrapper.getSuccessResultWrapper(page.getPageData());
    }

    /**
     * 数据字典 新增
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "新增字典明细数据", notes = "新增字典明细数据")
    @PreAuthorize("hasAuthority('system_dict_insert')")
    @OperateLogger(description = "新增字典明细数据",
            module = ModuleEnum.MODULE_DICT, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> insert(DictDetailModel model) {
        // 调用新增方法
        IService.insert(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("新增字典明细数据成功");
    }

    /**
     * 数据字典 修改
     * @param model 模型
     * @return ResultWrapper
     */
    @ApiOperation(value = "修改字典明细数据", notes = "修改字典明细数据")
    @PreAuthorize("hasAuthority('system_dict_update')")
    @OperateLogger(description = "修改字典明细数据",
            module = ModuleEnum.MODULE_DICT, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> update(DictDetailModel model) {

        if(model != null){
            DictDetailModel dictDetailModel = IService.get(model.getId());
            // 内置数据 只有超级管理员可以修改
            if(dictDetailModel != null && LOCK_DATA.equals(dictDetailModel.getIzLock()) ){
                UserModel user = UserUtil.getUser();

                if(!StringUtils.equals(UserUtil.SUPER_ADMIN, user.getUsername())){
                    throw new ServiceException(SystemMsg.EXCEPTION_LOCK_DATA);
                }
            }
        }

        // 调用修改方法
        IService.update(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("修改字典明细数据成功");
    }


    /**
     * 数据字典 删除
     * @param id ID
     * @return ResultWrapper
     */
    @ApiOperation(value = "删除数据", notes = "删除数据")
    @PreAuthorize("hasAuthority('system_dict_delete')")
    @OperateLogger(description = "删除字典明细数据",
            module = ModuleEnum.MODULE_DICT, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> del(String id){

        DictDetailModel dictDetailModel = IService.get(id);
        // 内置数据 只有超级管理员可以修改
        if(dictDetailModel != null && LOCK_DATA.equals(dictDetailModel.getIzLock()) ){
            UserModel user = UserUtil.getUser();

            if(!StringUtils.equals(UserUtil.SUPER_ADMIN, user.getUsername())){
                throw new ServiceException(SystemMsg.EXCEPTION_LOCK_DATA);
            }
        }

        IService.delete(id);
        return ResultWrapper.getSuccessResultWrapperByMsg("删除字典数据明细成功");
    }


    /**
     * 数据字典 批量删除
     * @param ids ID 数组
     * @return ResultWrapper
     */
    @ApiOperation(value = "批量删除字典数据", notes = "批量删除数据")
    @PreAuthorize("hasAuthority('system_dict_delete')")
    @OperateLogger(description = "批量删除字典明细数据",
            module = ModuleEnum.MODULE_DICT, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> delAll(String ids){
        String[] idArray = Convert.toStrArray(ids);
        if(ids != null){
            QueryBuilder<SysDictDetail> queryBuilder = new GenQueryBuilder<>();
            QueryWrapper<SysDictDetail> wrapper = queryBuilder.build();
            List<String> idList = Convert.toList(String.class, idArray);

            wrapper.in(MyBatisConstants.FIELD_ID, idList);
            List<SysDictDetail> dictList = IService.findList(wrapper);
            for (SysDictDetail sysDictDetail : dictList) {
                // 内置数据 只有超级管理员可以修改
                if(sysDictDetail != null && LOCK_DATA.equals(sysDictDetail.getIzLock()) ){
                    UserModel user = UserUtil.getUser();
                    if(!StringUtils.equals(UserUtil.SUPER_ADMIN, user.getUsername())){
                        throw new ServiceException(SystemMsg.EXCEPTION_LOCK_DATA);
                    }
                }
            }
        }

        IService.deleteAll(idArray);
        return ResultWrapper.getSuccessResultWrapperByMsg("批量删除字典数据明细成功");
    }


    /**
     * 根据字典类型编号 查询出所有字典
     *
     * @param typeCode 字典类型编号
     * @return ResultWrapper
     */
    @ApiOperation(value = "根据字典类型编号 查询出所有字典", notes = "根据字典类型编号 查询出所有字典")
    @Override
    public ResultWrapper<List<DictDetailModel>> findListByTypeCode(String typeCode) {
        return ResultWrapper.getSuccessResultWrapper(
                IService.findListByTypeCode(typeCode));
    }
}
