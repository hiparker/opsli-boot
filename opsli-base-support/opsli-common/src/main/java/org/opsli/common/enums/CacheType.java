package org.opsli.common.enums;

/**
 * 数据库类型
 *
 * @author Parker
 */
public enum CacheType {

    /** 缓存类型 */
    TIMED("timed", "时控数据"),
    EDEN("eden", "永久数据"),
    EDEN_HASH("edenhash", "永久Hash数据")

    ;

    private final String name;
    private final String desc;

    public static CacheType getCacheType(String cacheType) {
        CacheType[] var1 = values();
        for (CacheType type : var1) {
            if (type.name.equalsIgnoreCase(cacheType)) {
                return type;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.desc;
    }

    // =================

    CacheType(final String name, final String desc) {
        this.name = name;
        this.desc = desc;
    }
}