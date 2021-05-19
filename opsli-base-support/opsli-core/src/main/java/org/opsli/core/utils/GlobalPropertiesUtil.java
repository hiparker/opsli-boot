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
package org.opsli.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.opsli.core.autoconfigure.properties.GlobalProperties;
import org.opsli.core.msg.CoreMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.opsli.common.constants.OrderConstants.UTIL_ORDER;

/**
 * 系统配置参数 工具类
 *
 * @author parker
 * @date 2020-09-22 11:17
 */
@Slf4j
@Order(UTIL_ORDER)
@Component
@Lazy(false)
public class GlobalPropertiesUtil {

    /** 系统信息 */
    private static GlobalProperties GLOBAL_PROPERTIES;

    /** 增加初始状态开关 防止异常使用 */
    private static boolean IS_INIT;

    /**
     * 获得 系统配置信息
     * @return GlobalProperties
     */
    public static GlobalProperties getGlobalProperties(){
        // 判断 工具类是否初始化完成
        ThrowExceptionUtil.isThrowException(!IS_INIT,
                CoreMsg.OTHER_EXCEPTION_UTILS_INIT);

        return GLOBAL_PROPERTIES;
    }

    // ==================================

    /**
     * 初始化
     */
    @Autowired
    public void init(GlobalProperties globalProperties){
        // 获得 配置信息
        GlobalPropertiesUtil.GLOBAL_PROPERTIES = globalProperties;

        IS_INIT = true;
    }

}
