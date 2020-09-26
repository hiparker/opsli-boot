package org.opsli.modulars.system.menu.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 菜单表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysMenu extends BaseEntity {


    /** 父级主键 */
    private String parentId;

    /** 菜单编号 */
    private String menuCode;

    /** 菜单名称 */
    private String menuName;

    /** 图标 */
    private String icon;

    /** 项目类型:1-菜单2-按钮3-链接4-表单 */
    private String type;

    /** url地址 */
    private String url;


    // ========================================

    /** 逻辑删除字段 */
    @TableLogic
    private Integer deleted;

}
