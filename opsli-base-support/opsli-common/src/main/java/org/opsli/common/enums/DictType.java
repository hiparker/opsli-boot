package org.opsli.common.enums;


/**
 * @Author: 周鹏程
 * @CreateTime: 2020-09-17 23:40
 * @Description: 字典
 */
public enum DictType {

    /** no_yes */
    NO_YES_NO("0"),
    NO_YES_YES("1"),

    ;

    private final String code;

    DictType(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

    public static DictType getType(String code) {
        DictType[] var1 = values();
        for (DictType type : var1) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
}
