package org.opsli.core.creater.utils;

import cn.hutool.core.io.IoUtil;
import com.google.common.collect.Maps;
import com.jfinal.kit.Kv;
import com.jfinal.template.Engine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/***
 * Enjoy 模板引擎
 *
 * @author parker
 * @date 2020-11-18 13:21
 */
@Slf4j
public final class EnjoyUtil {

    private static final String BASE_PATH = "/tpl";

    /** 模板文件Map */
    private static final Map<String, String> TEMPLATE_FILE_MAP = Maps.newConcurrentMap();

    /**
     * 根据具体魔板生成文件
     * @param templateFileName  模板文件名称
     * @param kv                渲染参数
     * @return String
     */
    public static String render(final String templateFileName, Kv kv)  {

        // 模板缓存 减少每次更新
        String templateFile = TEMPLATE_FILE_MAP.get(templateFileName);
        if(StringUtils.isEmpty(templateFile)){

            // 如果为空 则IO 读取原始文件
            ClassPathResource resource = new ClassPathResource(BASE_PATH + templateFileName);
            try (InputStream inputStream = resource.getInputStream()){
                templateFile = IoUtil.read(inputStream, StandardCharsets.UTF_8);
                TEMPLATE_FILE_MAP.put(templateFileName, templateFile);
            } catch (Exception e) {
                log.error("load file {} error", templateFileName, e);
            }
        }

        return Engine.use()
                // 开启预热模式
                .setDevMode(true)
                .getTemplateByString(templateFile)
                .renderToString(kv);
    }


    private EnjoyUtil(){}
}
