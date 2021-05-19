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
package org.opsli.plugins.excel;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.plugins.excel.exception.ExcelPluginException;
import org.opsli.plugins.excel.listener.BatchExcelListener;
import org.opsli.plugins.excel.listener.ExcelListener;
import org.opsli.plugins.excel.msg.ExcelMsg;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * ExcelUtil
 * 基于easyExcel的开源框架，poi版本3.17
 * BeanCopy ExcelException 属于自定义数据，属于可自定义依赖
 * 工具类尽可能还是需要减少对其他java的包的依赖
 *
 * @author Parker
 * @date 2020-09-16 11:47
 */
@Slf4j
public class ExcelPlugin {

    /**
     * 读取 Excel(多个 sheet)
     * 将多sheet合并成一个list数据集，通过自定义ExcelReader继承AnalysisEventListener
     * 重写invoke doAfterAllAnalysed方法
     * getExtendsBeanList 主要是做Bean的属性拷贝 ，可以通过ExcelReader中添加的数据集直接获取
     * @param excel    文件
     * @param rowModel 实体类映射，继承 BaseRowModel 类
     * @return Excel 数据 list
     */
    public <T> List<T> readExcel(MultipartFile excel,Class<T>  rowModel)
            throws ExcelPluginException {
        return readExcel(excel, rowModel, null, 1);
    }

    /**
     * 读取某个 sheet 的 Excel
     * @param excel    文件
     * @param rowModel 实体类映射，继承 BaseRowModel 类
     * @param sheetName  sheet 的序号 从1开始
     * @return Excel 数据 list
     */
    public <T> List<T> readExcel(MultipartFile excel, Class<T>  rowModel, String sheetName)
            throws ExcelPluginException{
        return readExcel(excel, rowModel, sheetName, 1);
    }

    /**
     * 读取某个 sheet 的 Excel
     * @param excel       文件
     * @param rowModel    实体类映射，继承 BaseRowModel 类
     * @param sheetName     sheet 的序号 从1开始
     * @param headLineNum 表头行数，默认为1
     * @return Excel 数据 list
     */
    public <T> List<T> readExcel(MultipartFile excel, Class<T>  rowModel, String sheetName,
                                                             int headLineNum) throws ExcelPluginException {
        ExcelListener<T> excelListener = new ExcelListener<>();
        InputStream inputStream = null;
        try{
            if(null != excel){
                inputStream = excel.getInputStream();
            }
        }catch (IOException e){
            log.error(e.getMessage(),e);
        }
        if(null == inputStream){
            return Lists.newArrayList();
        }

        ExcelReader excelReader = EasyExcel.read(inputStream, rowModel, excelListener).build();
        if (excelReader == null) {
            return Lists.newArrayList();
        }
        ReadSheet readSheet;
        if(StringUtils.isEmpty(sheetName)){
            readSheet = EasyExcel.readSheet().build();
        }else{
            readSheet = EasyExcel.readSheet(sheetName).build();
        }
        readSheet.setHeadRowNumber(headLineNum);
        excelReader.read(readSheet);
        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();

        return getExtendsBeanList(excelListener.getDataList(), rowModel);
    }


    // ==================================================================================


    /**
     * 读取 Excel(多个 sheet)
     * 将多sheet合并成一个list数据集，通过自定义ExcelReader继承AnalysisEventListener
     * 重写invoke doAfterAllAnalysed方法
     * getExtendsBeanList 主要是做Bean的属性拷贝 ，可以通过ExcelReader中添加的数据集直接获取
     * @param excel    文件
     * @param rowModel 实体类映射，继承 BaseRowModel 类
     * @param batchExcelListener 监听器
     */
    public <T> void readExcelByListener(MultipartFile excel,Class<T>  rowModel,
                                           BatchExcelListener<T> batchExcelListener)
            throws ExcelPluginException {
        readExcelByListener(excel, rowModel, null, 1, batchExcelListener);
    }

    /**
     * 读取某个 sheet 的 Excel
     * @param excel    文件
     * @param rowModel 实体类映射，继承 BaseRowModel 类
     * @param sheetName  sheet 的序号 从1开始
     * @param batchExcelListener 监听器
     */
    public <T> void readExcelByListener(MultipartFile excel, Class<T>  rowModel, String sheetName,
                                           BatchExcelListener<T> batchExcelListener)
            throws ExcelPluginException{
        readExcelByListener(excel, rowModel, sheetName, 1, batchExcelListener);
    }

