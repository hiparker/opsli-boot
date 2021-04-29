package org.opsli.modulars.tools.email.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultVo;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.modulars.tools.email.service.IEmailService;
import org.opsli.modulars.tools.email.wrapper.EmailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.tools.email.web
 * @Author: Parker
 * @CreateTime: 2020-09-13 17:40
 * @Description: 邮件 Controller
 */
@Api(tags = "邮件类")
@Slf4j
@ApiRestController("/email")
public class EmailRestController {

    @Autowired
    private IEmailService iEmailService;

    /**
     * 测试发送邮件
     * @param model 模型
     * @return ResultVo
     */
    @ApiOperation(value = "测试发送邮件", notes = "测试发送邮件")
    @PostMapping("/testSend")
    public ResultVo<?> testSend(EmailModel model) {
        try {
            String result = iEmailService
                    .send(model.getTo(), model.getSubject(), model.getContent());
            return ResultVo.success(result);
        }catch (Exception e){
            return ResultVo.error("邮件发送失败 - " + e.getMessage());
        }
    }


}
