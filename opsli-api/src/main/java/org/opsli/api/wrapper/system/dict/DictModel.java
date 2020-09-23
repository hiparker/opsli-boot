package org.opsli.api.wrapper.system.dict;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 数据字典 - 工具类用
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DictModel {

    /** 类型编号 - 冗余 */
    private String typeCode;

    /** 字典名称 */
    private String dictName;

    /** 字典值 */
    private String dictValue;

    /** 消息 */
    private SysDictDetailModel model;

}
