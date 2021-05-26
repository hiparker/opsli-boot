package org.opsli.plugins.email.wrapper;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.common.annotation.validator.Validator;
import org.opsli.common.annotation.validator.ValidatorLenMax;
import org.opsli.common.enums.ValidatorType;

/**
 * 邮件服务
 *
 * @author Parker
 * @date 2020-09-19 20:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmailModel {


    /** 收件人 */
    @ApiModelProperty(value = "收件人")
    @Validator({ValidatorType.IS_EMAIL, ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(200)
    private String to;

    /** 主题 */
    @ApiModelProperty(value = "主题")
    @Validator({ValidatorType.IS_NOT_NULL})
    @ValidatorLenMax(200)
    private String subject;

    /** 内容 */
    @ApiModelProperty(value = "内容")
    @ValidatorLenMax(20000)
    private String content;


}