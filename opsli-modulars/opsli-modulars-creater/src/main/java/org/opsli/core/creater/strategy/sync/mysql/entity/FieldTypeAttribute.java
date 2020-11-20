package org.opsli.core.creater.strategy.sync.mysql.entity;


import lombok.Data;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.creater.strategy.sync.mysql.entity
 * @Author: Parker
 * @CreateTime: 2020-11-18 13:21
 * @Description: MySQL 字段类型 属性
 */
@Data
public class FieldTypeAttribute {

    private boolean izLength;

    private boolean izPrecision;

    public FieldTypeAttribute() {
    }

    public FieldTypeAttribute(boolean izLength, boolean izPrecision) {
        this.izLength = izLength;
        this.izPrecision = izPrecision;
    }
}
