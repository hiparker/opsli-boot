package org.opsli.plugins.email.wrapper;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.common.annotation.validation.ValidationArgs;
import org.opsli.common.annotation.validation.ValidationArgsLenMax;
import org.opsli.common.enums.ValiArgsType;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 邮件服务
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmailModel {


    /** 收件人 */
    @ApiModelProperty(value = "收件人")
    @ValidationArgs({ValiArgsType.IS_EMAIL, ValiArgsType.IS_NOT_NULL})
    @ValidationArgsLenMax(200)
    private String to;

    /** 主题 */
    @ApiModelProperty(value = "主题")
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsLenMax(200)
    private String subject;

    /** 内容 */
    @ApiModelProperty(value = "内容")
    @ValidationArgsLenMax(20000)
    private String content;


}