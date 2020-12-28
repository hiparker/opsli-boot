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
package org.opsli.modulars.system.area.web;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.area.SysAreaRestApi;
import org.opsli.api.wrapper.system.area.SysAreaModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.EnableLog;
import org.opsli.common.annotation.RequiresPermissionsCus;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.utils.HumpUtil;
import org.opsli.core.base.concroller.BaseRestController;
import org.opsli.core.base.entity.HasChildren;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.core.utils.SpringContextHolder;
import org.opsli.modulars.system.area.entity.SysArea;
import org.opsli.modulars.system.area.service.ISysAreaService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @BelongsProject: opsli-boot
* @BelongsPackage: org.opsli.modulars.system.area.web
* @Author: Parker
* @CreateTime: 2020-11-28 18:59:59
* @Description: 地域表 Controller
*/
@Slf4j
@ApiRestController("/sys/area")
public class SysAreaRestController extends BaseRestController<SysArea, SysAreaModel, ISysAreaService>
    implements SysAreaRestApi {


    /**
    * 地域 查一条
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "获得单条地域", notes = "获得单条地域 - ID")
    @RequiresPermissions("system_area_select")
    @Override
    public ResultVo<SysAreaModel> get(SysAreaModel model) {
        // 如果系统内部调用 则直接查数据库
        if(model != null && model.getIzApi() != null && model.getIzApi()){
            model = IService.get(model);
        }
        return ResultVo.success(model);
    }

    /**
     * 获得组织树树
     * @return ResultVo
     */
    @ApiOperation(value = "获得菜单树", notes = "获得菜单树")
    @RequiresPermissions("system_area_select")
    @Override
    public ResultVo<?> findTree(String parentId) {

        QueryBuilder<SysArea> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysArea> wrapper = queryBuilder.build();
        wrapper.eq(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentId);

        // 获得用户 对应菜单
        List<SysArea> dataList = IService.findList(wrapper);

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey("sortNo");
        treeNodeConfig.setNameKey("areaName");
        // 最大递归深度 最多支持4层菜单
        treeNodeConfig.setDeep(1);

        List<Tree<String>> treeNodes = TreeUtil.build(dataList, parentId, treeNodeConfig,
                (treeNode, tree) -> {

                    String areaCode = treeNode.getAreaCode();
                    int sort = 0;
                    if(StringUtils.isNotEmpty(areaCode)){
                        try {
                            sort = Integer.parseInt(areaCode);
                        }catch (Exception ignored){}
                    }

                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(sort);
                    tree.setName(treeNode.getAreaName());
                    // 扩展属性 ...
                    // 不是外链 则处理组件
                    tree.putExtra("areaCode", areaCode);
                    tree.putExtra("version", treeNode.getVersion());
                });

        Set<String> parentIds = Sets.newHashSet();
        for (Tree<String> treeNode : treeNodes) {
            parentIds.add(treeNode.getId());
        }

        // 数据排查是否存在下级
        List<HasChildren> hasChildrenList = IService.hasChildren(parentIds);
        if(CollUtil.isNotEmpty(hasChildrenList)){
            Map<String, Boolean> tmp = Maps.newHashMap();
            for (HasChildren hasChildren : hasChildrenList) {
                if(hasChildren.getCount() != null && hasChildren.getCount() > 0){
                    tmp.put(hasChildren.getParentId(), true);
                }
            }

            for (Tree<String> treeNode : treeNodes) {
                Boolean tmpFlag = tmp.get(treeNode.getId());
                if(tmpFlag != null && tmpFlag){
                    treeNode.putExtra("hasChildren", true);
                }
            }
        }

        return ResultVo.success(treeNodes);
    }

    /**
    * 地域 新增
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "新增地域数据", notes = "新增地域数据")
    @RequiresPermissions("system_area_insert")
    @EnableLog
    @Override
    public ResultVo<?> insert(SysAreaModel model) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用新增方法
        IService.insert(model);
        return ResultVo.success("新增地域成功");
    }

    /**
    * 地域 修改
    * @param model 模型
    * @return ResultVo
    */
    @ApiOperation(value = "修改地域数据", notes = "修改地域数据")
    @RequiresPermissions("system_area_update")
    @EnableLog
    @Override
    public ResultVo<?> update(SysAreaModel model) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.update(model);
        return ResultVo.success("修改地域成功");
    }


    /**
    * 地域 删除
    * @param id ID
    * @return ResultVo
    */
    @ApiOperation(value = "删除地域数据", notes = "删除地域数据")
    @RequiresPermissions("system_area_update")
    @EnableLog
    @Override
    public ResultVo<?> del(String id){
        // 演示模式 不允许操作
        super.demoError();

        IService.delete(id);
        return ResultVo.success("删除地域成功");
    }

    /**
    * 地域 批量删除
    * @param ids ID 数组
    * @return ResultVo
    */
    @ApiOperation(value = "批量删除地域数据", notes = "批量删除地域数据")
    @RequiresPermissions("system_area_update")
    @EnableLog
    @Override
    public ResultVo<?> delAll(String[] ids){
        // 演示模式 不允许操作
        super.demoError();

        IService.deleteAll(ids);
        return ResultVo.success("批量删除地域成功");
    }


    /**
    * 地域 Excel 导出
    * @param request request
    * @param response response
    * @return ResultVo
    */
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @RequiresPermissionsCus("system_area_export")
    @EnableLog
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "exportExcel");
        QueryBuilder<SysArea> queryBuilder = new WebQueryBuilder<>(SysArea.class, request.getParameterMap());
        super.excelExport(SysAreaRestApi.TITLE, queryBuilder.build(), response, method);
    }

    /**
    * 地域 Excel 导入
    * @param request 文件流 request
    * @return ResultVo
    */
    @ApiOperation(value = "导入Excel", notes = "导入Excel")
    @RequiresPermissions("system_area_import")
    @EnableLog
    @Override
    public ResultVo<?> importExcel(MultipartHttpServletRequest request) {
        return super.importExcel(request);
    }

    /**
    * 地域 Excel 下载导入模版
    * @param response response
    * @return ResultVo
    */
    @ApiOperation(value = "导出Excel模版", notes = "导出Excel模版")
    @RequiresPermissionsCus("system_area_import")
    @Override
    public void importTemplate(HttpServletResponse response) {
        // 当前方法
        Method method = ReflectUtil.getMethodByName(this.getClass(), "importTemplate");
        super.importTemplate(SysAreaRestApi.TITLE, response, method);
    }

    /**
     * 导入数据
     */
    @ApiOperation(value = "获得json数据 查询数据", notes = "获得json数据 查询数据")
    @GetMapping("/importJson")
    public void importJson() {
        // https://github.com/small-dream/China_Province_City
        // JSON 放在 resources下更新当前数据库数据
        ClassPathResource resource = new ClassPathResource("/json/2020年8月中华人民共和国县以上行政区划代码.json");
        try (InputStream inputStream = resource.getInputStream()) {
            String readTpl = IoUtil.read(inputStream, StandardCharsets.UTF_8);

            List<SysAreaModel> tmpList = Lists.newArrayList();

            // 填充省份
            Snowflake snowflake = IdUtil.getSnowflake(1, 1);

            String baseId = snowflake.nextIdStr();
            SysAreaModel model = new SysAreaModel();
            model.setId(baseId);
            model.setAreaCode("86");
            model.setAreaName("中国");
            model.setParentId("0");
            tmpList.add(model);

            JSONArray jsonArray = JSONArray.parseArray(readTpl);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // 填充省份
                SysAreaModel sysAreaModel = new SysAreaModel();
                String sId = snowflake.nextIdStr();
                sysAreaModel.setId(sId);
                sysAreaModel.setAreaCode((String) jsonObject.get("code"));
                sysAreaModel.setAreaName((String) jsonObject.get("name"));
                sysAreaModel.setParentId(baseId);
                tmpList.add(sysAreaModel);

                JSONArray jsonArray2 = jsonObject.getJSONArray("cityList");
                for (int j = 0; j < jsonArray2.size(); j++) {
                    JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
                    // 填充城市
                    SysAreaModel sysAreaModel2 = new SysAreaModel();
                    String cityId = snowflake.nextIdStr();
                    sysAreaModel2.setId(cityId);
                    sysAreaModel2.setAreaCode((String) jsonObject2.get("code"));
                    sysAreaModel2.setAreaName((String) jsonObject2.get("name"));
                    sysAreaModel2.setParentId(sId);
                    tmpList.add(sysAreaModel2);


                    JSONArray jsonArray3 = jsonObject2.getJSONArray("areaList");
                    if(jsonArray3 != null){
                        for (int k = 0; k < jsonArray3.size(); k++) {
                            JSONObject jsonObject3 = jsonArray3.getJSONObject(k);
                            // 填充城市
                            SysAreaModel sysAreaModel3 = new SysAreaModel();
                            String areaId = snowflake.nextIdStr();
                            sysAreaModel3.setId(areaId);
                            sysAreaModel3.setAreaCode((String) jsonObject3.get("code"));
                            sysAreaModel3.setAreaName((String) jsonObject3.get("name"));
                            sysAreaModel3.setParentId(cityId);
                            tmpList.add(sysAreaModel3);
                        }
                    }

                }

            }

            IService.insertBatch(tmpList);

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

}
