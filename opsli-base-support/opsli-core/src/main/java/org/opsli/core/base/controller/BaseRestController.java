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
package org.opsli.core.base.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.constants.RedisConstants;
import org.opsli.common.constants.TreeConstants;
import org.opsli.common.enums.ExcelOperate;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.UniqueStrGeneratorUtils;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.core.base.entity.HasChildren;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.core.cache.CacheUtil;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.persistence.querybuilder.WebQueryBuilder;
import org.opsli.core.utils.ExcelUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.plugins.excel.exception.ExcelPluginException;
import org.opsli.plugins.excel.listener.BatchExcelListener;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 默认 范型引用 子类的Service ， 为简单的CRUD做足准备
 *
 * @author Parker
 * @date 2020-09-15
 */
@Slf4j
@RestController
public abstract class BaseRestController <T extends BaseEntity, E extends ApiWrapper, S extends CrudServiceInterface<T,E>>{

    /** 凭证 10分钟失效 */
    private static final int CERTIFICATE_EXPIRED_MINUTE = 10;

    /** 配置类 */
    @Autowired
    protected GlobalProperties globalProperties;

    /** 子类Service */
    @Autowired(required = false)
    protected S IService;

    /** Redis 类 */
    @Autowired
    private RedisPlugin redisPlugin;

    /**
     * 默认 直接设置 传入数据的
     * 根据id 从缓存 直接查询 数据对象
     *
     * @param id id
     * @return E
     */
    @ModelAttribute
    public E get(@RequestParam(required=false) String id) {
        if(StrUtil.isEmpty(id)){
            return null;
        }
        return IService.get(id);
    }

    /**
     * Excel 导入
     * @param request request
     * @return ResultWrapper
     */
    protected ResultWrapper<?> importExcel(MultipartHttpServletRequest request){
        // 计时器
        TimeInterval timer = DateUtil.timer();
        Iterator<String> itr = request.getFileNames();
        String uploadedFile = itr.next();
        List<MultipartFile> files = request.getFiles(uploadedFile);
        if (CollectionUtils.isEmpty(files)) {
            // 请选择文件
            return ResultWrapper.getCustomResultWrapper(CoreMsg.EXCEL_FILE_NULL);
        }
        ResultWrapper<?> resultVo ;
        String msgInfo;
        try {
            UserModel user = UserUtil.getUser();
            Date currDate = DateUtil.date();

            // 导入优化为 监听器 模式 超过一定阈值直接释放资源 防止导入数据导致系统 OOM
            ExcelUtil.getInstance().readExcelByListener(files.get(0), IService.getModelClass(), new BatchExcelListener<E>() {
                @Override
                public void saveData(List<E> dataList) {
                    // 处理字典数据
                    List<E> disposeData = ExcelUtil.getInstance().handleDatas(dataList, IService.getModelClass(), ExcelOperate.READ);
                    // 手动赋值 必要数据 防止频繁开启Redis网络IO
                    for (E model : disposeData) {
                        model.setIzManual(true);
                        model.setCreateBy(user.getId());
                        model.setUpdateBy(user.getId());
                        model.setCreateTime(currDate);
                        model.setUpdateTime(currDate);
                    }
                    // 数据库插入数据
                    IService.insertBatch(disposeData);
                }
            });

            // 花费毫秒数
            long timerCount = timer.interval();
            // 提示信息
            msgInfo = StrUtil.format(CoreMsg.EXCEL_IMPORT_SUCCESS.getMessage(), DateUtil.formatBetween(timerCount));
            // 导出成功
            resultVo = ResultWrapper.getSuccessResultWrapper(msgInfo);
            resultVo.setCode(CoreMsg.EXCEL_IMPORT_SUCCESS.getCode());

        } catch (ExcelPluginException e) {
            // 花费毫秒数
            long timerCount = timer.interval();
            // 提示信息
            msgInfo = StrUtil.format(CoreMsg.EXCEL_IMPORT_ERROR.getMessage(), DateUtil.formatBetween(timerCount),
                    e.getMessage());
            // 导入失败
            resultVo = ResultWrapper.getCustomResultWrapper(
                    CoreMsg.EXCEL_IMPORT_ERROR.getCode(), msgInfo);
        }
        // 记录导出日志
        log.info(msgInfo);
        return resultVo;
    }

