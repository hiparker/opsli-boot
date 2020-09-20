package org.opsli.core.utils;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.utils
 * @Author: Parker
 * @CreateTime: 2020-09-19 20:03
 * @Description: 用户工具类
 */
public final class UserUtil {

    private UserUtil(){}

    /**
     * 获得 租户ID
     * @return
     */
    public static String getTenantId(){
        // TODO 如果 没取到多租户ID  也按照默认值赋值 且不可删除默认多租户数据
        // TODO 判断权限 如果是 admin 超级管理员 则租户ID清空 且findList 不做处理 否则默认都会做处理
        // TODO 如果表中 没有 tenant_id 字段 则不进行多租户处理

        return "a121321255";
    }

}
