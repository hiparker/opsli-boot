package org.opsli.common.utils;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.utils
 * @Author: Parker
 * @CreateTime: 2020-09-19 23:21
 * @Description: 驼峰转换
 */
public final class HumpUtil {

    private final static String UNDERLINE = "_";


    private HumpUtil(){}


    /***
     * 下划线命名转为驼峰命名
     *
     * @param para
     *        下划线命名的字符串
     */

    public static String underlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String a[] = para.split(UNDERLINE);
        for (String s : a) {
            if (!para.contains(UNDERLINE)) {
                result.append(s);
                continue;
            }
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public static String humpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;//定位
        if (!para.contains(UNDERLINE)) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, UNDERLINE);
                    temp += 1;
                }
            }
        }
        return sb.toString().toLowerCase();
    }

    public static void main(String[] args) {
        String aa = HumpUtil.humpToUnderline("tenantId");
        String bb = HumpUtil.underlineToHump(aa);
        System.out.println(aa);
        System.out.println(bb);
    }

}
