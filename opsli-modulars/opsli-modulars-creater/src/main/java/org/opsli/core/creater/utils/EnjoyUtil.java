package org.opsli.core.creater.utils;

import cn.hutool.core.io.IoUtil;
import com.jfinal.kit.Kv;
import com.jfinal.template.Engine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/***
 * jfinal魔板引擎
 * @author dufuzhong
 */
@Slf4j
public final class EnjoyUtil {

    private static final String BASE_PATH = "/tpl";

    /**
     * 根据具体魔板生成文件
     * @param templateFileName  模板文件名称
     * @param kv                渲染参数
     * @return
     */
    public static String render(String templateFileName, Kv kv)  {

        //ClassPathResource resource = new ClassPathResource(templateFileName);
        // (InputStream inputStream = resource.getInputStream())
        String str = "";
        ClassPathResource resource = new ClassPathResource(BASE_PATH + templateFileName);
        try (InputStream inputStream = resource.getInputStream()){
            String readTpl = IoUtil.read(inputStream, StandardCharsets.UTF_8);

            str = Engine.use()
                    // 开启预热模式
                    .setDevMode(true)
                    .getTemplateByString(readTpl)
                    .renderToString(kv);

        } catch (Exception e) {
            log.error("load config file {} error", templateFileName, e);
        }
        return str;
    }



    private EnjoyUtil(){}
}
