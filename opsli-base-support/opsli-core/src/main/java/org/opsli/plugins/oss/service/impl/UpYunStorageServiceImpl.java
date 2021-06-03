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
package org.opsli.plugins.oss.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.upyun.RestManager;
import com.upyun.UpException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.opsli.core.utils.GlobalPropertiesUtil;
import org.opsli.core.utils.ValidatorUtil;
import org.opsli.plugins.oss.conf.LocalConfigFactory;
import org.opsli.plugins.oss.conf.UpYunConfigFactory;
import org.opsli.plugins.oss.enums.OssStorageType;
import org.opsli.plugins.oss.exception.StoragePluginException;
import org.opsli.plugins.oss.msg.OssMsg;
import org.opsli.plugins.oss.service.BaseOssStorageService;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * 又拍云文件上传
 *
 * @author Parker
 * @date 2021年4月30日14:09:08
 */
@Slf4j
public class UpYunStorageServiceImpl extends BaseOssStorageService {

    @Override
    public OssStorageType getType() {
        return OssStorageType.UP_YUN;
    }

    @Override
    public String getDomain() {
        // 获得配置信息
        UpYunConfigFactory.UpYunConfig config = UpYunConfigFactory.INSTANCE.getConfig();
        return config.getDomain();
    }

    @Override
    public FileAttr upload(File file) {
        // 非空判断
        if(FileUtil.isEmpty(file)){
            return new FileAttr();
        }

        // 获得配置信息
        UpYunConfigFactory.UpYunConfig config = UpYunConfigFactory.INSTANCE.getConfig();

        // 验证对象
        ValidatorUtil.verify(config);

        // 当前时间戳
        long currentTimeMillis = System.currentTimeMillis();

        // 当前时间
        Date currDate = DateUtil.date(currentTimeMillis);

        // 静态路径前缀 默认为空
        String pathPrefix = StrUtil.isNotEmpty(config.getPathPrefix())?config.getPathPrefix():"";

        // 文件夹名称
        String folderName = DateUtil.format(currDate, "yyyyMMdd");

        // 包 名称
        String packageName = StrUtil.appendIfMissing(
                super.handlePath(pathPrefix) + super.handlePath(folderName),
                BaseOssStorageService.FOLDER_PREFIX);

        // 文件属性
        FileAttr fileAttr = super.getFileAttr(file);
        // 设置文件路径
        fileAttr.setFileStoragePath(packageName + fileAttr.getName() + fileAttr.getSuffix());

        try {
            // 获得又拍云 服务
            RestManager restManager = this.getService(config);
            restManager.writeFile(fileAttr.getFileStoragePath(), file, null);
        }catch (IOException | UpException e){
            // 上传文件失败，请检查配置信息
            throw new StoragePluginException(OssMsg.EXCEPTION_UPLOAD_ERROR, e);
        }

        return fileAttr;
    }

    @Override
    public FileAttr upload(InputStream inputStream, String suffix) {
        // 获得配置信息
        UpYunConfigFactory.UpYunConfig config = UpYunConfigFactory.INSTANCE.getConfig();

        // 验证对象
        ValidatorUtil.verify(config);

        // 当前时间戳
        long currentTimeMillis = System.currentTimeMillis();

        // 当前时间
        Date currDate = DateUtil.date(currentTimeMillis);

        // 静态路径前缀 默认为空
        String pathPrefix = StrUtil.isNotEmpty(config.getPathPrefix())?config.getPathPrefix():"";

        // 文件夹名称
        String folderName = DateUtil.format(currDate, "yyyyMMdd");

        // 包 名称
        String packageName = StrUtil.appendIfMissing(
                super.handlePath(pathPrefix) + super.handlePath(folderName),
                BaseOssStorageService.FOLDER_PREFIX);

        // 文件属性
        FileAttr fileAttr = super.getFileAttr(inputStream, suffix);
        // 设置文件路径
        fileAttr.setFileStoragePath(
                config.getDomain() + packageName + fileAttr.getRandomFileNameAndSuffix());

        try {
            // 获得又拍云 服务
            RestManager restManager = this.getService(config);
            Response response = restManager.writeFile(packageName + fileAttr.getRandomFileNameAndSuffix(),
                    inputStream, null);
            int code = response.code();
            if(HttpStatus.UNAUTHORIZED.value() == code){
                // 权限认证异常
                throw new StoragePluginException(OssMsg.EXCEPTION_UPLOAD_AUTH_ERROR);
            }
        }catch (IOException | UpException e){
            // 上传文件失败，请检查配置信息
            throw new StoragePluginException(OssMsg.EXCEPTION_UPLOAD_ERROR, e);
        }

        return fileAttr;
    }

    /**
     * 获得 又拍云 服务
     * @return RestManager
     */
    private RestManager getService(UpYunConfigFactory.UpYunConfig config){
        return new RestManager(config.getBucketName(),
                config.getUsername(), config.getPassword());
    }

}
