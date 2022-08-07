package org.opsli.modulars.tools.oss.web;

import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.util.CollectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.plugins.oss.OssStorageFactory;
import org.opsli.plugins.oss.service.BaseOssStorageService;
import org.opsli.plugins.oss.service.OssStorageService;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;


/**
 * 文件管理 Controller
 *
 * @author Parker
 * @date 2020-09-13 17:40
 */
@Api(tags = "文件管理类")
@Slf4j
@ApiRestController("/{ver}/tools/oss")
public class OssRestController {

    /**
     * 文件上传
     * @param request 文件流 request
     * @return ResultWrapper
     */
    @ApiOperation(value = "文件上传", notes = "文件上传")
    @PostMapping("/upload")
    public ResultWrapper<?> upload(MultipartHttpServletRequest request) {
        Iterator<String> itr = request.getFileNames();
        String uploadedFile = itr.next();
        List<MultipartFile> files = request.getFiles(uploadedFile);
        if (CollectionUtils.isEmpty(files)) {
            // 请选择文件
            return ResultWrapper.getCustomResultWrapper(SystemMsg.EXCEPTION_USER_FILE_NULL);
        }

        try {
            MultipartFile multipartFile = files.get(0);
            Resource resource = multipartFile.getResource();
            String filename = resource.getFilename();

            // 调用OSS 服务保存文件
            OssStorageService ossStorageService = OssStorageFactory.INSTANCE.getHandle();
            BaseOssStorageService.FileAttr fileAttr = ossStorageService.upload(
                    multipartFile.getInputStream(), FileUtil.extName(filename));

            return ResultWrapper.getSuccessResultWrapper(fileAttr);
        }catch (IOException e){
            log.error(e.getMessage(), e);
        }
        return ResultWrapper.getErrorResultWrapper();
    }


}
