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
import org.opsli.common.annotation.EnableHotData;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.msg.CommonMsg;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.msg.CoreMsg;
import org.opsli.plugins.excel.ExcelUtil;
import org.opsli.plugins.excel.exception.ExcelPluginException;
import org.springframework.beans.factory.annotation.Autowired;
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
public abstract class BaseRestController <E extends ApiWrapper, T extends BaseEntity, S extends CrudServiceInterface<E,T>>{

    /** 开启热点数据状态 */
    protected boolean hotDataFlag = false;

    /** Model Clazz 类 */
    protected Class<E> modelClazz;
    /** Model 泛型游标 */
    private static final int modelIndex = 0;
    /** Entity Clazz 类 */
    protected Class<T> entityClazz;
    /** Entity 泛型游标 */
    private static final int entityIndex = 1;

    @Autowired(required = false)
    protected S IService;


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
            // 如果缓存没读到 则去数据库读
            model = WrapperUtil.transformInstance(IService.get(id),modelClazz);
            if(model != null){
                // 如果开启缓存 将数据库查询对象 存如缓存
                if(hotDataFlag){
                    // 这里会 同步更新到本地Ehcache 和 Redis缓存
                    // 如果其他服务器缓存也丢失了 则 回去Redis拉取
                    CacheUtil.put(id, model);
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
        try {
            List<E> modelList = ExcelUtil.readExcel(files.get(0), modelClazz);
            boolean ret = IService.insertBatch(modelList);
            if(!ret){
                throw new ExcelPluginException(CoreMsg.EXCEL_IMPORT_NO);
            }
            // 花费毫秒数
            long timerCount = timer.interval();
            // 提示信息
            String msg = StrUtil.format(CoreMsg.EXCEL_IMPORT_SUCCESS.getMessage(), modelList.size(), timerCount);
            // 导入成功
            return ResultVo.success(msg);
        } catch (ExcelPluginException e) {
            // 花费毫秒数
            long timerCount = timer.interval();
            log.error(e.getMessage(),e);
            // 提示信息
            String msg = StrUtil.format(CoreMsg.EXCEL_IMPORT_ERROR.getMessage(), e.getMessage(), timerCount);
            // 导入失败
            return ResultVo.error(CoreMsg.EXCEL_IMPORT_ERROR.getCode(), msg);
        }
    }

    /**
     * 下载导入模板
     * @param fileName 文件名称
     * @param response
     */
    protected ResultVo<?> importTemplate(String fileName, HttpServletResponse response){
        return this.excelExport(fileName+" - 模版 ",null, response);
    }

    /**
     * 导出
     * @param fileName 文件名称
     * @param queryWrapper 查询构建器
     * @param response
     */
    protected ResultVo<?> excelExport(String fileName, QueryWrapper<T> queryWrapper, HttpServletResponse response){
        try {
            List<E> modelList = Lists.newArrayList();
            if(queryWrapper != null){
                List<T> entityList = IService.findList(queryWrapper);
                // 转化类型
                modelList = WrapperUtil.transformInstance(entityList, modelClazz);
            }
            // 导出Excel
            ExcelUtil.writeExcel(response, modelList ,fileName,"sheet", modelClazz ,ExcelTypeEnum.XLSX);
            // 导出成功
            return ResultVo.success(CoreMsg.EXCEL_EXPORT_SUCCESS.getMessage());
        } catch (ExcelPluginException e) {
            log.error(e.getMessage(),e);
            // 提示信息
            String msg = StrUtil.format(CoreMsg.EXCEL_EXPORT_ERROR.getMessage(), e.getMessage());
            // 导出失败
            return ResultVo.error(CoreMsg.EXCEL_EXPORT_ERROR.getCode(),msg);
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
