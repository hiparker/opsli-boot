package org.opsli.modulars.system.role.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 角色表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRole extends BaseEntity {



    /** 角色编码 */
    private String roleCode;

    /** 角色名称 */
    private String roleName;

    /** 是否内置数据 0是  1否*/
    private Character izLock;

    /** 备注 */
    private String remark;


    // ========================================

    /** 逻辑删除字段 */
    @TableLogic
    private Integer deleted;

}
