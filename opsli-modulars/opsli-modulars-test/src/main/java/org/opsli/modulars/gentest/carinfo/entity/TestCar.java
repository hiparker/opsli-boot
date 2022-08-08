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
package org.opsli.modulars.gentest.carinfo.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.core.base.entity.BaseEntity;

/**
 * 测试汽车 Entity
 *
 * @author Parker
 * @date 2022-08-06 23:53:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TestCar extends BaseEntity {


    /** 汽车名称 */
    private String carName;

    /** 汽车类型 */
    private String carType;

    /** 汽车品牌 */
    private String carBrand;

    /** 生产日期 */
    private Date produceData;

    /** 是否启用 */
    private String izUsable;


    // ========================================






    /** 多租户字段 */
    private String tenantId;

    /** 逻辑删除字段 */
    @TableLogic
    private Integer deleted;


}