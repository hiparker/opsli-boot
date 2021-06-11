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
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.user.UserApi;
import org.opsli.api.web.system.user.UserOrgRefApi;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.api.wrapper.system.org.SysOrgModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.api.wrapper.system.user.UserOrgRefWebModel;
import org.opsli.common.enums.DictType;
import org.opsli.common.utils.FieldUtil;
import org.opsli.common.utils.ListDistinctUtil;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.msg.CoreMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * 组织机构工具类
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class OrgUtil {

    /** 前缀 */
    public static final String PREFIX_CODE = "org:userId:";

    /** 用户表 是否分配组织  状态标识 */
    public static final String USER_ORG_FIELD = "iz_exist_org";
    /** 显示全部 */
    public static final String ORG_ALL = "org_all";
    /** 未分组 */
    public static final String ORG_NULL = "org_null";

    /** 用户组织 Api */
    private static UserOrgRefApi userOrgRefApi;

    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;

    /**
     * 根据 userId 获得用户菜单
     * @param userId 用户ID
     * @return List
     */
    public static List<UserOrgRefModel> getOrgListByUserId(String userId){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        // 缓存Key
        String cacheKey = PREFIX_CODE + userId;

        List<UserOrgRefModel> orgList;

        // 先从缓存里拿
        Object obj = CacheUtil.getTimed(cacheKey);
        orgList = Convert.toList(UserOrgRefModel.class, obj);
        if(CollUtil.isNotEmpty(orgList)){
            return orgList;
        }

        // 拿不到 --------
        // 防止缓存穿透判断
        boolean hasNilFlag = CacheUtil.hasNilFlag(cacheKey);
        if(hasNilFlag){
            return ListUtil.empty();
        }


        try {
            // 分布式加锁
            if(!DistributedLockUtil.lock(cacheKey)){
                // 无法申领分布式锁
                log.error(CoreMsg.REDIS_EXCEPTION_LOCK.getMessage());
                return ListUtil.empty();
            }

            // 如果获得锁 则 再次检查缓存里有没有， 如果有则直接退出， 没有的话才发起数据库请求
            obj = CacheUtil.getTimed(cacheKey);
            orgList = Convert.toList(UserOrgRefModel.class, obj);
            if(CollUtil.isNotEmpty(orgList)){
                return orgList;
            }

            // 查询数据库
            ResultVo<List<UserOrgRefModel>> resultVo = userOrgRefApi.findListByUserId(userId);
            if(resultVo.isSuccess()){
                orgList = resultVo.getData();
                // 存入缓存
                CacheUtil.put(cacheKey, orgList);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }finally {
            // 释放锁
            DistributedLockUtil.unlock(cacheKey);
        }

        if(CollUtil.isEmpty(orgList)){
            // 设置空变量 用于防止穿透判断
            CacheUtil.putNilFlag(cacheKey);
            return ListUtil.empty();
        }

        return orgList;
    }

    /**
     * 处理展示节点
     * @param parentId 父节点ID
     * @param orgModelList 组织集合
     * @return List
     */
    public static List<SysOrgModel> createDefShowNodes(String parentId, List<SysOrgModel> orgModelList) {
        // 显示全部
        SysOrgModel orgAll = new SysOrgModel();
        orgAll.setId(ORG_ALL);
        orgAll.setOrgName("全部");
        orgAll.setParentId(parentId);
        orgAll.setSortNo(-2);
        orgModelList.add(orgAll);

        // 未分组
        SysOrgModel orgNull = new SysOrgModel();
        orgNull.setId(ORG_NULL);
        orgNull.setOrgName("未分组");
        orgNull.setParentId(parentId);
        orgNull.setSortNo(-1);
        orgModelList.add(orgNull);

        return orgModelList;
    }

    /**
     * 处理 组织ID 组条件
     * @param orgIdGroup 父节点ID
     * @param queryWrapper 组织集合
     */
    public static void handleOrgIdGroupCondition(String orgIdGroup, QueryWrapper<?> queryWrapper) {
        String userRefOrgField = "b.org_ids";

        queryWrapper.and(wra -> {

            if(!ORG_NULL.equals(orgIdGroup)){
                wra.and(wraConfine -> {
                    // 增加自身 组织限制
                    UserModel currUser = UserUtil.getUser();
                    List<UserOrgRefModel> orgListByUserId = OrgUtil.getOrgListByUserId(currUser.getId());
                    if(CollUtil.isEmpty(orgListByUserId)){
                        // 如果为空 则默认 不查询
                        wraConfine.eq("1", "2");
                    }else {
                        List<String> parentIdList = Lists.newArrayListWithCapacity(orgListByUserId.size());
                        // 处理ParentId数据
                        for (UserOrgRefModel userOrgRefModel : orgListByUserId) {
                            parentIdList.add(userOrgRefModel.getOrgIds());
                        }
                        // 去重
                        parentIdList = ListDistinctUtil.distinct(parentIdList);

                        // 增加右模糊 查询条件
                        for (int i = 0; i < parentIdList.size(); i++) {
                            // 右模糊匹配
                            wraConfine.likeRight(
                                    userRefOrgField,
                                    parentIdList.get(i));

                            if(i < parentIdList.size() - 1){
                                wraConfine.or();
                            }
                        }
                    }
                });
            }

            // 放在后面 前面会报错
            switch (orgIdGroup){
                case ORG_ALL:
                    wra.and(wraAll -> {
                        wraAll.eq(FieldUtil.humpToUnderline(USER_ORG_FIELD),
                                DictType.NO_YES_YES.getValue());
                    });
                    wra.or(wraAll -> {
                        wraAll.eq(FieldUtil.humpToUnderline(USER_ORG_FIELD),
                                DictType.NO_YES_NO.getValue());
                    });
                    break;
                case ORG_NULL:
                    wra.and(wraNo -> {
                        wraNo.eq(FieldUtil.humpToUnderline(USER_ORG_FIELD),
                                DictType.NO_YES_NO.getValue());
                    });
                    break;
                default:
                    // 右模糊匹配
                    wra.likeRight(
                            userRefOrgField, orgIdGroup);
                    wra.and(wraYes -> {
                        wraYes.eq(FieldUtil.humpToUnderline(USER_ORG_FIELD),
                                DictType.NO_YES_YES.getValue());
                    });
                    break;
            }

        });
    }

    // ============== 刷新缓存 ==============

    /**
     * 刷新用户组织 - 删就完了
     * @param userId 用户ID
     * @return boolean
     */
    public static boolean refreshOrg(String userId){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        if(StringUtils.isEmpty(userId)){
            return true;
        }

        // 计数器
        int count = 0;

        UserOrgRefWebModel orgRefModel = CacheUtil.getTimed(UserOrgRefWebModel.class, PREFIX_CODE + userId);
        boolean hasNilFlag = CacheUtil.hasNilFlag(PREFIX_CODE + userId);

        // 只要不为空 则执行刷新
        if (hasNilFlag){
            count++;
            // 清除空拦截
            boolean tmp = CacheUtil.delNilFlag(PREFIX_CODE + userId);
            if(tmp){
                count--;
            }
        }

        if(orgRefModel != null){
            count++;
            // 先删除
            boolean tmp = CacheUtil.del(PREFIX_CODE + userId);
            if(tmp){
                count--;
            }
        }
        return count == 0;
    }


    // =====================================

    /**
     * 初始化
     */
    @Autowired
    public void init(UserOrgRefApi userOrgRefApi) {
        OrgUtil.userOrgRefApi = userOrgRefApi;

        IS_INIT = true;
    }

}
