package org.opsli.core.cache.pushsub.enums;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.enums
 * @Author: Parker
 * @CreateTime: 2020-09-15 16:15
 * @Description: 消息具体类型
 */
public enum MsgArgsType {

    /** 字典模型 */
    DICT_MODEL,
    /** 字典模型-集合 */
    DICT_MODELS,
    /** 字典模型-传输类型 */
    DICT_MODEL_TYPE,
    /** 字典操作类型 */
    DICT_TYPE,

    /** 用户ID */
    USER_ID,
    /** 用户名 */
    USER_USERNAME,
    /** 用户数据类型 */
    USER_MODEL_TYPE,
    /** 用户数据*/
    USER_MODEL_DATA,


    /** 缓存数据Key */
    CACHE_DATA_KEY,
    /** 缓存数据Value */
    CACHE_DATA_VALUE,
    /** 缓存数据Type */
    CACHE_DATA_TYPE,
    ;

}
