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
package org.opsli.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.opsli.api.wrapper.system.logs.LogsModel;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.annotation.EnableLog;
import org.opsli.common.annotation.RequiresPermissionsCus;
import org.opsli.common.utils.IPUtil;
import org.opsli.core.thread.LogsThreadPool;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.utils
 * @Author: Parker
 * @CreateTime: 2020-09-19 20:03
 * @Description: 日志工具类
 */
@Slf4j
public final class LogUtil {

    /**
     * 保存日志
     * @param point
     * @param e 异常
     * @param timerCount 花费毫秒数
     */
    public static void saveLog(ProceedingJoinPoint point, Exception e, long timerCount){

        try {
            Object[] args = point.getArgs();
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();

            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            if(sra == null){
                return;
            }
            HttpServletRequest request = sra.getRequest();

            // EnableLog 如果不被 EnableLog修饰 则直接退出
            // 判断 方法上是否使用 HotData注解 如果没有表示开启热数据 则直接跳过
            EnableLog enableLog = method.getAnnotation(EnableLog.class);
            if(enableLog == null){
                return;
            }

            UserModel user;
            try {
                user = UserUtil.getUser();
            }catch (Exception ex){
                return;
            }

            LogsModel logsModel = new LogsModel();
            // 操作方法
            String methodName = request.getMethod();
            // 获得IP
            String clientIpAddress = IPUtil.getClientIpAddress(request);

            // 设置标题
            setTitle(point, method, logsModel, user);
            // 设置类型
            logsModel.setType(e == null ? LogsModel.TYPE_ACCESS : LogsModel.TYPE_EXCEPTION);
            // 设置客户端代理
            logsModel.setUserAgent(request.getHeader("user-agent"));
            // 设置URI
            logsModel.setRequestUri(request.getRequestURI());
            // 设置IP
            logsModel.setRemoteAddr(clientIpAddress);
            // 设置参数
            logsModel.setParams(JSONUtil.parse(args).toString());
            // 设置方法
            logsModel.setMethod(methodName);
            // 设置执行时长
            logsModel.setTimeout(timerCount);
            // 设置异常信息
            if(e != null){
                logsModel.setException(e.getMessage());
            }

            logsModel.setCreateBy(user.getId());
            logsModel.setUpdateBy(user.getId());
            logsModel.setIzManual(true);

            // 保存日志
            LogsThreadPool.process(logsModel);
        } catch (Exception ex){
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * 设置日志标题
     * @param point
     * @param method
     * @param logsModel
     */
    private static void setTitle(ProceedingJoinPoint point, Method method, LogsModel logsModel, UserModel user){
        // 设置 title
        EnableLog enableLog = method.getAnnotation(EnableLog.class);
        if(enableLog != null){
            //注解上的描述,操作日志内容
            String title = enableLog.title();
            if(StringUtils.isNotEmpty(title)){
                logsModel.setTitle(title);
            }
        }
        // 如果title 还为空 则系统自动赋值
        if(StringUtils.isEmpty(logsModel.getTitle())){
            RequiresPermissions permissions = method.getAnnotation(RequiresPermissions.class);
            RequiresPermissionsCus permissionsCus = method.getAnnotation(RequiresPermissionsCus.class);

            List<String> values = null;
            if(permissions != null){
                values = Convert.toList(String.class, permissions.value());
            }else if(permissionsCus != null){
                values = Convert.toList(String.class, permissionsCus.value());
            }

            if(CollUtil.isNotEmpty(values)){
                if(values != null && values.size() > 0){
                    String perms = values.get(0);
                    // 获得当前用户所持有菜单
                    List<MenuModel> menuListByUserId = UserUtil.getMenuListByUserId(user.getId());
                    if(menuListByUserId != null){
                        // 根据当前controller权限 获得对应权限数据
                        MenuModel permsModel = MenuUtil.getMenuByCode(perms);
                        if(permsModel != null){
                            // 依次获得菜单全名
                            StringBuilder logTitleBuf = new StringBuilder();
                            List<MenuModel> parentMenu = getParentMenu(menuListByUserId, permsModel);
                            for (int i = parentMenu.size() - 1; i >= 0; i--) {
                                logTitleBuf.append(parentMenu.get(i).getMenuName())
                                        .append("-");
                            }
                            String logTitle = logTitleBuf.toString();
                            if(StringUtils.isNotEmpty(logTitle)){
                                logsModel.setTitle(logTitle + permsModel.getMenuName());
                            }
                        }
                    }
                }
            }

            // 如果title 还是为空 则系统自动赋class
            if(StringUtils.isEmpty(logsModel.getTitle())){
                // 获取请求的类名
                String className = point.getTarget().getClass().getName();
                String methodName = method.getName();
                logsModel.setTitle(className+"."+methodName);
            }
        }
    }


    /**
     * 递归 获得菜单全名
     * @param menuList
     * @param permsModel
     * @return
     */
    private static List<MenuModel> getParentMenu(List<MenuModel> menuList, MenuModel permsModel){
        List<MenuModel> menuModels = Lists.newArrayList();
        MenuModel parentMenu = null;
        for (MenuModel menu : menuList) {
            if(menu.getId().equals(permsModel.getParentId())){
                parentMenu = menu;
                break;
            }
        }

        if(parentMenu != null){
            menuModels.add(parentMenu);
            List<MenuModel> temp = getParentMenu(menuList, parentMenu);
            menuModels.addAll(temp);
        }

        return menuModels;
    }


    // =================

    private LogUtil(){}
}