    /**
     * 读取某个 sheet 的 Excel
     * @param excel       文件
     * @param rowModel    实体类映射，继承 BaseRowModel 类
     * @param sheetName     sheet 的序号 从1开始
     * @param headLineNum 表头行数，默认为1
     * @param batchExcelListener 监听器
     */
    public <T> void readExcelByListener(MultipartFile excel, Class<T>  rowModel, String sheetName,
                                 int headLineNum, BatchExcelListener<T> batchExcelListener) throws ExcelPluginException {
        if(null == batchExcelListener){
            return;
        }
        InputStream inputStream = null;
        try{
            if(null != excel){
                inputStream = excel.getInputStream();
            }
        }catch (IOException e){
            log.error(e.getMessage(),e);
        }
        if(null == inputStream){
            return;
        }

        ExcelReader excelReader = EasyExcel.read(inputStream, rowModel, batchExcelListener).build();
        if (excelReader == null) {
            return;
        }
        ReadSheet readSheet;
        if(StringUtils.isEmpty(sheetName)){
            readSheet = EasyExcel.readSheet().build();
        }else{
            readSheet = EasyExcel.readSheet(sheetName).build();
        }
        readSheet.setHeadRowNumber(headLineNum);
        excelReader.read(readSheet);
        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();
    }

    /**
     * 导出 Excel ：一个 sheet，带表头
     * 自定义WriterHandler 可以定制行列数据进行灵活化操作
     * @param response  HttpServletResponse
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     */
    public <T> void writeExcel(HttpServletResponse response, List<T> list,
                                                           String fileName, String sheetName,
                                                           Class<T> classType, ExcelTypeEnum excelTypeEnum)
            throws ExcelPluginException{

        if(sheetName == null || "".equals(sheetName)){
            sheetName = "sheet1";
        }

        fileName = fileName+"-"+DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
        OutputStream outputStream = getOutputStream(fileName, response, excelTypeEnum);
        ExcelWriter excelWriter = EasyExcel.write(outputStream, classType).build();
        WriteSheet writeSheet = EasyExcel.writerSheet(1, sheetName).build();
        writeSheet.setRelativeHeadRowIndex(0);
        excelWriter.write(list,writeSheet);

        // 关闭流
        try {
            excelWriter.finish();
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }


    /**
     * 导出文件时为Writer生成OutputStream
     */
    private OutputStream getOutputStream(String fileName, HttpServletResponse response,
                                                ExcelTypeEnum excelTypeEnum)
            throws ExcelPluginException{
        //创建本地文件
        String filePath = fileName + excelTypeEnum.getValue();
        try {
            fileName = new String(filePath.getBytes(), StandardCharsets.ISO_8859_1);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setHeader("Cache-Control", "no-store, no-cache");
            return response.getOutputStream();
        } catch (IOException e) {
            // 创建文件失败
            throw new ExcelPluginException(ExcelMsg.EXCEPTION_CREATE_ERROR);
        }
    }

    /**
     * 返回 ExcelReader
     * @param excel         需要解析的 Excel 文件
     * @param excelListener new ExcelListener()
     */
    private ExcelReader getReader(MultipartFile excel,
                                         ExcelListener<?> excelListener) throws ExcelPluginException{
        String fileName = excel.getOriginalFilename();
        if (fileName == null ) {
            // 文件格式错误
            throw new ExcelPluginException(ExcelMsg.EXCEPTION_FILE_FORMAT);
        }
        if (!fileName.toLowerCase().endsWith(ExcelTypeEnum.XLS.getValue()) && !fileName.toLowerCase()
                .endsWith(ExcelTypeEnum.XLSX.getValue())) {
            // 文件格式错误
            throw new ExcelPluginException(ExcelMsg.EXCEPTION_FILE_FORMAT);
        }
        InputStream inputStream;
        try {
            inputStream = excel.getInputStream();
            return EasyExcel.read(inputStream, excelListener).build();
        } catch (IOException e) {
            //do something
            log.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 利用BeanCopy转换list
     */
    public <T> List<T> getExtendsBeanList(List<?> list,Class<T> typeClazz){
        return WrapperUtil.transformInstance(list, typeClazz);
    }
}
