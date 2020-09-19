package org.opsli.api.wrapper.test;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.opsli.api.base.warpper.ApiWrapper;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.entity
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:33
 * @Description: 测试类
 */
@Data
public class TestModel extends ApiWrapper {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "备注")
    private String remark;

}
