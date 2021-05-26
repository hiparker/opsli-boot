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
package org.opsli.core.autoconfigure.conf;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.opsli.core.autoconfigure.properties.ApiPathProperties;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.opsli.core.security.shiro.authenticator.CustomModularRealmAuthenticator;
import org.opsli.core.security.shiro.filter.CustomShiroFilter;
import org.opsli.core.security.shiro.realm.FlagRealm;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Shiro配置
 *
 * 全程用 token认证 所以也就用不着 什么共享缓存 - 无状态
 *
 * @author Parker
 * @date 2017-04-20 18:33
 */
@Configuration
public class ShiroConfig {


    /**
     * filer
     * @param securityManager 安全管理器
     * @return ShiroFilterFactoryBean
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager,
                                             GlobalProperties globalProperties, ApiPathProperties apiPathProperties) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        //oauth过滤
        Map<String, Filter> filters = Maps.newHashMapWithExpectedSize(1);
        filters.put("last_filter", new CustomShiroFilter());
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = Maps.newLinkedHashMap();

        // 加载排除URL
        if(globalProperties.getAuth() != null &&
                globalProperties.getAuth().getToken() != null){
            Set<String> urlExclusion = globalProperties.getAuth().getToken().getUrlExclusion();

            if(CollUtil.isNotEmpty(urlExclusion)){
                for (String excUrl : urlExclusion) {
                    filterMap.put(excUrl, "anon");
                }
            }
        }

        // 登录接口拦截
        filterMap.put("/system/login", "anon");
        filterMap.put("/system/publicKey", "anon");
        filterMap.put("/system/slipCount", "anon");
        filterMap.put("/captcha*", "anon");

        // 导出Excel\模版 不做自动拦截 手动拦截
        filterMap.put(apiPathProperties.getGlobalPrefix() + "/**/exportExcel", "anon");
        filterMap.put(apiPathProperties.getGlobalPrefix() + "/**/importExcel/template", "anon");

        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/app/**", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/doc.html", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/**", "last_filter");

        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("sessionManager")
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager);
        // 设置验证器为自定义验证器
        securityManager.setAuthenticator(modularRealmAuthenticator());

        List<Realm> realms = Lists.newArrayList();
        // 拿到state包下 实现了 FlagRealm 接口的,所有子类
        Set<Class<?>> clazzSet = ClassUtil.scanPackageBySuper(
                FlagRealm.class.getPackage().getName(),
                FlagRealm.class
        );
        for (Class<?> aClass : clazzSet) {
            // 位运算 去除抽象类
            if((aClass.getModifiers() & Modifier.ABSTRACT) != 0){
                continue;
            }

            try {
                realms.add((Realm) aClass.newInstance());
            } catch (Exception ignored){ }
        }

        if(CollUtil.isNotEmpty(realms)){
            // 追加 Realms
            securityManager.setRealms(realms);
        }

        return securityManager;
    }

    /**
     * 针对多Realm，使用自定义身份验证器
     */
    @Bean("modularRealmAuthenticator")
    public ModularRealmAuthenticator modularRealmAuthenticator(){
        CustomModularRealmAuthenticator authenticator = new CustomModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return authenticator;
    }



    // ===================== 固定三板斧 =====================
    // 其实 没有 Spring Security 配置起来简单
    //

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean("defaultAdvisorAutoProxyCreator")
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        proxyCreator.setUsePrefix(true);
        proxyCreator.setAdvisorBeanNamePrefix("_no_advisor");
        return proxyCreator;
    }

    /**
     * 开启shiro权限注解生效
     * @param securityManager 安全管理器
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean("authorizationAttributeSourceAdvisor")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
