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
package org.opsli.plugins.security.handler;

import org.opsli.plugins.security.exception.AuthException;
import org.opsli.plugins.security.exception.AuthServiceException;
import org.opsli.plugins.security.exception.errorcode.AuthErrorCodeEnum;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

/**
 * 登录策略执行器
 *
 * @author Parker
 * @date 2022-07-16 10:50 PM
 **/
public class LoginHandler<T> {

    /** 是否初始化完成 */
    private boolean isInit;

    private AuthenticationManager authenticationManager;
    private Class<T> loginModelClass;
    private List<LoginBeforeListener> loginBeforeListenerList;
    private List<LoginAccessSuccessListener> loginAccessSuccessListenerList;
    private List<LoginAccessDeniedListener> loginAccessDeniedListenerList;

    public void login(T t, Function<T, Authentication> callback){
        if(!isInit || null == loginModelClass || null == authenticationManager){
            throw new RuntimeException("LoginHandler 未初始化");
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(null == attributes){
            throw new RuntimeException("ServletRequest 未获取");
        }
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        if(null == callback){
            AuthServiceException authException = new AuthServiceException(AuthErrorCodeEnum.AUTH_SERVICE_NOT_FIND_HANDLE);
            // 执行登录失败 监听器
            this.fireFailureEvent(t, request, response, authException);
            return;
        }

        try {
            // 执行登录前 监听器
            this.fireBeforeEvent(t);

            // 创建 Authentication
            Authentication authenticate = authenticationManager
                    .authenticate(callback.apply(t));

            // 执行登录成功 监听器
            this.fireSuccessEvent(t, authenticate, request, response);
        }catch (Exception e){
            // 执行登录失败 监听器
            this.fireFailureEvent(t, request, response, e);
        }
    }


    /**
     * 登录前执行器
     * 用于登录前 通知一系列监听事件
     * @param t 登录参数
     */
    private void fireBeforeEvent(T t) {
        if(null == loginBeforeListenerList){
            return;
        }
        for (LoginBeforeListener loginBeforeListener : loginBeforeListenerList) {
            // 如果 类或接口相同或是超类或类接口 不同则直接跳出
            Class<?> modelType = loginBeforeListener.getModelType();
            if(null == modelType
                    || (!Object.class.getName().equals(modelType.getName())
                            && !t.getClass().getName().equals(modelType.getName()))  ){
                continue;
            }

            loginBeforeListener.handle(t);
        }
    }

    /**
     * 登录成功后执行器
     * 用于登录后 通知一系列监听事件
     * @param t 登录参数
     */
    private void fireSuccessEvent(
            T t, Authentication authenticate, HttpServletRequest request, HttpServletResponse response) {
        if(null == loginAccessSuccessListenerList){
            return;
        }
        for (LoginAccessSuccessListener loginAccessSuccessListener : loginAccessSuccessListenerList) {
            // 如果 类或接口相同或是超类或类接口 不同则直接跳出
            Class<?> modelType = loginAccessSuccessListener.getModelType();
            if(null == modelType
                    || (!Object.class.getName().equals(modelType.getName())
                            && !t.getClass().getName().equals(modelType.getName()))  ){
                continue;
            }

            loginAccessSuccessListener.handle(t, authenticate, request, response);
        }
    }

    /**
     * 登录后执行器
     * 用于登录后 通知一系列监听事件
     * @param t 登录参数
     */
    private void fireFailureEvent(
            T t, HttpServletRequest request, HttpServletResponse response, Exception e) {
        if(null == loginAccessDeniedListenerList){
            return;
        }
        for (LoginAccessDeniedListener loginAccessDeniedListener : loginAccessDeniedListenerList) {
            // 如果 类或接口相同或是超类或类接口 不同则直接跳出
            Class<?> modelType = loginAccessDeniedListener.getModelType();
            if(null == modelType
                    || (!Object.class.getName().equals(modelType.getName())
                            && !t.getClass().getName().equals(modelType.getName()))  ){
                continue;
            }

            boolean isNotBreak = loginAccessDeniedListener.handle(t, request, response, e);
            if(!isNotBreak){
                break;
            }
        }
    }


    /**
     * 登录执行器构建器
     * @param <T>
     */
    public static class Builder<T> {

        private AuthenticationManager authenticationManager;
        private Class<T> loginModelClass;
        private final BeforeListenerBuilder<T> beforeListenerBuilder =
                new BeforeListenerBuilder<>(this);
        private final AccessSuccessListenerBuilder<T> accessSuccessListenerBuilder =
                new AccessSuccessListenerBuilder<>(this);
        private final AccessDeniedListenerBuilder<T> accessDeniedListenerBuilder =
                new AccessDeniedListenerBuilder<>(this);

        /**
         * 开始前监听器
         */
        public static class BeforeListenerBuilder<T> {

            private final Builder<T> builder;

            private final List<LoginBeforeListener> listenerList =
                    new CopyOnWriteArrayList<>();

            public BeforeListenerBuilder(Builder<T> builder){
                this.builder = builder;
            }

            /**
             * 新增 认证前监听器
             *  注：如果指定了Type类型 则 如果当前消息类型与Listener的类型不符 则不会发器调用
             * @param loginBeforeListener 认证前监听器
             * @return Builder<T>
             */
            public BeforeListenerBuilder<T> addListener(
                    LoginBeforeListener loginBeforeListener){
                listenerList.add(loginBeforeListener);
                return this;
            }

            public Builder<T> and(){
                return builder;
            }
        }

        /**
         * 成功后监听器
         */
        public static class AccessSuccessListenerBuilder<T> {

            private final Builder<T> builder;
            private final List<LoginAccessSuccessListener> listenerList =
                    new CopyOnWriteArrayList<>();

            public AccessSuccessListenerBuilder(Builder<T> builder){
                this.builder = builder;
            }

            /**
             * 新增 认证成功监听器
             *  注：如果指定了Type类型 则 如果当前消息类型与Listener的类型不符 则不会发器调用
             *
             * @param loginAccessSuccessListener 成功监听器
             * @return Builder<T>
             */
            public AccessSuccessListenerBuilder<T> addListener(
                    LoginAccessSuccessListener loginAccessSuccessListener){
                listenerList.add(loginAccessSuccessListener);
                return this;
            }

            public Builder<T> and(){
                return builder;
            }
        }

        /**
         * 失败后监听器
         */
        public static class AccessDeniedListenerBuilder<T> {

            private final Builder<T> builder;
            private final List<LoginAccessDeniedListener> listenerList =
                    new CopyOnWriteArrayList<>();

            public AccessDeniedListenerBuilder(Builder<T> builder){
                this.builder = builder;
            }

            /**
             * 新增 认证失败监听器
             *  注：如果指定了Type类型 则 如果当前消息类型与Listener的类型不符 则不会发器调用
             *
             * @param loginAccessDeniedListener 失败监听器
             * @return Builder<T>
             */
            public AccessDeniedListenerBuilder<T> addListener(
                    LoginAccessDeniedListener loginAccessDeniedListener){
                listenerList.add(loginAccessDeniedListener);
                return this;
            }

            public Builder<T> and(){
                return builder;
            }
        }

        /**
         * 初始化 登录模型
         * @param loginModelClass 登录模型Class
         * @return Builder<T>
         */
        public Builder<T> initLoginModelClass(Class<T> loginModelClass){
            this.loginModelClass = loginModelClass;
            return this;
        }

        /**
         * 初始化 认证控制器
         * @param authenticationManager 认证控制器
         * @return Builder<T>
         */
        public Builder<T> initAuthenticationManager(AuthenticationManager authenticationManager){
            this.authenticationManager = authenticationManager;
            return this;
        }

        /**
         * 认证开始前操作
         *
         * @return BeforeListenerBuilder
         */
        public BeforeListenerBuilder<T> before(){
            return beforeListenerBuilder;
        }

        /**
         * 认证成功 操作
         *
         * @return AccessSuccessListenerBuilder
         */
        public AccessSuccessListenerBuilder<T> accessSuccess(){
            return accessSuccessListenerBuilder;
        }

        /**
         * 认证失败 操作
         *
         * @return AccessDeniedListenerBuilder
         */
        public AccessDeniedListenerBuilder<T> accessDenied(){
            return accessDeniedListenerBuilder;
        }


        /**
         * 新增 认证失败监听器
         * @return LoginHandler<T>
         */
        public LoginHandler<T> build(){
            LoginHandler<T> loginHandler = new LoginHandler<>();

            // 赋值
            loginHandler.isInit = true;
            loginHandler.authenticationManager = this.authenticationManager;
            loginHandler.loginModelClass = this.loginModelClass;
            loginHandler.loginBeforeListenerList = this.beforeListenerBuilder.listenerList;
            loginHandler.loginAccessSuccessListenerList = this.accessSuccessListenerBuilder.listenerList;
            loginHandler.loginAccessDeniedListenerList = this.accessDeniedListenerBuilder.listenerList;
            return loginHandler;
        }
    }

}
