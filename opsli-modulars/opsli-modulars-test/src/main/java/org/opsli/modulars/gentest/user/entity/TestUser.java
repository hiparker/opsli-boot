/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.modulars.gentest.user.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * 某系统用户
 *
 * @author Parker
 * @date 2020-11-22 12:12:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TestUser extends BaseEntity {


    /** 名称 */
    private String name;

    /** 金钱 */
    private Double money;

    /** 年龄 */
    private Integer age;

    /** 生日 */
    private Date birth;

    /** 是否启用 */
    private String izUsable;


    // ========================================

    /** 多租户字段 */
    private String tenantId;

    /** 逻辑删除字段 */
    private String deleted;

}
