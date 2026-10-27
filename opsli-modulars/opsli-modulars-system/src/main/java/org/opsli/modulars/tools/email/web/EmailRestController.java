package org.opsli.modulars.tools.email.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.core.options.EmailConfigFactory;
import org.opsli.plugins.email.EmailPlugin;
import org.opsli.plugins.email.wrapper.EmailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 邮件 Controller
 *
 * @author Pace
 * @date 2020-09-13 17:40
 */
@Tag(name = "邮件类")
@Slf4j
@ApiRestController("/{ver}/tools/email")
public class EmailRestController {

    @Autowired
    private EmailPlugin emailPlugin;

    /**
     * 测试发送邮件
     * @param model 模型
     * @return ResultWrapper
     */
    @Operation(summary = "测试发送邮件")
    @PostMapping("/testSend")
    public ResultWrapper<?> testSend(@RequestBody EmailModel model) {
        try {
            emailPlugin
                    .send(model.getTo(), model.getSubject(), model.getContent(),
                            EmailConfigFactory.INSTANCE.getConfig());
            return ResultWrapper.getSuccessResultWrapperByMsg("邮件发送成功");
        }catch (Exception e){
            return ResultWrapper.getErrorResultWrapper().setMsg("邮件发送失败 - " + e.getMessage());
        }
    }

}
