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


import org.opsli.plugins.oss.enums.OssStorageType;

import java.io.File;
import java.io.InputStream;

/**
 * 云存储 (支持本地、七牛、阿里云、腾讯云、又拍云)
 *
 * @author Parker
 * @date 2021年4月30日16:12:45
 */
public interface OssStorageService {

    /**
     * 获得存储服务类型
     * @return type
     */
    OssStorageType getType();

    /**
     * 获得域名
     * @return String
     */
    String getDomain();

    /**
     * 文件上传
     * @param file    文件
     * @return  返回文件信息
     */
    BaseOssStorageService.FileAttr upload(File file);

    /**
     * 文件上传
     * @param inputStream    输入流
     * @param  suffix 后缀
     * @return 返回文件信息
     */
    BaseOssStorageService.FileAttr upload(InputStream inputStream, String suffix);

}
