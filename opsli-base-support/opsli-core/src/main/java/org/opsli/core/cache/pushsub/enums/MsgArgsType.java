package org.opsli.core.cache.pushsub.enums;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.enums
 * @Author: Parker
 * @CreateTime: 2020-09-15 16:15
 * @Description: 消息具体类型
 */
public enum MsgArgsType {

    /** 字典Key */
    DICT_KEY,
    /** 字典Field */
    DICT_FIELD,
    /** 字典Value */
    DICT_VALUE,
    /** 字典Value */
    DICT_TYPE,

    /** 缓存数据Key */
    CACHE_DATA_KEY,
    /** 缓存数据Value */
    CACHE_DATA_VALUE,
    /** 缓存数据Type */
    CACHE_DATA_TYPE,
    ;

}
