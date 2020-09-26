package org.opsli.api.wrapper.system.user;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.common.annotation.validation.ValidationArgs;
import org.opsli.common.annotation.validation.ValidationArgsMax;
import org.opsli.common.enums.ValiArgsType;
import org.opsli.plugins.excel.annotation.ExcelInfo;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 用户信息表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserModel extends ApiWrapper {


    /** 登录账户 */
    @ApiModelProperty(value = "登录账户")
    @ExcelIgnore
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL,ValiArgsType.IS_GENERAL})
    @ValidationArgsMax(32)
    private String username;

    /** 登录密码 */
    @ApiModelProperty(value = "登录密码")
    @ExcelIgnore
    // 验证器
    @ValidationArgsMax(50)
    private String password;

    /** 盐值，密码秘钥 */
    @ApiModelProperty(value = "盐值，密码秘钥")
    @ExcelIgnore
    // 验证器
    @ValidationArgsMax(50)
    private String secretkey;

    /** 是否锁定 */
    @ApiModelProperty(value = "是否锁定")
    @ExcelIgnore
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL})
    @ValidationArgsMax(1)
    private Character locked;

    /** 真实姓名 */
    @ApiModelProperty(value = "真实姓名")
    @ExcelProperty(value = "真实姓名", order = 1)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_NOT_NULL,ValiArgsType.IS_GENERAL_WITH_CHINESE})
    @ValidationArgsMax(50)
    private String realName;

    /** 手机 */
    @ApiModelProperty(value = "手机")
    @ExcelProperty(value = "手机", order = 2)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_MOBILE})
    private String mobile;

    /** 邮箱 */
    @ApiModelProperty(value = "邮箱")
    @ExcelProperty(value = "邮箱", order = 3)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_EMAIL})
    private String email;

    /** 工号 */
    @ApiModelProperty(value = "工号")
    @ExcelProperty(value = "工号", order = 4)
    @ExcelInfo
    // 验证器
    @ValidationArgs({ValiArgsType.IS_GENERAL})
    @ValidationArgsMax(32)
    private String no;

    /** 头像 */
    @ApiModelProperty(value = "头像")
    @ExcelIgnore
    // 验证器
    @ValidationArgsMax(255)
    private String avatar;

    /** 最后登陆IP */
    @ApiModelProperty(value = "最后登陆IP")
    @ExcelIgnore
    // 验证器
    @ValidationArgs(ValiArgsType.IS_IPV4)
    private String loginIp;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注", order = 5)
    @ExcelInfo
    // 验证器
    @ValidationArgsMax(255)
    private String remark;


    /** 多租户字段 */
    @ApiModelProperty(value = "多租户ID")
    @ExcelIgnore
    // 验证器
    @ValidationArgsMax(20)
    private String tenantId;


}
