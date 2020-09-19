package org.opsli.core.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.opsli.api.base.warpper.ApiWrapper;

/**
 *
 * Entity 基础类
 *
 * @author Parker
 * @date 2019-05-11
 *
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
	private Integer deleted;

	**/

}