    /**
     * Excel 导出认证
     *
     * @param type 类型（Excel导出、Excel模版导出）
     * @param subName 主题
     * @param request request
     * @return Optional<String>
     */
    protected Optional<String> excelExportAuth(String type, String subName, HttpServletRequest request){
        // 封装缓存数据
        ExcelExportCache exportCache;
        if(ExcelExportCache.EXCEL_EXPORT.equals(type)){
            // 异常检测
            QueryBuilder<T> queryBuilder = new WebQueryBuilder<>(IService.getEntityClass(), request.getParameterMap());
            QueryWrapper<T> queryWrapper = queryBuilder.build();
            // 导出数量限制 -1 为无限制
            Integer exportMaxCount = globalProperties.getExcel().getExportMaxCount();
            if(exportMaxCount != null && exportMaxCount > -1){
                // 获得数量 大于 阈值 禁止导出， 防止OOM
                long count = IService.count(queryWrapper);
                if(count > exportMaxCount){
                    String maxError = StrUtil.format(CoreMsg.EXCEL_HANDLE_MAX.getMessage(), count,
                            exportMaxCount);
                    // 超出最大导出数量
                    throw new ExcelPluginException(CoreMsg.EXCEL_HANDLE_MAX.getCode(), maxError);
                }
            }

            // 封装缓存数据
            exportCache = ExcelExportCache.builder()
                    .subName(subName)
                    .parameterMapStr(JSONUtil.toJsonStr(request.getParameterMap()))
                    .type(type)
                    .build();
        }else if(ExcelExportCache.EXCEL_IMPORT_TEMPLATE_EXPORT.equals(type)){
            // 封装缓存数据
            exportCache = ExcelExportCache.builder()
                    .subName(subName+ " 模版 ")
                    .parameterMapStr(JSONUtil.toJsonStr(request.getParameterMap()))
                    .type(type)
                    .build();
        }else {
            return Optional.empty();
        }


        // 缓存Key
        Long increment = redisPlugin
                .increment(CacheUtil.formatKey(RedisConstants.PREFIX_TMP_EXCEL_EXPORT_NUM_NAME));
        String certificate = UniqueStrGeneratorUtils.generator(increment);

        // 缓存Key
        String certificateCacheKeyTmp = CacheUtil.formatKey(
                RedisConstants.PREFIX_TMP_EXCEL_EXPORT_NAME + certificate);
        redisPlugin.put(certificateCacheKeyTmp, exportCache, CERTIFICATE_EXPIRED_MINUTE, TimeUnit.MINUTES);

        return Optional.of(certificate);
    }

    /**
     * 导出 Excel
     *
     * @param response response
     */
    protected void excelExport(String certificate, HttpServletResponse response){
        // 缓存Key
        String certificateCacheKeyTmp = CacheUtil.formatKey(
                RedisConstants.PREFIX_TMP_EXCEL_EXPORT_NAME + certificate);
        Object cacheObj = redisPlugin.get(certificateCacheKeyTmp);
        ExcelExportCache cache = Convert.convert(ExcelExportCache.class, cacheObj);
        if(cache == null){
            return;
        }

        // 主题名称
        String subName = cache.getSubName();

        List<E> modelList = null;

        // 如果导出Excel 需要查询数据
        if(ExcelExportCache.EXCEL_EXPORT.equals(cache.getType())){
            // 参数Map
            Map<String, String[]> parameterMap = new HashMap<>();
            JSONObject jsonObject = JSONUtil.parseObj(cache.getParameterMapStr());
            jsonObject.forEach((k, v) -> {
                JSONArray values = (JSONArray) v;
                String[] parameters = (String[]) values.toArray(String.class);
                parameterMap.put(k, parameters);
            });

            QueryBuilder<T> queryBuilder = new WebQueryBuilder<>(IService.getEntityClass(), parameterMap);
            QueryWrapper<T> queryWrapper = queryBuilder.build();

            List<T> entityList = IService.findList(queryWrapper);
            // 转化类型
            modelList = WrapperUtil.transformInstance(entityList, IService.getModelClass());

        }

        // 导出Excel
        ExcelUtil.getInstance().writeExcel(
                response, modelList, subName,"sheet", IService.getModelClass() ,ExcelTypeEnum.XLSX);

        // 删除凭证
        redisPlugin.del(certificateCacheKeyTmp);
    }


