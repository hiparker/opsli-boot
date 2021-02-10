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

package org.opsli.modulars.system.other.crypto.web;


import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.opsli.api.web.system.other.crypto.OtherCryptoAsymmetricRestApi;
import org.opsli.api.wrapper.system.options.OptionsModel;
import org.opsli.api.wrapper.system.other.crypto.OtherCryptoAsymmetricModel;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.annotation.RequiresPermissionsCus;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.modulars.system.options.entity.SysOptions;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


import org.opsli.modulars.system.other.crypto.entity.OtherCryptoAsymmetric;
import org.opsli.modulars.system.other.crypto.service.IOtherCryptoAsymmetricService;


/**
* @BelongsProject: opsli-boot

* @BelongsPackage: org.opsli.modulars.system.other.crypto.web

* @Author: Parker
* @CreateTime: 2021-02-10 17:09:34
* @Description: 非对称加密 Controller
*/
@Api(tags = "非对称加密")
@Slf4j

@ApiRestController("/other/crypto")

public class OtherCryptoAsymmetricRestController extends BaseRestController<OtherCryptoAsymmetric, OtherCryptoAsymmetricModel, IOtherCryptoAsymmetricService>
    implements OtherCryptoAsymmetricRestApi {


    /**
    * 非对称加密 查一条
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "获得单条非对称加密", notes = "获得单条非对称加密 - ID")
    @RequiresPermissions("other_crypto_select")
    @Override
    public ResultVo<OtherCryptoAsymmetricModel> get(OtherCryptoAsymmetricModel model) {
        // 如果系统内部调用 则直接查数据库
        if(model != null && model.getIzApi() != null && model.getIzApi()){
            model = IService.get(model);
        }
        return ResultVo.success(model);
    }

    /**
    * 非对称加密 查询分页
    * @param pageNo 当前页
    * @param pageSize 每页条数
    * @param request request
    * @return ResultVo
    */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @RequiresPermissions("other_crypto_select")
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<OtherCryptoAsymmetric> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        Page<OtherCryptoAsymmetric, OtherCryptoAsymmetricModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultVo.success(page.getBootstrapData());
    }

    /**
    * 非对称加密 新增
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "新增非对称加密数据", notes = "新增非对称加密数据")
    @RequiresPermissions("other_crypto_insert")
    @EnableLog
    @Override
    public ResultVo<?> insert(OtherCryptoAsymmetricModel model) {
        // 调用新增方法
        IService.insert(model);
        return ResultVo.success("新增非对称加密成功");
    }

    /**
    * 非对称加密 修改
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "修改非对称加密数据", notes = "修改非对称加密数据")
    @RequiresPermissions("other_crypto_update")
    @EnableLog
    @Override
    public ResultVo<?> update(OtherCryptoAsymmetricModel model) {
        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改非对称加密成功");
    }


    /**
    * 非对称加密 删除
    * @param id ID
    * @return ResultVo
    */
    @ApiOperation(value = "删除非对称加密数据", notes = "删除非对称加密数据")
    @RequiresPermissions("other_crypto_update")
    @EnableLog
    @Override
    public ResultVo<?> del(String id){
        IService.delete(id);
        return ResultVo.success("删除非对称加密成功");
    }

    /**
    * 非对称加密 批量删除
    * @param ids ID 数组
    * @return ResultVo
    */
    @ApiOperation(value = "批量删除非对称加密数据", notes = "批量删除非对称加密数据")
    @RequiresPermissions("other_crypto_update")
    @EnableLog
    @Override
    public ResultVo<?> delAll(String ids){
        String[] idArray = Convert.toStrArray(ids);
        IService.deleteAll(idArray);
        return ResultVo.success("批量删除非对称加密成功");
    }


    /**
    * 非对称加密 Excel 导出
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
    @RequiresPermissionsCus("other_crypto_export")
    @EnableLog
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "exportExcel");
        QueryBuilder<OtherCryptoAsymmetric> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        super.excelExport(OtherCryptoAsymmetricRestApi.TITLE, queryBuilder.build(), response, method);
    }

    /**
    * 非对称加密 Excel 导入
    * 注：这里 RequiresPermissions 引入的是 Shiro原生鉴权注解
    * @param request 文件流 request
    * @return ResultVo
    */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @RequiresPermissions("other_crypto_import")
    @EnableLog
    @Override
    public ResultVo<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }

    /**
    * 非对称加密 Excel 下载导入模版
    * 注：这里 RequiresPermissionsCus 引入的是 自定义鉴权注解
    * @param response response
    */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissionsCus("other_crypto_import")
    @Override
    public void importTemplate(HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "importTemplate");
        super.importTemplate(OtherCryptoAsymmetricRestApi.TITLE, response, method);
    }

    // =======================


    @Override
    public ResultVo<OtherCryptoAsymmetricModel> getByCryptoType(String cryptoType) {
        QueryWrapper<OtherCryptoAsymmetric> wrapper = new QueryWrapper<>();
        wrapper.eq("crypto_type", cryptoType);
        OtherCryptoAsymmetric entity = IService.getOne(wrapper);

        return ResultVo.success(
                WrapperUtil.transformInstance(entity, OtherCryptoAsymmetricModel.class)
        );
    }
}
