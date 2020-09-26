package org.opsli.modulars.system.dict.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 字典表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysDict extends BaseEntity {



    /** 字典类型编号 */
    private String typeCode;

    /** 字典类型名称 */
    private String typeName;

    /** 是否内置数据 0是  1否*/
    private Character izLock;

    /** 备注 */
    private String remark;


    // ========================================

    /** 逻辑删除字段 */
    @TableLogic
    private Integer deleted;

}
