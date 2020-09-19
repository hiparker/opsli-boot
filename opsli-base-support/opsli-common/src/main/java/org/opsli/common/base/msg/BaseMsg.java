package org.opsli.common.base.msg;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.base.msg
 * @Author: Parker
 * @CreateTime: 2020-09-13 19:34
 * @Description: 总消息类
 *
 * 消息类 用来存放消息
 * opsli将消息全部提取出至一个总文件
 *
 */
public interface BaseMsg {

    /**
     * 获取消息的状态码
     */
    Integer getCode();

    /**
     * 获取消息提示信息
     */
    String getMessage();

}
