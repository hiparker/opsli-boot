package org.opsli.core.cache.pushsub.entity;

import lombok.Data;
import org.opsli.core.cache.pushsub.enums.PushSubType;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.cache.pushsub.entity
 * @Author: Parker
 * @CreateTime: 2020-09-18 00:01
 * @Description: 热点数据处理 Entity
 */
@Data
public class CacheDataEntity {

    /** key */
    private String key;

    /** 数据类型 */
    private PushSubType type;

    /** 缓存名称 */
    private String cacheName;

}
