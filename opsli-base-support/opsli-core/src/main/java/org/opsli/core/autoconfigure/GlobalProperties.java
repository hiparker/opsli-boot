package org.opsli.core.autoconfigure;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * OPSLI 配置文件
 *
 * @author 周鹏程
 * @date 2021-01-31 5:52 下午
 **/
@Configuration
@ConfigurationProperties(prefix = GlobalProperties.PREFIX)
@Data
@EqualsAndHashCode(callSuper = false)
public class GlobalProperties {

    public static final String PREFIX = "opsli";

    /** 系统名称 */
    private String systemName;

    /** 系统启动时间 为空则默认 真实当前系统启动时间 */
    private String systemStarterTime;

    /** 是否开启演示模式 */
    private boolean enableDemo;

    /** 软防火墙 */
    private Waf waf;

    /** 认证类 */
    private Auth auth;

    /** Web类 */
    private Web web;

    /** Excel类 */
    private Excel excel;


    // ============== 内部类 =============

    /**
     * 软防火墙
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Waf {

        /** 是否生效 */
        private boolean enable;

        /** xss 过滤  */
        private boolean xssFilter;

        /** sql 过滤 */
        private boolean sqlFilter;

        /** 过滤器需要过滤的路径 */
        private Set<String> urlPatterns;

        /** 过滤器需要排除过滤的路径 */
        private Set<String> urlExclusion;

        /** 过滤器的优先级，值越小优先级越高 */
        private int order;

    }

    /**
     * Web
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Web {

        /** 文件上传地址 */
        private String uploadPath;

    }

    /**
     * 认证类
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Auth {

        /** 超级管理员账号 */
        private String superAdmin;

        /** 默认密码 */
        private String defaultPass;

        /** Token */
        private Token token;

        /** Login */
        private Login login;


        /**
         * 认证类
         */
        @Data
        @EqualsAndHashCode(callSuper = false)
        public static class Token {

            /** 盐值 */
            private String secret;

            /** 有效时间 （分钟）*/
            private Integer effectiveTime;

            /** 排除URL*/
            private Set<String> urlExclusion;

        }

        /**
         * 登录类
         */
        @Data
        @EqualsAndHashCode(callSuper = false)
        public static class Login {

            /** 失败次数 */
            private Integer slipCount;

            /** 失败N次后弹出验证码 （超过验证码阈值 弹出验证码） */
            private Integer slipVerifyCount;

            /** 失败锁定时间(秒) */
            private Integer slipLockSpeed;

        }
    }

    /**
     * Excel
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Excel {

        /** 最大导入操作数 */
        private Integer importMaxCount;

        /** 最大导出操作数 */
        private Integer exportMaxCount;

    }

}

