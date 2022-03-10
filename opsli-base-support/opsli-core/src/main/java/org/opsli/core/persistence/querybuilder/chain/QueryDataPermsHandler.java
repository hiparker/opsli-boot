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
package org.opsli.core.persistence.querybuilder.chain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.role.RoleModel;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.utils.FieldUtil;
import org.opsli.common.utils.ListDistinctUtil;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.core.persistence.querybuilder.conf.WebQueryConf;
import org.opsli.core.utils.UserUtil;

import java.util.List;

/**
 * 数据权限赋值处理
 *
 * @author Parker
 * @date 2020-09-13 19:36
 */
public class QueryDataPermsHandler implements QueryBuilderChain{

    /**
     * 子 责任链
     */
    private QueryBuilderChain queryBuilderChain;


    public QueryDataPermsHandler(){}

    /**
     * 构造函数
     * @param queryBuilderChain 责任链
     */
    public QueryDataPermsHandler(QueryBuilderChain queryBuilderChain){
        this.queryBuilderChain = queryBuilderChain;
    }

    @Override
    public <T extends BaseEntity> QueryWrapper<T> handler(Class<T> entityClazz, QueryWrapper<T> wrapper) {
        // 执行 子 责任链
        if(queryBuilderChain != null){
            wrapper = queryBuilderChain.handler(entityClazz, wrapper);
        }

        // 自身责任 -- 判断组织
        boolean flag = ReflectUtil.hasField(entityClazz, MyBatisConstants.FIELD_ORG_GROUP);
        if(flag) {
            // 处理查询条件
            handleDataPermsCondition(null, wrapper);
        }

        return wrapper;
    }

    @Override
    public <T extends BaseEntity> QueryWrapper<T> handler(Class<T> entityClazz, WebQueryConf webQueryConf, QueryWrapper<T> wrapper) {
        // 执行 子 责任链
        if(queryBuilderChain != null){
            wrapper = queryBuilderChain.handler(entityClazz, webQueryConf, wrapper);
        }

        // 自身责任 -- 判断组织
        boolean flag = ReflectUtil.hasField(entityClazz, MyBatisConstants.FIELD_ORG_GROUP);
        if(flag) {
            // 处理查询条件
            handleDataPermsCondition(webQueryConf, wrapper);
        }

        return wrapper;
    }


    /**
     * 处理 数据权限 条件
     *
     * @param webQueryConf 字段配置
     * @param queryWrapper 组织集合
     */
    private static <T extends BaseEntity> QueryWrapper<T> handleDataPermsCondition(
            WebQueryConf webQueryConf, QueryWrapper<T> queryWrapper) {

        // 创建人字段
        String createByFiled = FieldUtil.humpToUnderline(MyBatisConstants.FIELD_CREATE_BY);
        // 组织字段
        String orgFiled = FieldUtil.humpToUnderline(MyBatisConstants.FIELD_ORG_GROUP);
        if(null != webQueryConf){
            if(StringUtils.isNotEmpty(webQueryConf.get(MyBatisConstants.FIELD_CREATE_BY))){
                createByFiled = FieldUtil.humpToUnderline(
                        webQueryConf.get(MyBatisConstants.FIELD_CREATE_BY)
                );
            }
            if(StringUtils.isNotEmpty(webQueryConf.get(MyBatisConstants.FIELD_ORG_GROUP))){
                orgFiled = FieldUtil.humpToUnderline(
                        webQueryConf.get(MyBatisConstants.FIELD_ORG_GROUP)
                );
            }
        }

        // 1. 当前用户
        UserModel currUser = UserUtil.getUser();
        String userId = currUser.getId();

        // 2. 当前用户 组织机构集合
        List<UserOrgRefModel> userOrgRefModelList = UserUtil.getOrgListByUserId(userId);
        List<String> orgIdGroupList = Lists.newArrayListWithCapacity(userOrgRefModelList.size());
        for (UserOrgRefModel userOrgRefModel : userOrgRefModelList) {
            orgIdGroupList.add(userOrgRefModel.getOrgIds());
        }
        // 组织机构集合 去重
        orgIdGroupList = ListDistinctUtil.distinct(orgIdGroupList);

        // 3. 获得查询类型
        // 如果是超级管理员 则查询类型为全部
        ConditionType conditionType = ConditionType.SELF;
        if(StringUtils.equals(UserUtil.SUPER_ADMIN, currUser.getUsername())){
            conditionType = ConditionType.ALL;
        }else{
            // 如果不是超级管理员 则获得当前用户的默认角色下的 授权数据权限类型
            RoleModel defRole = UserUtil.getUserDefRoleByUserId(userId);
            if(null != defRole){
                conditionType = ConditionType.getConditionType(defRole.getDataScope());
            }

            // 如果组织为空 则默认权限为查自己的数据
            if(!ConditionType.SELF.equals(conditionType) &&
                    CollUtil.isEmpty(orgIdGroupList)){
                // 如果不是 查看全部数据 需要默认角色权限为只查自己
                if(!ConditionType.ALL.equals(conditionType)){
                    conditionType = ConditionType.SELF;
                }
            }
        }

        // 常量
        final ConditionType finalConditionType = conditionType;
        final String finalOrgField = orgFiled;
        final String finalCreateByField = createByFiled;
        final List<String> finalOrgIdGroupList = orgIdGroupList;

        // 查询 全部
        if(ConditionType.ALL.equals(finalConditionType)){
            // ..
        }else {
            queryWrapper.and(wra -> {
                // 查询 本部门
                if(ConditionType.DEPT.equals(finalConditionType)){
                    wra.in(finalOrgField, finalOrgIdGroupList);
                }
                // 部门及以下
                else if(ConditionType.DEPT_AND_BELOW.equals(finalConditionType)){
                    wra.and(wraConfine -> {
                        // 增加右模糊 查询条件
                        for (int i = 0; i < finalOrgIdGroupList.size(); i++) {
                            // 右模糊匹配
                            wraConfine.likeRight(
                                    finalOrgField, finalOrgIdGroupList.get(i));

                            if(i < finalOrgIdGroupList.size() - 1){
                                wraConfine.or();
                            }
                        }
                    });
                }else {
                    // 查自身
                    wra.eq(finalCreateByField, userId);
                }
            });
        }

        return queryWrapper;
    }

    // =================================

    /**
     * 条件类型
     */
    public enum ConditionType{

        /** 自身 */
        SELF("0", "自身"),

        /** 部门 */
        DEPT("1", "本部门"),

        /** 部门及以下 */
        DEPT_AND_BELOW("2", "本部门及以下"),

        /** 全部 */
        ALL("3", "全部");


        /** 值 */
        private final String value;

        /** 描述 */
        private final String describe;

        ConditionType(String value, String describe){
            this.value = value;
            this.describe = describe;
        }

        public String getValue() {
            return value;
        }

        public String getDescribe() {
            return describe;
        }

        /**
         * 获得 类型
         * @param value 值
         * @return AlgSource
         */
        public static ConditionType getConditionType(String value) {
            ConditionType[] var1 = values();
            for (ConditionType source : var1) {
                if(source.value.equals(value)){
                    return source;
                }
            }
            // 如果条件类型为空 则默认 查看自身数据
            return ConditionType.SELF;
        }
    }

}
