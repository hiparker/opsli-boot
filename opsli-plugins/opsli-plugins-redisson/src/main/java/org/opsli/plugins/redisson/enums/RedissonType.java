/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.plugins.redisson.enums;


/**
 * Redisson 类型
 *
 * @author : Parker
 * @date 2020-09-17 23:40
 */
public enum RedissonType {

    /** 类型 */
    STANDALONE("standalone", "单节点部署方式"),
    SENTINEL("sentinel", "哨兵部署方式"),
    CLUSTER("cluster", "集群方式"),
    MASTER_SLAVE("master_slave", "主从部署方式"),

    ;


    private final String type;
    private final String desc;

    public static RedissonType getType(String type) {
        RedissonType[] types = values();
        for (RedissonType e : types) {
            if (e.type.equalsIgnoreCase(type)) {
                return e;
            }
        }
        return null;
    }

    public String getType() {
        return this.type;
    }

    public String getDesc() {
        return this.desc;
    }

    // ================

    RedissonType(final String type, final String desc) {
        this.type = type;
        this.desc = desc;
    }

}
