package org.opsli.core.autoconfigure;

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


    // ==================================

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemStarterTime() {
        return systemStarterTime;
    }

    public void setSystemStarterTime(String systemStarterTime) {
        this.systemStarterTime = systemStarterTime;
    }

    public boolean isEnableDemo() {
        return enableDemo;
    }

    public void setEnableDemo(boolean enableDemo) {
        this.enableDemo = enableDemo;
    }

    public Waf getWaf() {
        return waf;
    }

    public void setWaf(Waf waf) {
        this.waf = waf;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public Web getWeb() {
        return web;
    }

    public void setWeb(Web web) {
        this.web = web;
    }

    public Excel getExcel() {
        return excel;
    }

    public void setExcel(Excel excel) {
        this.excel = excel;
    }

    // ============== 内部类 =============

    /**
     * 软防火墙
     */
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

        // =============================

        public Set<String> getUrlPatterns() {
            return urlPatterns;
        }

        public void setUrlPatterns(Set<String> urlPatterns) {
            this.urlPatterns = urlPatterns;
        }

        public Set<String> getUrlExclusion() {
            return urlExclusion;
        }

        public void setUrlExclusion(Set<String> urlExclusion) {
            this.urlExclusion = urlExclusion;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public boolean isXssFilter() {
            return xssFilter;
        }

        public void setXssFilter(boolean xssFilter) {
            this.xssFilter = xssFilter;
        }

        public boolean isSqlFilter() {
            return sqlFilter;
        }

        public void setSqlFilter(boolean sqlFilter) {
            this.sqlFilter = sqlFilter;
        }
    }

    /**
     * Web
     */
    public static class Web {

        /** 文件上传地址 */
        private String uploadPath;

        // ===========================


        public String getUploadPath() {
            return uploadPath;
        }

        public void setUploadPath(String uploadPath) {
            this.uploadPath = uploadPath;
        }
    }

    /**
     * 认证类
     */
    public static class Auth {

        /** 超级管理员账号 */
        private String superAdmin;

        /** 默认密码 */
        private String defaultPass;

        /** Token */
        private Token token;

        /** Login */
        private Login login;

        // ===========================

        public String getSuperAdmin() {
            return superAdmin;
        }

        public void setSuperAdmin(String superAdmin) {
            this.superAdmin = superAdmin;
        }

        public String getDefaultPass() {
            return defaultPass;
        }

        public void setDefaultPass(String defaultPass) {
            this.defaultPass = defaultPass;
        }

        public Token getToken() {
            return token;
        }

        public void setToken(Token token) {
            this.token = token;
        }

        public Login getLogin() {
            return login;
        }

        public void setLogin(Login login) {
            this.login = login;
        }

        /**
         * 认证类
         */
        public static class Token {

            /** 盐值 */
            private String secret;

            /** 有效时间 （分钟）*/
            private Integer effectiveTime;

            /** 排除URL*/
            private Set<String> urlExclusion;

            // ===============================

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }

            public Integer getEffectiveTime() {
                return effectiveTime;
            }

            public void setEffectiveTime(Integer effectiveTime) {
                this.effectiveTime = effectiveTime;
            }

            public Set<String> getUrlExclusion() {
                return urlExclusion;
            }

            public void setUrlExclusion(Set<String> urlExclusion) {
                this.urlExclusion = urlExclusion;
            }
        }

        /**
         * 登录类
         */
        public static class Login {

            /** 失败次数 */
            private Integer slipCount;

            /** 失败N次后弹出验证码 （超过验证码阈值 弹出验证码） */
            private Integer slipVerifyCount;

            /** 失败锁定时间(秒) */
            private Integer slipLockSpeed;

            public Integer getSlipCount() {
                return slipCount;
            }

            public void setSlipCount(Integer slipCount) {
                this.slipCount = slipCount;
            }

            public Integer getSlipVerifyCount() {
                return slipVerifyCount;
            }

            public void setSlipVerifyCount(Integer slipVerifyCount) {
                this.slipVerifyCount = slipVerifyCount;
            }

            public Integer getSlipLockSpeed() {
                return slipLockSpeed;
            }

            public void setSlipLockSpeed(Integer slipLockSpeed) {
                this.slipLockSpeed = slipLockSpeed;
            }
        }
    }

    /**
     * Excel
     */
    public static class Excel {

        /** 最大导入操作数 */
        private Integer importMaxCount;

        /** 最大导出操作数 */
        private Integer exportMaxCount;

        // ===========================

        public Integer getImportMaxCount() {
            return importMaxCount;
        }

        public void setImportMaxCount(Integer importMaxCount) {
            this.importMaxCount = importMaxCount;
        }

        public Integer getExportMaxCount() {
            return exportMaxCount;
        }

        public void setExportMaxCount(Integer exportMaxCount) {
            this.exportMaxCount = exportMaxCount;
        }
    }

}

