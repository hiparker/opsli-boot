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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.wrapper.system.org.SysOrgModel;
import org.opsli.api.wrapper.system.user.UserOrgRefModel;
import org.opsli.common.enums.DictType;
import org.opsli.common.utils.FieldUtil;
import org.opsli.common.utils.ListDistinctUtil;

import java.util.List;

/**
 * 组织机构工具类
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
@Slf4j
public final class OrgUtil {

    /** 用户表 是否分配组织  状态标识 */
    public static final String USER_ORG_FIELD = "iz_exist_org";
    /** 显示全部 */
    public static final String ORG_ALL = "org_all";
    /** 未分组 */
    public static final String ORG_NULL = "org_null";

    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;


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
                    List<UserOrgRefModel> orgListByUserId = UserUtil.getOrgByCurrUser();
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

    // ===========

    private OrgUtil() {}

}
