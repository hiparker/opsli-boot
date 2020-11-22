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
package org.opsli.core.base.concroller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.annotation.hotdata.EnableHotData;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.msg.CommonMsg;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.utils.ExcelUtil;
import org.opsli.core.utils.UserUtil;
import org.opsli.plugins.excel.exception.ExcelPluginException;
import org.opsli.plugins.redis.RedisLockPlugins;
import org.opsli.plugins.redis.lock.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.base.concroller
 * @Author: Parker
 * @CreateTime: 2020-09-13 21:16
 * @Description: Controller 基类
 *
 * 默认 范型引用 子类的Service ， 为简单的CRUD做足准备
 *
 */
@Slf4j
@RestController
public abstract class BaseRestController <T extends BaseEntity, E extends ApiWrapper, S extends CrudServiceInterface<T,E>>{

    @Value("${opsli.enable-demo}")
    private boolean enableDemo;

    /** 开启热点数据状态 */
    protected boolean hotDataFlag = false;

    /** Entity Clazz 类 */
    protected Class<T> entityClazz;
    /** Entity 泛型游标 */
    private static final int entityIndex = 0;
    /** Model Clazz 类 */
    protected Class<E> modelClazz;
    /** Model 泛型游标 */
    private static final int modelIndex = 1;

    @Autowired(required = false)
    protected S IService;
    @Autowired
    private RedisLockPlugins redisLockPlugins;
    @Autowired
    private ExcelUtil excelUtil;

    /**
     * 默认 直接设置 传入数据的
     * 根据id 从缓存 直接查询 数据对象
     *
     * @param id
     * @return
     */
    @ModelAttribute
    public E get(@RequestParam(required=false) String id) {
        E model = null;
        if (StringUtils.isNotBlank(id)){
            // 如果开启缓存 先从缓存读
            if(hotDataFlag){
                model = WrapperUtil.transformInstance(
                        CacheUtil.get(id, entityClazz),modelClazz);
                if(model != null){
                    return model;
                }
            }
            // 防止缓存穿透判断
            boolean hasNilFlag = false;
            // 如果开启缓存 防止缓存穿透判断
            if(hotDataFlag){
                hasNilFlag = CacheUtil.hasNilFlag("get:" + id);
            }
            if(!hasNilFlag){
                // 锁凭证 redisLock 贯穿全程
                RedisLock redisLock = new RedisLock();
                redisLock.setLockName("getLock:"+id)
                        .setAcquireTimeOut(3000L)
                        .setLockTimeOut(10000L);
                // 这里增加分布式锁 防止缓存击穿
                if(hotDataFlag){
                    try {
                        // 加锁
                        redisLock = redisLockPlugins.tryLock(redisLock);
                        if(redisLock == null){
                            throw new ServiceException(CoreMsg.CACHE_PUNCTURE_EXCEPTION);
                        }

                        // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
                        model = WrapperUtil.transformInstance(
                                CacheUtil.get(id, entityClazz),modelClazz);
                        if(model != null){
                            return model;
                        }

                        // 如果缓存没读到 则去数据库读
                        model = WrapperUtil.transformInstance(IService.get(id),modelClazz);
                    }catch (Exception e){
                        log.error(e.getMessage(), e);
                    }finally {
                        // 释放锁
                        redisLockPlugins.unLock(redisLock);
                        redisLock = null;
                    }
                }else{
                    // 如果缓存没读到 则去数据库读
                    model = WrapperUtil.transformInstance(IService.get(id),modelClazz);
                }

                // 获得数据后处理
                if(model != null){
                    // 如果开启缓存 将数据库查询对象 存如缓存
                    if(hotDataFlag){
                        // 这里会 同步更新到本地Ehcache 和 Redis缓存
                        // 如果其他服务器缓存也丢失了 则 回去Redis拉取
                        CacheUtil.put(id, model);
                    }
                }else {
                    // 设置空变量 用于防止穿透判断
                    CacheUtil.putNilFlag("get:" + id);
                }
            }
        }
        if (model == null){
            try {
                // 创建泛型对象
                model = this.createModel();
            }catch (Exception e){
                log.error(CommonMsg.EXCEPTION_CONTROLLER_MODEL.toString()+" : {}",e.getMessage());
                throw new ServiceException(CommonMsg.EXCEPTION_CONTROLLER_MODEL);
            }
        }
        return model;
    }

