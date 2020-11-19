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

    private String code;

    DictType(String code){
        this.code = code;
    }


    public String getCode(){
        return this.code;
    }
}
