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
package org.opsli.core.log.enums;


/**
 * 模块字典
 *
 * @author Parker
 * @date 2021年7月15日20:19:27
 */
public enum ModuleEnum {

    /** 模块 */
    MODULE_UNKNOWN("-1", "未知(请配置模块)"),
    MODULE_COMMON("00", "公共模块"),
    MODULE_USER("01", "用户模块"),
    MODULE_ROLE("02", "角色模块"),
    MODULE_MENU("03", "菜单模块"),
    MODULE_ORG("04", "组织模块"),
    MODULE_DICT("05", "字典模块"),
    MODULE_TENANT("06", "租户模块"),
    MODULE_AREA("07", "地区模块"),
    MODULE_MONITOR("08", "监控模块"),
    MODULE_GENERATOR("09", "代码生成器"),

    MODULE_OPERATION("11", "行为日志"),


    MODULE_TEST("100", "测试模块"),
    MODULE_TEST_USER("101", "测试用户模块"),
    MODULE_TEST_CAR("102", "测试汽车模块"),


    ;

    private final String id;
    private final String name;

    ModuleEnum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
