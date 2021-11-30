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
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import opsli.plugins.crypto.CryptoPlugin;
import opsli.plugins.crypto.enums.CryptoAsymmetricType;
import opsli.plugins.crypto.model.CryptoAsymmetric;
import opsli.plugins.crypto.strategy.CryptoAsymmetricService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.options.OptionsApi;
import org.opsli.api.wrapper.system.options.OptionsModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.common.annotation.RequiresPermissionsCus;
import org.opsli.common.enums.DictType;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.core.utils.OptionsUtil;
import org.opsli.modulars.system.options.entity.SysOptions;
import org.opsli.modulars.system.options.service.ISysOptionsService;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


/**
 * 系统参数 Controller
 *
 * @author Parker
 * @date 2021-02-07 18:24:38
 */
@Api(tags = OptionsApi.TITLE)
@Slf4j
@ApiRestController("/system/options/{ver}")
public class SysOptionsRestController extends BaseRestController<SysOptions, OptionsModel, ISysOptionsService>
    implements OptionsApi {


    /**
    * 系统参数 查一条
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "获得单条系统参数", notes = "获得单条系统参数 - ID")
    @RequiresPermissions("system_options_select")
    @Override
    public ResultVo<OptionsModel> get(OptionsModel model) {
        // 如果系统内部调用 则直接查数据库
        if(model != null && model.getIzApi() != null && model.getIzApi()){
            model = IService.get(model);
        }
        return ResultVo.success(model);
    }

    /**
    * 系统参数 查询分页
    * @param pageNo 当前页
    * @param pageSize 每页条数
    * @param request request
    * @return ResultVo
    */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @RequiresPermissions("system_options_select")
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<SysOptions> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());

        // 查询不是内置的数据
        QueryWrapper<SysOptions> queryWrapper = queryBuilder.build();
        queryWrapper.eq("iz_lock", DictType.NO_YES_NO.getValue());

        Page<SysOptions, OptionsModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryWrapper);
        page = IService.findPage(page);

        return ResultVo.success(page.getPageData());
    }

    /**
    * 系统参数 新增
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "新增系统参数数据", notes = "新增系统参数数据")
    @RequiresPermissions("system_options_insert")
    @EnableLog
    @Override
    public ResultVo<?> insert(OptionsModel model) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用新增方法
        IService.insert(model);
        return ResultVo.success("新增系统参数成功");
    }

    /**
    * 系统参数 修改
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "修改系统参数数据", notes = "修改系统参数数据")
    @RequiresPermissions("system_options_update")
    @EnableLog
    @Override
    public ResultVo<?> update(OptionsModel model) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改系统参数成功");
    }

    /**
     * 系统参数 修改
     * @param params Map
     * @return ResultVo
     */
    @ApiOperation(value = "修改系统参数数据", notes = "修改系统参数数据")
    @RequiresPermissions("system_options_update")
    @EnableLog
    @Override
    public ResultVo<?> updateOptions(Map<String, String> params) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.updateOptions(params);
        return ResultVo.success("保存参数成功");
    }


    /**
    * 系统参数 删除
    * @param id ID
    * @return ResultVo
    */
    @ApiOperation(value = "删除系统参数数据", notes = "删除系统参数数据")
    @RequiresPermissions("system_options_update")
    @EnableLog
    @Override
    public ResultVo<?> del(String id){
        // 演示模式 不允许操作
        super.demoError();

        IService.delete(id);
        return ResultVo.success("删除系统参数成功");
    }

    /**
    * 系统参数 批量删除
    * @param ids ID 数组
    * @return ResultVo
    */
    @ApiOperation(value = "批量删除系统参数数据", notes = "批量删除系统参数数据")
    @RequiresPermissions("system_options_update")
    @EnableLog
    @Override
    public ResultVo<?> delAll(String ids){
        // 演示模式 不允许操作
        super.demoError();

        String[] idArray = Convert.toStrArray(ids);
        IService.deleteAll(idArray);
        return ResultVo.success("批量删除系统参数成功");
    }


    /**
    * 系统参数 Excel 导出
    * 注：这里 RequiresPermissionsCus 引入的是 自定义鉴权注解
    *
    * 导出时，Token认证和方法权限认证 全部都由自定义完成
    * 因为在 导出不成功时，需要推送错误信息，
    * 前端直接走下载流，当失败时无法获得失败信息，即使前后端换一种方式后端推送二进制文件前端再次解析也是最少2倍的耗时
    * ，且如果数据量过大，前端进行渲染时直接会把浏览器卡死
    * 而直接开启socket接口推送显然是太过浪费资源了，所以目前采用Java最原始的手段
    * response 推送 javascript代码 alert 提示报错信息
    *
    * @param request request
    * @param response response
    */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @RequiresPermissionsCus("system_options_export")
    @EnableLog
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "exportExcel");
        QueryBuilder<SysOptions> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        super.excelExport(OptionsApi.SUB_TITLE, queryBuilder.build(), response, method);
    }

    /**
    * 系统参数 Excel 导入
    * 注：这里 RequiresPermissions 引入的是 Shiro原生鉴权注解
    * @param request 文件流 request
    * @return ResultVo
    */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @RequiresPermissions("system_options_import")
    @EnableLog
    @Override
    public ResultVo<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }

    /**
    * 系统参数 Excel 下载导入模版
    * 注：这里 RequiresPermissionsCus 引入的是 自定义鉴权注解
    * @param response response
    */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissionsCus("system_options_import")
    @Override
    public void importTemplate(HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "importTemplate");
        super.importTemplate(OptionsApi.SUB_TITLE, response, method);
    }

    // =========================

    /**
     * 根据编号 获得参数配置
     * @param optionCode 参数编号
     */
    @Override
    public ResultVo<OptionsModel> getByCode(String optionCode) {

        QueryWrapper<SysOptions> wrapper = new QueryWrapper<>();
        wrapper.eq("option_code", optionCode);
        SysOptions option = IService.getOne(wrapper);

        return ResultVo.success(
                WrapperUtil.transformInstance(option, OptionsModel.class)
        );
    }

    /**
     * 系统参数 查询全部
     * @return ResultVo
     */
    @Override
    public ResultVo<Map<String, OptionsModel>> findAllOptions() {
        QueryWrapper<SysOptions> queryWrapper = new QueryWrapper<>();
        // 查询内置数据
        queryWrapper.eq("iz_lock", DictType.NO_YES_YES.getValue());
        // 数据库查询数据
        List<SysOptions> allList = IService.findList(queryWrapper);
        return ResultVo.success(
                OptionsUtil.convertOptionsMap(
                        WrapperUtil.transformInstance(allList, OptionsModel.class))
        );
    }

    /**
     * 系统参数 查询全部 List
     * @return ResultVo
     */
    @Override
    public ResultVo<List<OptionsModel>> findAll() {
        return ResultVo.success(
                WrapperUtil.transformInstance(IService.findAllList(), OptionsModel.class)
        );
    }


    /**
     * 创建加密 公私钥
     * @return ResultVo
     */
    @Override
    @ApiOperation(value = "创建加密 公私钥", notes = "创建加密 公私钥")
    @RequiresPermissions("system_options_update")
    public ResultVo<?> createCrypto(String type) {
        CryptoAsymmetricType cryptoType = CryptoAsymmetricType.getCryptoType(type);
        if(cryptoType == null){
            return ResultVo.error();
        }

        CryptoAsymmetricService asymmetricService = CryptoPlugin.getAsymmetric();
        CryptoAsymmetric keyModel = asymmetricService.createKeyModel(cryptoType);
        return ResultVo.success(
                keyModel
        );
    }
}
