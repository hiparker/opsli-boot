package org.opsli.api.base.encrypt;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 加解密
 *
 * @author Parker
 * @date 2021-01-24 12:48 下午
 **/
@Data
public class BaseEncrypt implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 加密数据 */
    @ApiModelProperty(value = "加密数据")
    @ExcelIgnore
    @TableField(exist = false)
    private String encryptData;

}