    /**
     * Excel 导入
     * @param request
     * @return
     */
    protected ResultVo<?> excelImport(MultipartHttpServletRequest request){
        // 计时器
        TimeInterval timer = DateUtil.timer();
        Iterator<String> itr = request.getFileNames();
        String uploadedFile = itr.next();
        List<MultipartFile> files = request.getFiles(uploadedFile);
        if (CollectionUtils.isEmpty(files)) {
            // 请选择文件
            return ResultVo.error(CoreMsg.EXCEL_FILE_NULL.getCode(),
                    CoreMsg.EXCEL_FILE_NULL.getMessage());
        }
        ResultVo<?> resultVo;
        String msgInfo = "";
        try {
            List<E> modelList = excelUtil.readExcel(files.get(0), modelClazz);
            boolean ret = IService.insertBatch(modelList);
            if(!ret){
                throw new ExcelPluginException(CoreMsg.EXCEL_IMPORT_NO);
            }
            // 花费毫秒数
            long timerCount = timer.interval();
            // 提示信息
            msgInfo = StrUtil.format(CoreMsg.EXCEL_IMPORT_SUCCESS.getMessage(), timerCount);
            // 导出成功
            resultVo = ResultVo.success(msgInfo);
            resultVo.setCode(CoreMsg.EXCEL_IMPORT_SUCCESS.getCode());
        } catch (ExcelPluginException e) {
            // 花费毫秒数
            long timerCount = timer.interval();
            // 提示信息
            msgInfo = StrUtil.format(CoreMsg.EXCEL_IMPORT_ERROR.getMessage(), timerCount, e.getMessage());
            // 导入失败
            resultVo = ResultVo.error(CoreMsg.EXCEL_IMPORT_ERROR.getCode(), msgInfo);
        }
        // 记录导出日志
        log.info(msgInfo);
        return resultVo;
    }

    /**
     * 下载导入模板
     * @param fileName 文件名称
     * @param response
     */
    protected ResultVo<?> importTemplate(String fileName, HttpServletResponse response){
        return this.excelExport(fileName + " 模版 ",null, response);
    }

    /**
     * 导出
     * @param fileName 文件名称
     * @param queryWrapper 查询构建器
     * @param response
     */
    protected ResultVo<?> excelExport(String fileName, QueryWrapper<T> queryWrapper, HttpServletResponse response){
        // 计时器
        TimeInterval timer = DateUtil.timer();
        String msgInfo = "";
        ResultVo<?> resultVo;
        try {
            List<E> modelList = Lists.newArrayList();
            if(queryWrapper != null){
                List<T> entityList = IService.findList(queryWrapper);
                // 转化类型
                modelList = WrapperUtil.transformInstance(entityList, modelClazz);
            }
            // 导出Excel
            excelUtil.writeExcel(response, modelList ,fileName,"sheet", modelClazz ,ExcelTypeEnum.XLSX);
            // 花费毫秒数
            long timerCount = timer.interval();
            // 提示信息
            msgInfo = StrUtil.format(CoreMsg.EXCEL_EXPORT_SUCCESS.getMessage(), modelList.size(), timerCount);
            // 导出成功
            resultVo = ResultVo.success(msgInfo);
            resultVo.setCode(CoreMsg.EXCEL_EXPORT_SUCCESS.getCode());
        } catch (ExcelPluginException e) {
            // 花费毫秒数
            long timerCount = timer.interval();
            // 提示信息
            msgInfo = StrUtil.format(CoreMsg.EXCEL_EXPORT_ERROR.getMessage(), timerCount, e.getMessage());
            // 导出失败
            resultVo = ResultVo.error(CoreMsg.EXCEL_EXPORT_ERROR.getCode(), msgInfo);
        }
        // 记录导出日志
        log.info(msgInfo);
        return resultVo;
    }

    /**
     * 演示模式
     */
    protected void demoError(){
        UserModel user = UserUtil.getUser();
        // 演示模式 不允许操作 （超级管理员可以操作）
        if(enableDemo && !UserUtil.SUPER_ADMIN.equals(user.getUsername())){
            throw new ServiceException(CoreMsg.EXCEPTION_ENABLE_DEMO);
        }
    }

    // =================================================

    @PostConstruct
    public void init(){
        try {
            this.modelClazz = this.getModelClass();
            this.entityClazz = this.getEntityClass();
            Class<?> serviceClazz = IService.getServiceClazz();
            // 判断是否开启热点数据
            if(serviceClazz != null){
                EnableHotData annotation = serviceClazz.getAnnotation(EnableHotData.class);
                if(annotation != null){
                    this.hotDataFlag = true;
                }
            }

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }


    /**
     * 创建包装类泛型对象
     * @return
     */
    private E createModel() {
        try {
            Class<E> modelClazz = this.modelClazz;
            return modelClazz.newInstance();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }


    /**
     * 获得 泛型 Clazz
     * @return
     */
    private Class<E> getModelClass(){
        Class<E> tClass = null;
        Type typeArgument = TypeUtil.getTypeArgument(getClass().getGenericSuperclass(), modelIndex);
        if(typeArgument != null){
            tClass = (Class<E>) typeArgument;
        }
        return tClass;
    }

    /**
     * 获得 泛型 Clazz
     * @return
     */
    private Class<T> getEntityClass(){
        Class<T> tClass = null;
        Type typeArgument = TypeUtil.getTypeArgument(getClass().getGenericSuperclass(), entityIndex);
        if(typeArgument != null){
            tClass = (Class<T>) typeArgument;
        }
        return tClass;
    }

}
