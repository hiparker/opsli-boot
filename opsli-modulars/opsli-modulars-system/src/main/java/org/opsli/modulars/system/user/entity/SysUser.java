package org.opsli.modulars.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 用户信息表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUser extends BaseEntity {


    /** 登录账户 */
    private String username;

    /** 登录密码 */
    private String password;

    /** 盐值，密码秘钥 */
    private String secretKey;

    /** 是否锁定 */
    private String locked;

    /** 真实姓名 */
    private String realName;

    /** 手机 */
    private String mobile;

    /** 邮箱 */
    private String email;

    /** 工号 */
    private String no;

    /** 头像 */
    private String avatar;

    /** 最后登陆IP */
    private String loginIp;

    /** 备注 */
    private String remark;

    // ========================================

    /** 逻辑删除字段 */
    @TableLogic
    private Integer deleted;

    /** 多租户字段 */
    private String tenantId;

}
