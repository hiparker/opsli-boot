package org.opsli.api.base.warpper;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.opsli.plugins.excel.annotation.CellStyleFormat;
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
@ContentRowHeight(16)
@HeadRowHeight(21)
@HeadFontStyle(fontName = "Arial",color = 9,fontHeightInPoints = 10)
@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 23)
@ColumnWidth(22)
public abstract class ApiWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	/** ID */
	@TableId
	@ApiModelProperty(value = "ID")
	@ExcelProperty(value = "ID", order = 1000)
	@CellStyleFormat
	private String id;

	/** 创建人 */
	@ApiModelProperty(value = "创建人")
	@ExcelProperty(value = "创建人", order = 1001)
	@CellStyleFormat
	private String createBy;

	/** 创建时间 */
	@ApiModelProperty(value = "创建时间")
	@ExcelProperty(value = "创建时间", order = 1002)
	@CellStyleFormat
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/** 更新人 */
	@ApiModelProperty(value = "修改人")
	@ExcelProperty(value = "修改人", order = 1003)
	@CellStyleFormat
	private String updateBy;

	/** 更新时间 */
	@ApiModelProperty(value = "修改时间")
	@ExcelProperty(value = "修改时间", order = 1004)
	@CellStyleFormat
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/** 乐观锁 版本 */
	@ApiModelProperty(value = "版本")
	@ExcelIgnore
	@Version
	private Integer version;

}
