package org.opsli.plugins.redisson.constant;


/**
 * @author xub
 * @Description: 全局常量枚举 用来拼接完整的URL
 * @date 2019/6/19 下午9:09
 */
public enum GlobalConstant {

    /** 前缀 */
    REDIS_CONNECTION_PREFIX("redis://", "Redis地址配置前缀");

    private final String constant_value;
    private final String constant_desc;

    GlobalConstant(String constant_value, String constant_desc) {
        this.constant_value = constant_value;
        this.constant_desc = constant_desc;
    }

    public String getConstant_value() {
        return constant_value;
    }

    public String getConstant_desc() {
        return constant_desc;
    }
}
