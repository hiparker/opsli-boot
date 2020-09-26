package org.opsli.modulars.system;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.msg
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:36
 * @Description: 核心类 - 消息
 */
public enum SystemMsg implements BaseMsg {


    /**
     * 数据字典
     */
    EXCEPTION_DICT_UNIQUE(20000,"字典编号重复，该字典已存在！"),
    EXCEPTION_DICT_DETAIL_UNIQUE(20001,"字典名称或值重复，该字典已存在！"),

    /**
     * 角色
     */
    EXCEPTION_ROLE_UNIQUE(20000,"角色编号重复，该角色已存在！"),

    /**
     * 用户
     */
    EXCEPTION_USER_UNIQUE(20000,"该用户已存在！"),

    ;

    private int code;
    private String message;

    SystemMsg(int code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
