package org.opsli.plugins.cache.msg;

import org.opsli.common.base.msg.BaseMsg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.mail.msg
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:54
 * @Description: 邮件消息
 */
public enum EhCacheMsg implements BaseMsg {

    /** 缓存未开启 */
    EXCEPTION_ENABLE(90001,"本地缓存未开启！"),
    EXCEPTION_PUT(90001,"添加缓存失败"),
    EXCEPTION_GET(90001,"获取缓存数据失败"),
    EXCEPTION_DEL(90001,"删除缓存数据失败"),


    ;


    private int code;
    private String message;

    EhCacheMsg(int code, String message){
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
