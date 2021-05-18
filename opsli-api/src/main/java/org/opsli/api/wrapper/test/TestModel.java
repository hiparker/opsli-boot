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
package org.opsli.api.wrapper.test;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.plugins.excel.annotation.ExcelInfo;

/**
 * 测试类
 * 测试导入导出
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="测试接口返回Model",description="测试接口返回Model")
@Data
public class TestModel extends ApiWrapper {

    @ApiModelProperty(value = "名称")
    @ExcelProperty(value = "名称", order = 1)
    @ExcelInfo
    private String name;

    @ApiModelProperty(value = "分类")
    @ExcelProperty(value = "分类", order = 2)
    @ExcelInfo(dictType = "test_type")
    private String type;

    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注", order = 3)
    @ExcelInfo
    private String remark;

}
