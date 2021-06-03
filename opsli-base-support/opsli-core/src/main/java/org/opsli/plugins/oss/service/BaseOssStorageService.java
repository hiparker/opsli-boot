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
package org.opsli.plugins.oss.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;

/**
 * 云存储 (支持本地、又拍云...持续更新中)
 *
 * @author Parker
 * @date 2021年4月30日16:12:45
 */
public abstract class BaseOssStorageService implements OssStorageService {

    /** 文件夹前缀 */
    public static final String FOLDER_PREFIX = "/";
    private static final String FOLDER_WINDOWS_PREFIX = "\\";

    /**
     * 获得文件信息
     * @param file 文件
     * @return FileAttr
     */
    protected FileAttr getFileAttr(File file){
        FileAttr fileAttr = new FileAttr();
        fileAttr.setName(FileUtil.getName(file));
        fileAttr.setSize(FileUtil.size(file));
        fileAttr.setPrefix(FileUtil.getPrefix(file));
        fileAttr.setSuffix(FileUtil.getSuffix(file));
        fileAttr.setNameAndSuffix(fileAttr.getName()+"."+fileAttr.getSuffix());
        // 生成随机 文件名称
        // 当前时间戳 + 文件名 hash + 文件大小 + 5位随机码

        // 当前时间戳
        long currentTimeMillis = System.currentTimeMillis();
        // 文件名 HashCode
        int fileNameHashCode = fileAttr.getName().hashCode();
        // 随机字符串
        String randomString = RandomUtil.randomStringUpper(5);
        // 随机文件名
        String randomFileName = currentTimeMillis + fileNameHashCode + fileAttr.getSize() + randomString;

        fileAttr.setRandomFileName(randomFileName);
        fileAttr.setRandomFileNameAndSuffix(randomFileName+"."+fileAttr.getSuffix());

        return fileAttr;
    }


    /**
     * 获得文件信息
     * @param inputStream 文件
     * @param suffix 后缀
     * @return FileAttr
     */
    protected FileAttr getFileAttr(InputStream inputStream, String suffix){
        FileAttr fileAttr = new FileAttr();
        fileAttr.setSuffix(suffix);

        // 生成随机 文件名称
        // 当前时间戳 + 文件名 hash + 5位随机数字码 + 5位随机码

        // 当前时间戳
        long currentTimeMillis = System.currentTimeMillis();
        // 文件名 HashCode
        int fileNameHashCode = inputStream.hashCode();
        // 随机字符串
        String randomString = RandomUtil.randomStringUpper(5);
        // 随机数
        String randomNumbers = RandomUtil.randomNumbers(5);
        // 随机文件名
        String randomFileName = currentTimeMillis + fileNameHashCode + randomNumbers + randomString;

        fileAttr.setRandomFileName(randomFileName);
        fileAttr.setRandomFileNameAndSuffix(randomFileName+"."+fileAttr.getSuffix());

        return fileAttr;
    }

    /**
     * 处理 Path 路径
     * @param path 路径
     * @return String
     */
    protected String handlePath(String path){
        return this.handlePath(path, true);
    }

    /**
     * 处理 Path 路径
     * @param path 路径
     * @return String
     */
    protected String handlePath(String path, boolean isHandleFirst){
        if(StringUtils.isEmpty(path)){
            return path;
        }

        if(isHandleFirst){
            // 如果 第一位不是 / 则加 /
            path = StrUtil.prependIfMissing(path, FOLDER_PREFIX);
        }

        // 如果最后一位 是 / 则减 /
        char lastChar = path.charAt(path.length()-1);
        if(path.length() > 1 &&
                StringUtils.equals(FOLDER_PREFIX, Convert.toStr(lastChar))){
            path = StringUtils.substring(path, 0, path.length()-1);
        }

        // 针对 windows 特殊路径 进行统一翻转
        return StringUtils.replace(path, FOLDER_WINDOWS_PREFIX, FOLDER_PREFIX);
    }


    // =============================

    @Data
    public static class FileAttr{

        /** 大小 */
        private long size;

        /** 文件名 */
        private String name;

        /** 文件名 包含后缀 */
        private String nameAndSuffix;

        /** 前缀 */
        private String prefix;

        /** 后缀 */
        private String suffix;

        /** 随机唯一文件名 */
        private String randomFileName;

        /** 随机唯一文件名 包含后缀 */
        private String randomFileNameAndSuffix;

        /** 文件存储 */
        private String fileStoragePath;

    }

}
