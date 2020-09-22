package org.opsli.common.enums;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.enums
 * @Author: Parker
 * @CreateTime: 2020-09-17 23:40
 * @Description: 通过单例 模式 生成系统唯一标示
 */
public enum ValiArgsType {

    /** 不能为空 */
    IS_NOT_NULL,
    /** 字母，数字和下划线 */
    IS_GENERAL,
    /** 数字 */
    IS_NUMBER,
    /** 纯字母 */
    IS_LETTER,
    /** 大写 */
    IS_UPPER_CASE,
    /** 小写 */
    IS_LOWER_CASE,
    /** ip4 */
    IS_IPV4,
    /** 金额 */
    IS_MONEY,
    /** 邮箱 */
    IS_EMAIL,
    /** 手机号 */
    IS_MOBILE,
    /** 18位身份证 */
    IS_CITIZENID,
    /** 邮编 */
    IS_ZIPCODE,
    /** URL */
    IS_URL,
    /** 汉字 */
    IS_CHINESE,
    /** 汉字，字母，数字和下划线 */
    IS_GENERAL_WITH_CHINESE,
    /** MAC地址 */
    IS_MAC,
    /** 中国车牌 */
    IS_PLATE_NUMBER,

    ;
    public static void main(String[] args) {
        /*
        为空判断
            isNull
        字母，数字和下划线
            isGeneral
        数字
            isNumber
        纯字母
            isLetter
        大小写
            isUpperCase
            isLowerCase
        ip4
            isIpv4
        金额
            isMoney
        邮件
            isEmail
        手机号码
            isMobile
        18位身份证
            isCitizenId
        邮编
            isZipCode
        URL
            isUrl
        汉字
            isChinese
        汉字，字母，数字和下划线
            isGeneralWithChinese
        mac地址
            isMac
        中国车牌
            isPlateNumber
        uuid
            isUUID
        *
        *
        *
        *
        *
        * */
    }
}
