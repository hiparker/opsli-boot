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
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.dict.DictDetailApi;
import org.opsli.api.wrapper.system.dict.DictDetailModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.common.annotation.RequiresPermissionsCus;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.exception.ServiceException;
import org.opsli.core.base.concroller.BaseRestController;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.dict.entity.SysDictDetail;
import org.opsli.modulars.system.dict.service.IDictDetailService;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 数据字典明细
 */
@Slf4j
@ApiRestController("/sys/dict/detail")
public class DictDetailRestController extends BaseRestController<SysDictDetail, DictDetailModel, IDictDetailService>
        implements DictDetailApi {


    /** 内置数据 */
    private static final String LOCK_DATA = "1";

    /**
     * 数据字典 查一条
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "获得单条字典明细数据", notes = "获得单条字典明细数据 - ID")
    @Override
    public ResultVo<DictDetailModel> get(DictDetailModel model) {
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

        QueryBuilder<SysDictDetail> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        Page<SysDictDetail, DictDetailModel> page = new Page<>(pageNo, pageSize);
        page.setQueryWrapper(queryBuilder.build());
        page = IService.findPage(page);

        return ResultVo.success(page.getBootstrapData());
    }

    /**
     * 数据字典 新增
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "新增字典明细数据", notes = "新增字典明细数据")
    @RequiresPermissions("system_dict_insert")
    @EnableLog
    @Override
    public ResultVo<?> insert(DictDetailModel model) {
        // 调用新增方法
        IService.insert(model);
        return ResultVo.success("新增字典明细数据成功");
    }

    /**
     * 数据字典 修改
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "修改字典明细数据", notes = "修改字典明细数据")
    @RequiresPermissions("system_dict_update")
    @EnableLog
    @Override
    public ResultVo<?> update(DictDetailModel model) {

        if(model != null){
            DictDetailModel dictDetailModel = IService.get(model.getId());
            // 内置数据 只有超级管理员可以修改
            if(dictDetailModel != null && LOCK_DATA.equals(dictDetailModel.getIzLock()) ){
                UserModel user = UserUtil.getUser();

                if(!UserUtil.SUPER_ADMIN.equals(user.getUsername())){
                    throw new ServiceException(SystemMsg.EXCEPTION_LOCK_DATA);
                }
            }
        }

        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改字典明细数据成功");
    }


    /**
     * 数据字典 删除
     * @param id ID
     * @return ResultVo
     */
    @ApiOperation(value = "删除数据", notes = "删除数据")
    @RequiresPermissions("system_dict_delete")
    @EnableLog
    @Override
    public ResultVo<?> del(String id){

        DictDetailModel dictDetailModel = IService.get(id);
        // 内置数据 只有超级管理员可以修改
        if(dictDetailModel != null && LOCK_DATA.equals(dictDetailModel.getIzLock()) ){
            UserModel user = UserUtil.getUser();

            if(!UserUtil.SUPER_ADMIN.equals(user.getUsername())){
                throw new ServiceException(SystemMsg.EXCEPTION_LOCK_DATA);
            }
        }

        IService.delete(id);
        return ResultVo.success("删除字典数据明细成功");
    }


    /**
     * 数据字典 批量删除
     * @param ids ID 数组
     * @return ResultVo
     */
    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @RequiresPermissions("system_dict_delete")
    @EnableLog
    @Override
    public ResultVo<?> delAll(String ids){
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
                    if(!UserUtil.SUPER_ADMIN.equals(user.getUsername())){
                        throw new ServiceException(SystemMsg.EXCEPTION_LOCK_DATA);
                    }
                }
            }
        }

        IService.deleteAll(idArray);
        return ResultVo.success("批量删除字典数据明细成功");
    }


    /**
     * 数据字典 Excel 导出
     * @param request request
     * @param response response
     * @return ResultVo
     */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @RequiresPermissionsCus("system_dict_export")
    @EnableLog
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "exportExcel");
        QueryBuilder<SysDictDetail> queryBuilder = new WebQueryBuilder<>(entityClazz, request.getParameterMap());
        super.excelExport(DictDetailApi.TITLE, queryBuilder.build(), response, method);
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
     * @return ResultVo
     */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissionsCus("system_dict_import")
    @Override
    public void importTemplate(HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "importTemplate");
        super.importTemplate(DictDetailApi.TITLE, response, method);
    }

    /**
     * 根据字典类型编号 查询出所有字典
     *
     * @param typeCode 字典类型编号
     * @return
     */
    @ApiOperation(value = "根据字典类型编号 查询出所有字典", notes = "根据字典类型编号 查询出所有字典")
    // 权限
    @Override
    public ResultVo<List<DictDetailModel>> findListByTypeCode(String typeCode) {
        return ResultVo.success(IService.findListByTypeCode(typeCode));
    }
}
