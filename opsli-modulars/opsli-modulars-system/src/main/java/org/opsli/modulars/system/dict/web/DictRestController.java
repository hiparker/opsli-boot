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
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.dict.DictApi;
import org.opsli.api.wrapper.system.dict.DictModel;
import org.opsli.api.wrapper.system.dict.DictWrapper;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.common.annotation.RequiresPermissionsCus;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.core.utils.DictUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.dict.entity.SysDict;
import org.opsli.modulars.system.dict.service.IDictService;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;


/**
 * 数据字典 Controller
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Api(tags = DictApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/system/dict")
public class DictRestController extends BaseRestController<SysDict, DictModel, IDictService>
        implements DictApi {

    /** 内置数据 */
    private static final String LOCK_DATA = "1";

    /**
     * 数据字典 查一条
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "获得单条字典数据", notes = "获得单条字典数据 - ID")
    @Override
    public ResultVo<DictModel> get(DictModel model) {
        // 如果系统内部调用 则直接查数据库
        if(model != null && model.getIzApi() != null && model.getIzApi()){
            model = IService.get(model);
        }
        return ResultVo.success(model);
    }

    /**
     * 数据字典 查询分页
     * @param pageNo 当前页
     * @param pageSize 每页条数
     * @param request request
     * @return ResultVo
     */
    @ApiOperation(value = "获得分页数据", notes = "获得分页数据 - 查询构造器")
    @Override
    public ResultVo<?> findPage(Integer pageNo, Integer pageSize, HttpServletRequest request) {

        QueryBuilder<SysDict> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        Page<SysDict, DictModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultVo.success(page.getPageData());
    }

    /**
     * 数据字典 新增
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "新增字典数据", notes = "新增字典数据")
    @RequiresPermissions("system_dict_insert")
    @EnableLog
    @Override
    public ResultVo<?> insert(DictModel model) {
        // 调用新增方法
        IService.insert(model);
        return ResultVo.success("新增字典数据成功");
    }

    /**
     * 数据字典 修改
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "修改字典数据", notes = "修改字典数据")
    @RequiresPermissions("system_dict_update")
    @EnableLog
    @Override
    public ResultVo<?> update(DictModel model) {

        if(model != null){
            DictModel dictModel = IService.get(model.getId());
            // 内置数据 只有超级管理员可以修改
            if(dictModel != null && LOCK_DATA.equals(dictModel.getIzLock()) ){
                UserModel user = UserUtil.getUser();

                if(!StringUtils.equals(UserUtil.SUPER_ADMIN, user.getUsername())){
                    throw new ServiceException(SystemMsg.EXCEPTION_LOCK_DATA);
                }
            }
        }

        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改字典数据成功");
    }


    /**
     * 数据字典 删除
     * @param id ID
     * @return ResultVo
     */
    @ApiOperation(value = "删除字典数据数据", notes = "删除字典数据数据")
    @RequiresPermissions("system_dict_delete")
    @EnableLog
    @Override
    public ResultVo<?> del(String id){

        DictModel dictModel = IService.get(id);
        // 内置数据 只有超级管理员可以修改
        if(dictModel != null && LOCK_DATA.equals(dictModel.getIzLock()) ){
            UserModel user = UserUtil.getUser();

            if(!StringUtils.equals(UserUtil.SUPER_ADMIN, user.getUsername())){
                throw new ServiceException(SystemMsg.EXCEPTION_LOCK_DATA);
            }
        }

        IService.delete(id);
        return ResultVo.success("删除字典数据成功");
    }


    /**
     * 数据字典 批量删除
     * @param ids ID 数组
     * @return ResultVo
     */
    @ApiOperation(value = "批量删除字典数据", notes = "批量删除字典明细数据")
    @RequiresPermissions("system_dict_insert")
    @EnableLog
    @Override
    public ResultVo<?> delAll(String ids){
        String[] idArray = Convert.toStrArray(ids);
        if(ids != null){
            QueryBuilder<SysDict> queryBuilder = new GenQueryBuilder<>();
            QueryWrapper<SysDict> wrapper = queryBuilder.build();
            List<String> idList = Convert.toList(String.class, idArray);

            wrapper.in(MyBatisConstants.FIELD_ID, idList);
            List<SysDict> dictList = IService.findList(wrapper);
            for (SysDict sysDict : dictList) {
                // 内置数据 只有超级管理员可以修改
                if(sysDict != null && LOCK_DATA.equals(sysDict.getIzLock()) ){
                    UserModel user = UserUtil.getUser();
                    if(!StringUtils.equals(UserUtil.SUPER_ADMIN, user.getUsername())){
                        throw new ServiceException(SystemMsg.EXCEPTION_LOCK_DATA);
                    }
                }
            }
        }

        IService.deleteAll(idArray);
        return ResultVo.success("批量删除字典数据成功");
    }


    /**
     * 数据字典 Excel 导出
     * @param request request
     * @param response response
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @RequiresPermissionsCus("system_dict_export")
    @EnableLog
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "exportExcel");
        QueryBuilder<SysDict> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        super.excelExport(DictApi.SUB_TITLE, queryBuilder.build(), response, method);
    }

    /**
     * 数据字典 Excel 导入
     * @param request 文件流 request
     * @return ResultVo
     */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @RequiresPermissions("system_dict_import")
    @EnableLog
    @Override
    public ResultVo<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }

    /**
     * 数据字典 Excel 下载导入模版
     * @param response response
     */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissionsCus("system_dict_import")
    @Override
    public void importTemplate(HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "importTemplate");
        super.importTemplate(DictApi.SUB_TITLE, response, method);
    }

    /**
     * 根据字典类型编号 查询出所有字典
     *
     * @param typeCode 字典类型编号
     * @return ResultVo
     */
    @ApiOperation(value = "根据字典类型编号 查询出所有字典", notes = "根据字典类型编号 查询出所有字典")
    @Override
    public ResultVo<?> getDictListByCode(String typeCode) {
        List<DictWrapper> dictList = DictUtil.getDictList(typeCode);
        if(dictList == null){
            return ResultVo.error("暂无该字典");
        }
        return ResultVo.success(dictList);
    }
}
