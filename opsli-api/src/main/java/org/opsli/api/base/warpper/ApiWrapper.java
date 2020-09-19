package org.opsli.api.base.warpper;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * Api 基础类
 *
 * 尽量 与本地 服务的 entity 保持一致（除去不想要暴露给 web的字段）
 *
 * api层级的 wrapper 也是对于数据安全性的一次包装
 *
 * Entity 增加的 deleted 字段， 不需要同步更新到 Wrapper的Model中
 * Wrapper的Model 只是用于 对外展示
 *
 * @author Parker
 * @date 2019-05-11
 *
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public abstract class ApiWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	/** ID */
	@TableId
	@ApiModelProperty(value = "ID")
	private String id;

	/** 创建人 */
	@ApiModelProperty(value = "创建人")
	private String createBy;

	/** 创建时间 */
	@ApiModelProperty(value = "创建时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/** 更新人 */
	@ApiModelProperty(value = "修改人")
	private String updateBy;

	/** 更新时间 */
	@ApiModelProperty(value = "修改时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/** 乐观锁 版本 */
	@ApiModelProperty(value = "版本")
	@Version
	private Integer version;

}
