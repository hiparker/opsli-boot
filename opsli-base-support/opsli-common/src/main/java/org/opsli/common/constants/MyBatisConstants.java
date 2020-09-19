package org.opsli.common.constants;

import cn.hutool.setting.dialect.Props;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.constants
 * @Author: Parker
 * @CreateTime: 2020-09-18 18:46
 * @Description: MyBatis 常量
 */
public final class MyBatisConstants {

    private static final Props prop = Props.getProp("application.yaml");

    /** 逻辑删除值 */
    public static final char  LOGIC_DELETE_VALUE =
            prop.getChar("mybatis-plus.global-config.db-config.logic-delete-value",'1');
    /** 逻辑不删除值 */
    public static final char  LOGIC_NOT_DELETE_VALUE =
            prop.getChar("mybatis-plus.global-config.db-config.logic-not-delete-value",'0');


    /** 创建人 */
    public static final String FIELD_CREATE_BY = "createBy";
    /** 更新时间 */
    public static final String FIELD_CREATE_TIME = "createTime";
    /** 更新人 */
    public static final String FIELD_UPDATE_BY = "updateBy";
    /** 更新时间 */
    public static final String FIELD_UPDATE_TIME = "updateTime";
    /** 逻辑删除 */
    public static final String FIELD_DELETE_LOGIC = "deleted";
    /** 乐观锁 */
    public static final String FIELD_OPTIMISTIC_LOCK = "version";

    private MyBatisConstants(){}
}
