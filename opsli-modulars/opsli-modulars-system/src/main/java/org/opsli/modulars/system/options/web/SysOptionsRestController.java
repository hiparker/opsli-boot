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

package org.opsli.modulars.system.options.web;


import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import opsli.plugins.crypto.CryptoPlugin;
import opsli.plugins.crypto.enums.CryptoAsymmetricType;
import opsli.plugins.crypto.model.CryptoAsymmetric;
import opsli.plugins.crypto.strategy.CryptoAsymmetricService;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.options.OptionsApi;
import org.opsli.api.wrapper.system.options.OptionsModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.enums.DictType;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.log.annotation.OperateLogger;
import org.opsli.core.log.enums.ModuleEnum;
import org.opsli.core.log.enums.OperationTypeEnum;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.core.utils.OptionsUtil;
import org.opsli.modulars.system.options.entity.SysOptions;
import org.opsli.modulars.system.options.service.ISysOptionsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * 系统参数 Controller
 *
 * @author Parker
 * @date 2021-02-07 18:24:38
 */
@Api(tags = OptionsApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/system/options")
public class SysOptionsRestController extends BaseRestController<SysOptions, OptionsModel, ISysOptionsService>
    implements OptionsApi {


    /**
    * 系统参数 查一条
    * @param model 模型
    * @return ResultWrapper
    */
    @ApiOperation(value = "获得单条系统参数", notes = "获得单条系统参数 - ID")
    @PreAuthorize("hasAuthority('system_options_select')")
    @Override
    public ResultWrapper<OptionsModel> get(OptionsModel model) {
        model = IService.get(model);
        return ResultWrapper.getSuccessResultWrapper(model);
    }

    /**
    * 系统参数 查询分页
    * @param pageNo 当前页
    * @param pageSize 每页条数
    * @param request request
    * @return ResultWrapper
    */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @PreAuthorize("hasAuthority('system_options_select')")
    @Override
    public ResultWrapper<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<SysOptions> queryBuilder = new WebQueryBuilder<>(IService.getEntityClass(), request.getParameterMap());

        // 查询不是内置的数据
        QueryWrapper<SysOptions> queryWrapper = queryBuilder.build();
        queryWrapper.eq("iz_lock", DictType.NO_YES_NO.getValue());

        Page<SysOptions, OptionsModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryWrapper);
        page = IService.findPage(page);

        return ResultWrapper.getSuccessResultWrapper(page.getPageData());
    }

    /**
    * 系统参数 新增
    * @param model 模型
    * @return ResultWrapper
    */
    @ApiOperation(value = "新增系统参数数据", notes = "新增系统参数数据")
    @PreAuthorize("hasAuthority('system_options_insert')")
    @OperateLogger(description = "新增系统参数数据",
            module = ModuleEnum.MODULE_COMMON, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> insert(OptionsModel model) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用新增方法
        IService.insert(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("新增系统参数成功");
    }

    /**
    * 系统参数 修改
    * @param model 模型
    * @return ResultWrapper
    */
    @ApiOperation(value = "修改系统参数数据", notes = "修改系统参数数据")
    @PreAuthorize("hasAuthority('system_options_update')")
    @OperateLogger(description = "修改系统参数数据",
            module = ModuleEnum.MODULE_COMMON, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> update(OptionsModel model) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.update(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("修改系统参数成功");
    }

    /**
     * 系统参数 修改
     * @param params Map
     * @return ResultWrapper
     */
    @ApiOperation(value = "修改系统参数数据", notes = "修改系统参数数据")
    @PreAuthorize("hasAuthority('system_options_update')")
    @OperateLogger(description = "修改系统参数数据",
            module = ModuleEnum.MODULE_COMMON, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> updateOptions(Map<String, String> params) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.updateOptions(params);
        return ResultWrapper.getSuccessResultWrapperByMsg("保存参数成功");
    }


    /**
    * 系统参数 删除
    * @param id ID
    * @return ResultWrapper
    */
    @ApiOperation(value = "删除系统参数数据", notes = "删除系统参数数据")
    @PreAuthorize("hasAuthority('system_options_update')")
    @OperateLogger(description = "删除系统参数数据",
            module = ModuleEnum.MODULE_COMMON, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> del(String id){
        // 演示模式 不允许操作
        super.demoError();

        IService.delete(id);
        return ResultWrapper.getSuccessResultWrapperByMsg("删除系统参数成功");
    }

    /**
    * 系统参数 批量删除
    * @param ids ID 数组
    * @return ResultWrapper
    */
    @ApiOperation(value = "批量删除系统参数数据", notes = "批量删除系统参数数据")
    @PreAuthorize("hasAuthority('system_options_update')")
    @OperateLogger(description = "批量删除系统参数数据",
            module = ModuleEnum.MODULE_COMMON, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> delAll(String ids){
        // 演示模式 不允许操作
        super.demoError();

        String[] idArray = Convert.toStrArray(ids);
        IService.deleteAll(idArray);
        return ResultWrapper.getSuccessResultWrapperByMsg("批量删除系统参数成功");
    }

    /**
     * 系统参数 Excel 导出认证
     *
     * @param type 类型
     * @param request request
     */
    @ApiOperation(value = "Excel 导出认证", notes = "Excel 导出认证")
    @PreAuthorize("hasAnyAuthority('system_options_export', 'system_options_import')")
    @Override
    public ResultWrapper<String> exportExcelAuth(String type, HttpServletRequest request) {
        Optional<String> certificateOptional =
                super.excelExportAuth(type, OptionsApi.SUB_TITLE, request);
        if(!certificateOptional.isPresent()){
            return ResultWrapper.getErrorResultWrapper();
        }
        return ResultWrapper.getSuccessResultWrapper(certificateOptional.get());
    }


    /**
     * 系统参数 Excel 导出
     * @param response response
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @PreAuthorize("hasAuthority('system_options_export')")
    @OperateLogger(description = "导出Excel",
            module = ModuleEnum.MODULE_COMMON, operationType = OperationTypeEnum.SELECT, db = true)
    @Override
    public void exportExcel(String certificate, HttpServletResponse response) {
        // 导出Excel
        super.excelExport(certificate, response);
    }


    /**
    * 系统参数 Excel 导入
    * 注：这里 RequiresPermissions 引入的是 Shiro原生鉴权注解
    * @param request 文件流 request
    * @return ResultWrapper
    */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @PreAuthorize("hasAuthority('system_options_import')")
    @OperateLogger(description = "系统参数 Excel 导入",
            module = ModuleEnum.MODULE_COMMON, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }


    // =========================

    /**
     * 根据编号 获得参数配置
     * @param optionCode 参数编号
     */
    @Override
    public ResultWrapper<OptionsModel> getByCode(String optionCode) {

        QueryWrapper<SysOptions> wrapper = new QueryWrapper<>();
        wrapper.eq("option_code", optionCode);
        SysOptions option = IService.getOne(wrapper);

        return ResultWrapper.getSuccessResultWrapper(
                WrapperUtil.transformInstance(option, OptionsModel.class)
        );
    }

    /**
     * 系统参数 查询全部 对外查询
     * @return ResultWrapper
     */
    @Override
    public ResultWrapper<Map<String, OptionsModel>> findAllOptions() {
        QueryWrapper<SysOptions> queryWrapper = new QueryWrapper<>();
        // 查询内置数据
        queryWrapper.eq("iz_lock", DictType.NO_YES_YES.getValue());
        // 查询不忽略的数据
        queryWrapper.eq("iz_exclude", DictType.NO_YES_NO.getValue());

        // 数据库查询数据
        List<SysOptions> allList = IService.findList(queryWrapper);

        return ResultWrapper.getSuccessResultWrapper(
                OptionsUtil.convertOptionsMap(
                        WrapperUtil.transformInstance(allList, OptionsModel.class))
        );
    }

    /**
     * 系统参数 查询全部 List
     * @return ResultWrapper
     */
    @Override
    public ResultWrapper<List<OptionsModel>> findAll() {
        return ResultWrapper.getSuccessResultWrapper(
                WrapperUtil.transformInstance(IService.findAllList(), OptionsModel.class)
        );
    }


    /**
     * 创建加密 公私钥
     * @return ResultWrapper
     */
    @Override
    @ApiOperation(value = "创建加密 公私钥", notes = "创建加密 公私钥")
    @PreAuthorize("hasAuthority('system_options_update')")
    public ResultWrapper<?> createCrypto(String type) {
        CryptoAsymmetricType cryptoType = CryptoAsymmetricType.getCryptoType(type);
        if(cryptoType == null){
            return ResultWrapper.getErrorResultWrapper();
        }

        CryptoAsymmetricService asymmetricService = CryptoPlugin.getAsymmetric();
        CryptoAsymmetric keyModel = asymmetricService.createKeyModel(cryptoType);
        return ResultWrapper.getSuccessResultWrapper(
                keyModel
        );
    }
}
