package org.opsli.core.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.msg
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:36
 * @Description: 核心类 - 消息
 */
public enum CoreMsg implements BaseMsg {

    /**
     * Mybatis-Plus
     */
    /** Mybatis-Plus 乐观锁 */
    MYBATIS_OPTIMISTIC_LOCKER(10100,"当前数据已被更改，请刷新重试！"),

    /**
     * Redis
     */
    REDIS_EXCEPTION_PUSH_SUB(10200,"Redis 订阅通道失败！"),

    /**
     * MySQL
     */
    MySQL_EXCEPTION_SQL_INTEGRITY_CONSTRAINT_VIOLATION(10300,"数据主键冲突或者已有该数据！"),

    ;

    private int code;
    private String message;

    CoreMsg(int code,String message){
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
