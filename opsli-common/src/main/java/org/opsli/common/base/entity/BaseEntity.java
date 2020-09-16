package org.opsli.common.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * Entity 基础类
 *
 * @author Parker
 * @date 2019-05-11
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/** ID */
	@TableId(type = IdType.ASSIGN_ID)
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
	private Integer version;

}
