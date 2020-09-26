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
 * @Description: 字典表 - 明细
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysDictDetail extends BaseEntity {


    /** 字典类型ID */
    private String typeId;

    /** 字典类型Code 冗余字段 */
    private String typeCode;

    /** 字典文本 */
    private String dictName;

    /** 字典值 */
    private String dictValue;

    /** 是否内置数据 0是  1否*/
    private Character izLock;

    /** 排序 */
    private Integer sortNo;

    /** 备注 */
    private String remark;


    // ========================================

    /** 逻辑删除字段 */
    @TableLogic
    private Integer deleted;

}
