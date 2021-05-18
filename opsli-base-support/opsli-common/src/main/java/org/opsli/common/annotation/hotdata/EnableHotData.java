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
package org.opsli.common.annotation.hotdata;

import java.lang.annotation.*;

/**
 * 添加在 Service get 方法上 ， 默认获得 传入对象 key为id的数据
 *
 * 调用 热点数据 不论增加缓存 还是 删除缓存
 *  返回值 必须为 集成了 BaseEntity 的 类
 *
 * 注意：不论是什么缓存，只要是缓存 就多少会有一致性的问题，针对不是那么重要的数据 且高频访问的数据可以缓存起来
 *
 * 主动式 推送热点数据
 *
 * 用于 注解类 - 直接按照类开启 增 删 经过缓存
 *
 * 并发更新 不建议使用 热数据 ☆☆☆☆☆
 *
 * @author Parker
 * @date 2020-09-16 16:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface EnableHotData {



}
