package org.opsli.plugins.redisson.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.opsli.plugins.redisson.enums.RedissonType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 读取redis配置信息，封装到当前实体中
 *
 * @author xub
 * @date 2019/6/19 下午9:35
 */
@ConfigurationProperties(prefix = RedissonProperties.PROP_PREFIX)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RedissonProperties {

    public static final String PROP_PREFIX = "redisson.lock.server";

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * redis主机地址，ip：port，有多个用半角逗号分隔
     */
    private String address;

    /**
     * 连接类型，支持standalone-单机节点，sentinel-哨兵，cluster-集群，masterslave-主从
     */
    private RedissonType type = RedissonType.STANDALONE;

    /**
     * redis 连接密码
     */
    private String password;

    /**
     * 选取那个数据库
     */
    private int database;

    public RedissonProperties setPassword(String password) {
        this.password = password;
        return this;
    }

    public RedissonProperties setDatabase(int database) {
        this.database = database;
        return this;
    }

}
