package org.opsli.common.base.entity;

import com.baomidou.mybatisplus.annotation.Version;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.base.entity
 * @Author: Parker
 * @CreateTime: 2020-09-14 17:29
 * @Description: Entity 基类
 */
public class BaseEntity {

    /** 乐观锁 */
    @Version
    private Integer version;

}
