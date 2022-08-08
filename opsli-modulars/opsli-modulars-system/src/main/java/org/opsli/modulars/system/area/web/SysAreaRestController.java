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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.web.system.area.SysAreaRestApi;
import org.opsli.api.wrapper.system.area.SysAreaModel;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.utils.FieldUtil;
import org.opsli.core.base.controller.BaseRestController;
import org.opsli.core.log.annotation.OperateLogger;
import org.opsli.core.log.enums.ModuleEnum;
import org.opsli.core.log.enums.OperationTypeEnum;
import org.opsli.core.utils.TreeBuildUtil;
import org.opsli.modulars.system.area.entity.SysArea;
import org.opsli.modulars.system.area.service.ISysAreaService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 地域表 Controller
 *
 * @author Parker
 * @date 2020-11-28 18:59:59
 */
@Api(tags = SysAreaRestApi.TITLE)
@Slf4j
@ApiRestController("/{ver}/system/area")
public class SysAreaRestController extends BaseRestController<SysArea, SysAreaModel, ISysAreaService>
    implements SysAreaRestApi {

    /** 排序字段 */
    private static final String SORT_FIELD = "sortNo";

    /**
    * 地域 查一条
    * @param model 模型
    * @return ResultWrapper
    */
    @ApiOperation(value = "获得单条地域", notes = "获得单条地域 - ID")
    @PreAuthorize("hasAuthority('system_area_select')")
    @Override
    public ResultWrapper<SysAreaModel> get(SysAreaModel model) {
        model = IService.get(model);
        return ResultWrapper.getSuccessResultWrapper(model);
    }

    /**
     * 获得组织树树
     * @return ResultWrapper
     */
    @ApiOperation(value = "获得菜单树", notes = "获得菜单树")
    @PreAuthorize("hasAuthority('system_area_select')")
    @Override
    public ResultWrapper<?> findTree(String parentId) {

        QueryWrapper<SysArea> wrapper = new QueryWrapper<>();
        wrapper.eq(FieldUtil.humpToUnderline(MyBatisConstants.FIELD_PARENT_ID), parentId);
        List<SysArea> dataList =  IService.findList(wrapper);

        // 获得BeanMapList
        List<Map<String, Object>> beanMapList = this.getBeanMapList(dataList);

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey(SORT_FIELD);
        // 最大递归深度 最多支持1层
        treeNodeConfig.setDeep(1);

        //转换器
        List<Tree<Object>> treeNodes = TreeBuildUtil.INSTANCE.build(beanMapList, parentId, treeNodeConfig);

        // 处理是否包含子集
        super.handleTreeHasChildren(treeNodes,
                (parentIds)-> IService.hasChildren(parentIds));

        return ResultWrapper.getSuccessResultWrapper(treeNodes);
    }

    /**
     * 获取全量地域列表
     *
     * @param deep 层级
     *
     *
     * @return ResultWrapper
     */
    @ApiOperation(value = "获取全量地域列表", notes = "获取全量地域列表")
    @PreAuthorize("hasAuthority('system_area_select')")
    @Override
    public ResultWrapper<?> findTreeAll(Integer deep) {

        List<SysArea> dataList =  IService.findList(new QueryWrapper<>());

        // 获得BeanMapList
        List<Map<String, Object>> beanMapList = this.getBeanMapList(dataList);

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey(SORT_FIELD);
        // 最大递归深度 最多支持1层
        treeNodeConfig.setDeep(deep);

        //转换器
        List<Tree<Object>> treeNodes = TreeBuildUtil.INSTANCE.build(beanMapList, treeNodeConfig);

        // 处理是否包含子集
        super.handleTreeHasChildren(treeNodes,
                (parentIds)-> IService.hasChildren(parentIds));

        return ResultWrapper.getSuccessResultWrapper(treeNodes);
    }

    /**
    * 地域 新增
    * @param model 模型
    * @return ResultWrapper
    */
    @ApiOperation(value = "新增地域数据", notes = "新增地域数据")
    @PreAuthorize("hasAuthority('system_area_insert')")
    @OperateLogger(description = "新增地域数据",
            module = ModuleEnum.MODULE_AREA, operationType = OperationTypeEnum.INSERT, db = true)
    @Override
    public ResultWrapper<?> insert(SysAreaModel model) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用新增方法
        IService.insert(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("新增地域成功");
    }

    /**
    * 地域 修改
    * @param model 模型
    * @return ResultWrapper
    */
    @ApiOperation(value = "修改地域数据", notes = "修改地域数据")
    @PreAuthorize("hasAuthority('system_area_update')")
    @OperateLogger(description = "修改地域数据",
            module = ModuleEnum.MODULE_AREA, operationType = OperationTypeEnum.UPDATE, db = true)
    @Override
    public ResultWrapper<?> update(SysAreaModel model) {
        // 演示模式 不允许操作
        super.demoError();

        // 调用修改方法
        IService.update(model);
        return ResultWrapper.getSuccessResultWrapperByMsg("修改地域成功");
    }


    /**
    * 地域 删除
    * @param id ID
    * @return ResultWrapper
    */
    @ApiOperation(value = "删除地域数据", notes = "删除地域数据")
    @PreAuthorize("hasAuthority('system_area_delete')")
    @OperateLogger(description = "删除地域数据",
            module = ModuleEnum.MODULE_AREA, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> del(String id){
        // 演示模式 不允许操作
        super.demoError();

        IService.delete(id);
        return ResultWrapper.getSuccessResultWrapperByMsg("删除地域成功");
    }

    /**
    * 地域 批量删除
    * @param ids ID 数组
    * @return ResultWrapper
    */
    @ApiOperation(value = "批量删除地域数据", notes = "批量删除地域数据")
    @PreAuthorize("hasAuthority('system_area_delete')")
    @OperateLogger(description = "批量删除地域数据",
            module = ModuleEnum.MODULE_AREA, operationType = OperationTypeEnum.DELETE, db = true)
    @Override
    public ResultWrapper<?> delAll(String ids){
        // 演示模式 不允许操作
        super.demoError();

        String[] idArray = Convert.toStrArray(ids);
        IService.deleteAll(idArray);

        return ResultWrapper.getSuccessResultWrapperByMsg("批量删除地域成功");
    }


    // ==============================

    /**
     * 获得BeanMap集合
     * @param dataList 数据集合
     * @return List
     */
    private List<Map<String, Object>> getBeanMapList(List<SysArea> dataList) {
        List<Map<String, Object>> beanMapList = Lists.newArrayList();
        if(CollUtil.isEmpty(dataList)){
            return beanMapList;
        }

        // 转化为 BeanMap 处理数据
        for (SysArea sysArea : dataList) {
            Map<String, Object> beanToMap = BeanUtil.beanToMap(sysArea);

            // 获得排序
            String areaCode = sysArea.getAreaCode();
            int sort = 0;
            if(StringUtils.isNotEmpty(areaCode)){
                try {
                    sort = Integer.parseInt(areaCode);
                }catch (Exception ignored){}
            }
            beanToMap.put(SORT_FIELD, sort);

            beanMapList.add(beanToMap);
        }
        return beanMapList;
    }


    /**
     * 导入数据
     */
    //@ApiOperation(value = "获得json数据 查询数据", notes = "获得json数据 查询数据")
    //@GetMapping("/importJson")
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