    /**
     * 演示模式
     */
    protected void demoError(){
        UserModel user = UserUtil.getUser();
        // 演示模式 不允许操作 （超级管理员可以操作）
        if(globalProperties.isEnableDemo() &&
                !StringUtils.equals(UserUtil.SUPER_ADMIN, user.getUsername())){
            throw new ServiceException(CoreMsg.EXCEPTION_ENABLE_DEMO);
        }
    }


    /**
     * 处理是否包含子集
     * @param treeNodes 树节点
     */
    protected List<Tree<Object>> handleTreeHasChildren(List<Tree<Object>> treeNodes,
                                                       Function<Set<String>, List<HasChildren>> callback) {
        if(CollUtil.isEmpty(treeNodes) || callback == null){
            return treeNodes;
        }

        Set<String> parentIds = Sets.newHashSet();
        for (Tree<Object> treeNode : treeNodes) {
            parentIds.add(Convert.toStr(treeNode.getId()));
        }

        // 数据排查是否存在下级
        List<HasChildren> hasChildrenList = callback.apply(parentIds);
        if(CollUtil.isEmpty(hasChildrenList)){
            hasChildrenList = ListUtil.empty();
        }

        // 字典
        Map<String, Boolean> hasChildrenDict = Maps.newHashMap();
        for (HasChildren hasChildren : hasChildrenList) {
            if (hasChildren.getCount() != null && hasChildren.getCount() > 0) {
                hasChildrenDict.put(hasChildren.getParentId(), true);
            }
        }

        // 处理节点
        this.handleTreeHasChildren(treeNodes, hasChildrenDict);

        return treeNodes;
    }

    /**
     * 处理 树节点是否 有子节点
     * @param treeNodes 树节点
     * @param hasChildrenDict 字典树
     */
    private void handleTreeHasChildren(List<Tree<Object>> treeNodes,
                                       Map<String, Boolean> hasChildrenDict){

        for (Tree<Object> treeNode : treeNodes) {
            Boolean tmpFlag = hasChildrenDict.get(Convert.toStr(treeNode.getId()));
            if (tmpFlag != null && tmpFlag) {
                treeNode.putExtra(TreeConstants.IS_LEAF, false);
                treeNode.putExtra(TreeConstants.HAS_CHILDREN, true);
            }else {
                treeNode.putExtra(TreeConstants.IS_LEAF, true);
                treeNode.putExtra(TreeConstants.HAS_CHILDREN, false);
            }

            // 如果不为空 则继续递归处理
            if(CollUtil.isNotEmpty(treeNode.getChildren())){
                handleTreeHasChildren(treeNode.getChildren(), hasChildrenDict);
            }
        }
    }

    // =================================================


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ExcelExportCache implements Serializable {

        private static final long serialVersionUID = 1L;

        public final static String EXCEL_IMPORT_TEMPLATE_EXPORT = "import-template-export";

        public final static String EXCEL_EXPORT = "export";


        /** 主题名 */
        private String subName;

        /** 类型 */
        private String type;

        /** 请求参数Map */
        private String parameterMapStr;

    }
}
