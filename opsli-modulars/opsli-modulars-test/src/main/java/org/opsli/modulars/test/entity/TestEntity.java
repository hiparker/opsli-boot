package org.opsli.modulars.test.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 测试类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TestEntity extends BaseEntity {

    /** 名称 */
    private String name;

    /** 备注 */
    private String remark;


    // ========================================


    /** 多租户字段 */
    private String tenantId;

    /** 逻辑删除字段 */
    //@TableLogic
    //private Integer deleted;

}
