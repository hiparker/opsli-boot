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
package org.opsli.core.base.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.opsli.api.base.warpper.ApiWrapper;

/**
 * Entity 基础类
 *
 * @author Parker
 * @date 2020-09-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public abstract class BaseEntity extends ApiWrapper {

	private static final long serialVersionUID = 1L;


	/** 逻辑删除  子类需要 逻辑删除时 需要重写 父类 deleted 属性 并且加上 @TableLogic
	 *	数据库增加字段 deleted 类型为 Char 0 标示未删除  1 标示已删除
	 *
	 *  Entity 增加的 deleted 字段， 不需要同步更新到 Wrapper的Model中
	 *  Wrapper的Model 只是用于 对外展示
	 * */
	/**

	@TableLogic
	private String deleted;

	**/



	/**
	 * 多租户 ID ，如果要使用多租户模式的话 子类必须重写父类字段
	 * 且 数据库 有 tenant_id 字段 varchar类型 32 位
	 *
	 * 只需要加载 Entity上 Wrapper的Model不需要加字段，如果没有 tenantId 字段默认不是租户模式
	 * 且不可为空，为空的字段当数据量大起来时 查询会影响效率
	 */
	/**

	private String tenantId;

	*/


}
