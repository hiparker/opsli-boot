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
package org.opsli.common.utils;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.msg.CommonMsg;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * OutputStream 处理
 *
 * @author Parker
 * @date 2021-01-05 14:26
 */
@Slf4j
public final class OutputStreamUtil {

    /**
     * 导出文件时为Writer生成OutputStream
     */
    public static OutputStream getOutputStream(String fileName, HttpServletResponse response)
            throws ServiceException {
        try {
            fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setHeader("Cache-Control", "no-store, no-cache");
            return response.getOutputStream();
        } catch (IOException e) {
            // 创建文件失败
            throw new ServiceException(CommonMsg.EXCEPTION_CREATE_FILE_ERROR);
        }
    }

    /**
     * 返回异常值
     */
    public static void exceptionResponse(String msg, HttpServletResponse response){
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8;");
            PrintWriter writer = response.getWriter();
            writer.write(
                    "<script type=\"text/javascript\">alert('"+msg+"');</script>");
            writer.flush();
            // 关闭流
            IoUtil.close(writer);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }


    // ==========================

    private OutputStreamUtil(){}

}
