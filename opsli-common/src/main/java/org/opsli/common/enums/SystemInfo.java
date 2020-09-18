package org.opsli.common.enums;

import java.util.UUID;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.common.enums
 * @Author: Parker
 * @CreateTime: 2020-09-17 23:40
 * @Description: 通过单例 模式 生成系统唯一标示
 */
public enum SystemInfo {

    /** 实例 */
    INSTANCE;

    private String systemID;

    SystemInfo(){
        // 生成系统ID
        systemID = UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 获得系统ID
     * @return String
     */
    public String getSystemID() {
        return systemID;
    }
}
