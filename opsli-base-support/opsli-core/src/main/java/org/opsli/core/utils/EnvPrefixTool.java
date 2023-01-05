package org.opsli.core.utils;


import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 环境前缀 工具
 *
 * @author Parker
 * @date 2022-11-16 6:52 PM
 **/
@Slf4j
public final class EnvPrefixTool {

    /**
     * 根据环境 获取业务编号
     * @param bizTag 业务号
     * @return String
     */
    public static String getEnvBizTag(String bizTag){
        String activeProfile = StrUtil.blankToDefault(
                SpringUtil.getActiveProfile(), EnvPrefixEnums.PROD.name());
        try {
            EnvPrefixEnums envPrefixEnums = EnvPrefixEnums.valueOf(activeProfile.toUpperCase());
            return envPrefixEnums.getPrefix()+bizTag;
        }catch (Exception e){
            log.error("获取环境异常 =>  {}", e.getMessage(), e);
        }
        return bizTag;
    }


    /**
     * 环境前缀 枚举
     */
    @Getter
    @AllArgsConstructor
    private enum EnvPrefixEnums{

        /** 本地环境 */
        LOCAL("LOCAL_"),

        /** 开发环境 */
        DEV("DEV_"),

        /** 测试环境 */
        TEST("TEST_"),

        /** 生产环境 */
        PROD("");

        /** 前缀 */
        private final String prefix;
    }

}
